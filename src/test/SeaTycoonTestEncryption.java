/**
 * $Id: SeaTycoonTestEncryption.java 2072 2013-03-08 09:12:26Z yang.guo $
 * Copyright(C) 2011-2016 dreamingame.com. All rights reserved.
 */
package test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import util.ProtocolParseUtil;

import com.nx.commons.communication.io.DeflateInputResolver;
import com.nx.commons.communication.io.DeflateOutputResolver;
import com.nx.commons.communication.io.XorInputResolver;
import com.nx.commons.communication.io.XorOutputResolver;
import com.nx.commons.lang.encrypt.MD5Util;

/**
 * 
 * @author <a href="mailto:yang.guo@dreamingame.com">Guo Yang</a>
 * @version 1.0 2012-2-25 涓嬪崍5:26:01
 */
public class SeaTycoonTestEncryption {

//	private static final String requestUrlImpart = "http://54.248.22.28/seatycoon/api/";
//	private static final String requestUrlImpart = "http://ko.eos.dreamingame.net/seatycoon/api/";
	private static final String requestUrlImpart = "http://192.168.0.56/seatycoon/api/";
//	private static final String requestUrlImpart = "http://gameserver.eraofsail.com/seatycoon/api/";
//	private static final String requestUrlImpart = "http://gameserver_cn.eraofsail.com/seatycoon/api/";
//	private static final String requestUrlImpart = "http://ko.eos.dreamingame.net/seatycoon/api/";
//	private static final String requestUrlImpart = "http://192.168.0.13/seatycoon/api/";
//	private static final String requestUrlImpart = "http://192.168.0.22/seatycoon_test/api/";
//	private static final String requestUrlImpart = "http://192.168.0.22/seatycoon/api/";
//	private static final String requestUrlImpart = "http://gameserver-test.eraofsail.com/seatycoon/api/";
//	private static final String requestUrlImpart = "http://gameserver-test.eraofsail.com/seatycoon_test/api/";
//	private static final String requestUrlImpart = "http://localhost/seatycoon/api/";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		for(int i=0;i<1;i++) {
			sendProtocol("protect.stop");
			System.out.println("i=" + i);
			Thread.sleep(5000);
		}
	}
	
	private static void sendProtocol(String protocol) throws Exception {
		String protocolName = protocol;//"checkversion";//"impart";//"connect";//"aircraft_buy";//"aircraft";//"airline.start";//"airline.fillfuel";//"airline.resume";//"airline.arrive";//"airline.collect";//"apron.construct";//"apron.complete";//"apron.boost";
		String content = ProtocolParseUtil.parse(protocolName);
		
		int pointIndex = protocolName.indexOf(".");
		if(pointIndex > 0) {
			protocolName = protocolName.substring(0, pointIndex);
		}
		
		System.out.println(content);
		
		URL url = new URL(requestUrlImpart + protocolName);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
//		content = "1111111111111111" + content;
		byte[] retValue = content.getBytes("UTF-8");
		//压缩+加密+16byte
		//压缩
		retValue = new DeflateOutputResolver().resolve(retValue);
		//加密
		retValue = new XorOutputResolver(97).resolve(retValue);
		//+16byte
		byte[] bb = "1111111111111111".getBytes("utf-8");
		byte[] returnx = new byte[retValue.length + bb.length];
		System.arraycopy(bb, 0, returnx, 0, bb.length);
		System.arraycopy(retValue, 0, returnx, bb.length, retValue.length);
//		byte[] returnx = ("1111111111111111" + new String(retValue)).getBytes();  
				
		byte[] md5Checker = new byte[returnx.length + 6];
		System.arraycopy(returnx, 0, md5Checker, 0, returnx.length);
		System.arraycopy("-idodo".getBytes("UTF-8"), 0, md5Checker, returnx.length, 6);
//		String checkStr = "1111111111111111" + new String(retValue) + "-idodo";
		String mddd5 = MD5Util.encrypt(md5Checker);
//		System.out.println("%%%%% " + checkStr);
		System.out.println("***** " + mddd5);
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("X-Game-Checksum", mddd5);
		
		conn.setDoOutput(true);
		conn.setDoInput(true);
		OutputStream os = conn.getOutputStream();
		os.write(returnx);
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
		
		byte[] bytes = baos.toByteArray();
		// 获得明文byte数组
		byte[] result = new byte[bytes.length - 16];
		System.arraycopy(bytes, 16, result, 0, result.length);
		//解密
		byte[] tmp = new XorInputResolver(97).resolve(result);
		//解压缩
		tmp=new DeflateInputResolver().resolve(tmp);
		// 请求明文
		content = new String(tmp, "utf-8");
		
		
		System.out.println("response content : " + content);
		
		conn.disconnect();
	}

}
