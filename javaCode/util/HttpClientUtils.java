package com.futuredata.base.core.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.futuredata.base.core.config.AppConfig;
import com.futuredata.base.core.entity.FdResponse;
import com.futuredata.base.core.entity.Meta;
import com.futuredata.base.core.exception.FdSystemException;
import com.futuredata.base.core.util.JsonUtils;

/**
 * HttpClients工具类
 * 
 */
public class HttpClientUtils {

  private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

  static final int timeOut = 600 * 1000;

  private static CloseableHttpClient httpClient = null;

  private static void config(HttpRequestBase httpRequestBase) {
    // 配置请求的超时设置
    RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
        .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
    httpRequestBase.setConfig(requestConfig);
  }

  /**
   * 获取HttpClient对象
   * 
   */
  public synchronized static CloseableHttpClient getHttpClient(String url) {
    String hostname = url.split("/")[2];
    int port = 80;
    if (hostname.contains(":")) {
      String[] arr = hostname.split(":");
      hostname = arr[0];
      port = Integer.parseInt(arr[1]);
    }
    if (httpClient == null) {
      httpClient = createHttpClient(100, 20, 50, hostname, port);
    }
    return httpClient;
  }

  /**
   * 创建HttpClient对象
   * 
   */
  public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute,
      String hostname, int port) {
    ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
    LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", plainsf).register("https", sslsf).build();
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
    // 将最大连接数增加
    cm.setMaxTotal(maxTotal);
    // 将每个路由基础的连接增加
    cm.setDefaultMaxPerRoute(maxPerRoute);
    HttpHost httpHost = new HttpHost(hostname, port);
    // 将目标主机的最大连接数增加
    cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

    // 请求重试处理
    HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
      public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= 5) {// 如果已经重试了5次，就放弃
          return false;
        }
        if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
          return true;
        }
        if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
          return false;
        }
        if (exception instanceof InterruptedIOException) {// 超时
          return false;
        }
        if (exception instanceof UnknownHostException) {// 目标服务器不可达
          return false;
        }
        if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
          return false;
        }
        if (exception instanceof SSLException) {// SSL握手异常
          return false;
        }

        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        // 如果请求是幂等的，就再次尝试
        if (!(request instanceof HttpEntityEnclosingRequest)) {
          return true;
        }
        return false;
      }
    };

    CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
        .setRetryHandler(httpRequestRetryHandler).build();

    return httpClient;
  }

  private static void setPostParams(HttpPost httppost, Object params) {
    String json = JsonUtils.toJson(params);
    StringEntity entity = new StringEntity(json, "utf-8");// 解决中文乱码问题
    entity.setContentEncoding("UTF-8");
    entity.setContentType("application/json");
    httppost.setEntity(entity);
  }

  /**
   * post请求URL获取内容
   * 
   * @param url
   * @return
   */
  public static <U, T> T post(String ipaddr, String api, U params) {

    final String url = String.format("http://%s/%s", ipaddr, api);

    HttpPost httppost = new HttpPost(url);
    config(httppost);
    setPostParams(httppost, params);

    try (CloseableHttpResponse response =
        getHttpClient(url).execute(httppost, HttpClientContext.create())) {

      HttpEntity entity = response.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      EntityUtils.consume(entity);
      if (logger.isDebugEnabled()) {
        logger.debug(MessageFormat.format("请求地址:{0}, 请求参数:{1}, 返回结果:{2}", url, params, result));
      }

      FdResponse<T> fdResp = JsonUtils.toObject(result, new TypeReference<FdResponse<T>>() {});

      Meta meta = fdResp.getMeta();
      if (HttpStatus.OK.value() != meta.getCode().intValue()) {
        throw new FdSystemException(MessageFormat.format("服务返回结果异常:{0}", meta));
      }
      return fdResp.getData();

    } catch (IOException ex) {
      logger.error(url + "服务接口api调用异常", ex);
      throw new FdSystemException(MessageFormat.format("服务接口api调用异常:{0}", url));
    }
  }
  public static <T> T getMethod(String url) {
    HttpGet httpget = new HttpGet(url);
    config(httpget);
    CloseableHttpResponse response = null;
    try {
      response = getHttpClient(url).execute(httpget, HttpClientContext.create());
      HttpEntity entity = response.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      EntityUtils.consume(entity);
      if (logger.isDebugEnabled()) {
        logger.debug(MessageFormat.format("请求地址:{0}, 返回结果:{1}", url,  result));
      }

      FdResponse<T> fdResp = JsonUtils.toObject(result, new TypeReference<FdResponse<T>>() {});

      Meta meta = fdResp.getMeta();
      if (HttpStatus.OK.value() != meta.getCode().intValue()) {
        throw new FdSystemException(MessageFormat.format("服务返回结果异常:{0}", meta));
      }
      return fdResp.getData();
    } catch (IOException e) {
      logger.error(url + "服务接口api调用异常", e);
    } finally {
      try {
        if (response != null)
          response.close();
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
    return null;
    

  }
  /**
   * GET请求URL获取内容
   * 
   * @param url
   * @return
   */
  public static String get(String url) {
    HttpGet httpget = new HttpGet(url);
    config(httpget);
    CloseableHttpResponse response = null;
    try {
      response = getHttpClient(url).execute(httpget, HttpClientContext.create());
      HttpEntity entity = response.getEntity();
      String result = EntityUtils.toString(entity, "utf-8");
      EntityUtils.consume(entity);
      return result;
    } catch (IOException e) {
      logger.error(url + "服务接口api调用异常", e);
    } finally {
      try {
        if (response != null)
          response.close();
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
    return null;
  }

  public static void main(String[] args) {
    // URL列表数组
    String[] urisToGet = {"http://www.baidu.com", "http://www.baidu.com", "http://www.baidu.com",
        "http://www.baidu.com"};

    long start = System.currentTimeMillis();
    try {
      int pagecount = urisToGet.length;
      ExecutorService executors = Executors.newFixedThreadPool(pagecount);
      CountDownLatch countDownLatch = new CountDownLatch(pagecount);
      for (int i = 0; i < pagecount; i++) {
        HttpGet httpget = new HttpGet(urisToGet[i]);
        config(httpget);
        // 启动线程抓取
        executors.execute(new GetRunnable(urisToGet[i], countDownLatch));
      }
      countDownLatch.await();
      executors.shutdown();
    } catch (InterruptedException e) {
      logger.error("ES服务接口api调用异常", e);
      Thread.currentThread().interrupt();
    } finally {
      System.out.println("线程" + Thread.currentThread().getName() + "," + System.currentTimeMillis()
          + ", 所有线程已完成，开始进入下一步！");
    }

    long end = System.currentTimeMillis();
    System.out.println("consume -> " + (end - start));
  }

  static class GetRunnable implements Runnable {
    private CountDownLatch countDownLatch;
    private String url;

    public GetRunnable(String url, CountDownLatch countDownLatch) {
      this.url = url;
      this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
      try {
        System.out.println(HttpClientUtils.get(url));
      } finally {
        countDownLatch.countDown();
      }
    }
  }
}
