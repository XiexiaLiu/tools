package com.futuredata.base.core.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.futuredata.base.core.config.AppConfig;
import com.futuredata.base.core.exception.FdSystemException;
import com.futuredata.base.web.model.system.Attachment;

/**
 * 附件工具类.
 * 
 * @author haichao.sang
 *
 */
public class AttachmentUtils {

	private static final Logger logger = LoggerFactory.getLogger(AttachmentUtils.class);

	// 附件的存储路径
	private static String ATTACHMENT_PATH = AppConfig.get("file.storage.path");
	private static String WORK_PATH = AppConfig.get("file.work.path");

	/**
	 * 获取附件内容.
	 *
	 * @param fileMap
	 * @return
	 */
	public static List<Attachment> getAttachments(Map<String, MultipartFile> fileMap) {
		List<Attachment> attachments = new ArrayList<Attachment>();
		if (fileMap == null || fileMap.isEmpty()) {
			return attachments;
		}

		try {
			for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
				// 取得上传文件
				MultipartFile file = entry.getValue();
				// 取得当前上传文件的文件名称
				String orgFileName = file.getOriginalFilename();
				// 取得文件的大小
				String fileSize = FileUtils.byteCountToDisplaySize(file.getSize());

				// 如果名称不为"",说明该文件存在，否则说明该文件不存在
				if (!StringUtils.isEmpty(orgFileName)) {
					Attachment attachment = new Attachment();
					attachment.setAttachmentName(orgFileName);
					attachment.setId(AppUtils.randomUUID());
					attachment.setAttachment(file.getBytes());
					attachment.setAttachmentSize(fileSize);
					attachments.add(attachment);
				}
			}
		} catch (IOException ex) {
			logger.error("文件上传失败", ex);
			throw new FdSystemException("文件上传失败");
		}
		return attachments;
	}

	public static Attachment getAttachment(Map<String, MultipartFile> fileMap) {

		return getAttachments(fileMap).get(0);
	}

	/**
	 * 读取附件内容.
	 *
	 * @param attachmentId
	 * @return
	 */
	public static byte[] getAttachment(String attachmentId) {
		try {
			return FileUtils.readFileToByteArray(new File(ATTACHMENT_PATH + attachmentId));
		} catch (IOException ex) {
			logger.error("文件读取失败", ex);
		}
		return null;
	}

	/**
	 * 压缩文件.
	 * 
	 * @param ownerName
	 * @param attachments
	 * 
	 * @return 压缩文件路径
	 */
	public static String zipAttachment(String ownerName, List<Map<String, String>> attachments, String pathFactor) {

		List<String> files = new ArrayList<String>();

		String workPath = WORK_PATH + pathFactor + "/";

		for (Map<String, String> attachment : attachments) {
			String src = ATTACHMENT_PATH + attachment.get("id");
			String dest = workPath + attachment.get("attachmentName");
			try {
				FileUtils.copyFile(new File(src), new File(dest));
			} catch (IOException e) {
				logger.error("复制文件失败，源文件：" + src + ",目标地址:" + dest, e);
			}

			files.add(dest);
		}

		return ZipUtils.zip(workPath + ownerName + ".zip", files);
	}

	/**
	 * 压缩文件.
	 * 
	 * @param attachments
	 * @param pathFactor
	 * 
	 * @return 压缩文件路径
	 */
	public static String zipAttachment(List<String> attachments, String pathFactor) {

		String workPath = WORK_PATH + pathFactor + "/";

		return ZipUtils.zip(workPath + AppUtils.randomUUID(), attachments);
	}

	public static void saveAttachment(byte[] attachment, String attachmentId) {

		try {
			FileUtils.writeByteArrayToFile(new File(ATTACHMENT_PATH + attachmentId), attachment);
		} catch (IOException ex) {
			logger.error("附件保存失败", ex);
		}
	}

	/**
	 * 删除附件.
	 *
	 * @param attachmentId
	 *            附件Id
	 */
	public static void delAttachment(String attachmentId) {
		FileUtils.deleteQuietly(new File(ATTACHMENT_PATH + attachmentId));
	}

	/**
	 * 批量删除附件.
	 *
	 * @param attachmentIds
	 *            附件Ids
	 */
	public static void delAttachments(List<String> attachmentIds) {
		for (String attachmentId : attachmentIds) {
			delAttachment(attachmentId);
		}
	}

}
