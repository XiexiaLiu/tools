package testWebservice;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.io.FileUtils;

import test.webService.AES;

public class WebServiceUtil {
  public static String request(String wsdl, String namespace, String method, String[] paramNames, String[] paramValues) throws Exception {
    Service service = new Service();// 创建客户端调用webservice的代理对象
    Call call = (Call) service.createCall();// 创建一个调用对象，代表对web service 的一次调用
    call.setTargetEndpointAddress(new java.net.URL(wsdl)); // 设置web service的url 地址
    call.setOperationName(new QName(namespace, method)); // 设置操作名称，QName 对象的两个参数分别为命名空间和方法名称
    for (String paramName : paramNames) {
      call.addParameter(paramName, XMLType.XSD_STRING, ParameterMode.IN);// 为本次调用的方法增加参数，参数名称，参数类型：字符串类型，参数模式：入参
    }
    call.setReturnType(XMLType.XSD_STRING);// 为本次调用方法设置返回类型，这里是字符串类型
    String result = (String) call.invoke(paramValues); // 执行调用操作，result 保存返回的结果,invoke 的参数为实参
    return result;
  }

  public static void main(String[] args) throws IOException {
    String wsdl = "http://139.0.31.80:8081/fd-data-share/service/sqtjIService";
    String nameSpace = "http://www.futuredata.com/ws/wsdl/sqtjIService";
    String method = "closeCase";
    String[] paramNames = new String[] {"userId", "requestXML"};
    String content=FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\ws_content.txt"));
    String[] paramValues = new String[] {"SQTJPT002", AES.Encrypt(content, "54c03b615b33cc41")};
    try {
      String res = request(wsdl, nameSpace, method, paramNames, paramValues);
      System.out.println(res);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
