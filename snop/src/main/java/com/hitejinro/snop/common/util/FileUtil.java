package com.hitejinro.snop.common.util;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component("fileUtil")
public class FileUtil {

	private static final String filePath = "C:\\HITEJINRO\\snop\\uploader\\";

	private static final String downloadFilePath = "C:\\HITEJINRO\\snop\\download\\";
	private static final String templateFilePath = "C:\\HITEJINRO\\snop\\template\\";
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 파일 업로드
	 * @param params 업로드 결과를 반환받을 Map object
	 * @param request HttpServletRequest
	 * @return fileUploadResult[{"originalFileName", "storedFileName", "storedFilePath"}]
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public Map<String, Object> uploadFile(Map<String, Object> params, HttpServletRequest request) throws Exception {

		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

		//        String sUploadFilePath = filePath + DateUtil.getToday("yyyyMMdd") + "\\";
		DateTimeFormatter inputFormatYmd = DateTimeFormatter.ofPattern("yyyymmdd");
		DateTimeFormatter inputFormatHms = DateTimeFormatter.ofPattern("HHmmss");
		String sUploadFilePath = filePath + LocalDateTime.now().format(inputFormatYmd);

		File file = new File(sUploadFilePath);
		if (file.exists() == false) {
			file.mkdirs();
		}

		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		String storedFilePath = "";

		List<Map<String, String>> arrResult = new ArrayList<Map<String, String>>(); // - 파일의 서버 저장결과 : [{"originalFileName", "storedFileName", "storedFilePath"}]

		while (iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());

			if (!multipartFile.isEmpty()) {
				if (multipartFile != null) {
					originalFileName = multipartFile.getOriginalFilename(); // - 원본파일명
					originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // - 확장자
					storedFileName = LocalDateTime.now().format(inputFormatYmd) + "_" + LocalDateTime.now().format(inputFormatHms) + "_" + originalFileName; // - 저장할 파일명
					storedFilePath = sUploadFilePath + storedFileName;

					file = new File(storedFilePath);
					logger.info(originalFileName + " ==> " + storedFilePath);
					multipartFile.transferTo(file);

					Map<String, String> mResult = new HashMap<String, String>();
					mResult.put("originalFileName", originalFileName);
					mResult.put("storedFileName", storedFileName);
					mResult.put("storedFilePath", storedFilePath);
					arrResult.add(mResult);

				}
			}
		}

		params.put("fileUploadResult", arrResult);

		return params;
	}

	/**
	 * 서버에 저장된 파일을 내려받는다.
	 * @param map {storedFileName : 서버에 저장된 파일명, downloadFileName : 클라이언트에 저장될 파일명}
	 * @param response HttpServletResponse
	 * @param isTemplateFile 해당 파일이 템플릿 파일(미리 생성해서 올려놓은 파일)인지 여부 : 해당 값에 따라서 경로가 달라진다 : "C:\SCP\template", "C:\SCP\download"
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public void downloadFile(Map<String, Object> map, HttpServletResponse response, boolean isTemplateFile) throws IOException {

		String storedFileName = (String) map.get("storedFileName"); // - 서버에 저장된 파일 명
		String downloadFileName = (String) map.get("downloadFileName"); // - 클라이언트에 다운로드될 파일 명

		String sFilePath = this.downloadFilePath;
		if (isTemplateFile) {
			sFilePath = this.templateFilePath;
		}

		byte fileByte[] = FileUtils.readFileToByteArray(new File(sFilePath + storedFileName));

		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(downloadFileName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.getOutputStream().write(fileByte);

		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	/**
	 * 서버에 저장된 파일을 내려받는다.
	 * <BR/>경로 : "C:\SCP\download"
	 * @param map
	 * @param response
	 * @throws IOException
	 */
	public void downloadFile(Map<String, Object> map, HttpServletResponse response) throws IOException {
		this.downloadFile(map, response, false);
	}

	public String getStringFromStream(InputStream is) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public byte[] readFully(InputStream is, int length, boolean readAll) throws IOException {
		byte[] output = {};
		if (length == -1)
			length = Integer.MAX_VALUE;
		int pos = 0;
		while (pos < length) {
			int bytesToRead;
			if (pos >= output.length) { // Only expand when there's no room
				bytesToRead = Math.min(length - pos, output.length + 1024);
				if (output.length < pos + bytesToRead) {
					output = Arrays.copyOf(output, pos + bytesToRead);
				}
			} else {
				bytesToRead = output.length - pos;
			}
			int cc = is.read(output, pos, bytesToRead);
			if (cc < 0) {
				if (readAll && length != Integer.MAX_VALUE) {
					throw new EOFException("Detect premature EOF");
				} else {
					if (output.length != pos) {
						output = Arrays.copyOf(output, pos);
					}
					break;
				}
			}
			pos += cc;
		}
		return output;
	}

	/**
	 * 첨부파일 경로 반환
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

}
