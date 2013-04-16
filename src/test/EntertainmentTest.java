/**
 * $Id: SeaTycoonTest.java 1991 2013-02-27 08:08:51Z yang.guo $
 * Copyright(C) 2011-2016 dreamingame.com. All rights reserved.
 */
package test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import util.MD5Util;
import util.ProtocolParseUtil;

/**
 * 
 * @author <a href="mailto:yang.guo@dreamingame.com">Guo Yang</a>
 * @version 1.0 2012-2-25 下午5:26:01
 */
public class EntertainmentTest {

	private static final String requestUrlImpart = "http://127.0.0.1:8080/EntertainmentServer/action/";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		for(int i=0;i<1;i++) {
			sendProtocol("getCitys");//categoryRecommendItem    itemCatGet
			System.out.println("i=" + i);
			Thread.sleep(5000);
		}
	}
	
	private static void sendProtocol(String protocol) throws Exception {
		String protocolName = protocol;//"checkversion";//"impart";//"connect";//"aircraft_buy";//"aircraft";//"airline.start";//"airline.fillfuel";//"airline.resume";//"airline.arrive";//"airline.collect";//"apron.construct";//"apron.complete";//"apron.boost";
		String content = ProtocolParseUtil.parseEntertainment(protocolName);
		
//		int pointIndex = protocolName.indexOf(".");
//		if(pointIndex > 0) {
//			protocolName = protocolName.substring(0, pointIndex);
//		}
		
		System.out.println(content);
		
		URL url = new URL(requestUrlImpart + protocolName);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
//		conn.setRequestMethod("POST");
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("X-Game-Checksum", MD5Util.toMD5(content + "-idodo"));
		
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		OutputStream os = conn.getOutputStream();
		os.write(content.getBytes("UTF-8"));
		os.close();
		
		System.out.println("response code : " + conn.getResponseCode());
		
		InputStream is = conn.getInputStream();
		byte[] b = new byte[4096];
		int n;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((n = is.read(b, 0, 4096)) > 0) {
			baos.write(b, 0, n);
		}
		is.close();
		
		System.out.println("response content : " + new String(baos.toByteArray(), "UTF-8"));
		
		conn.disconnect();
	}

}
