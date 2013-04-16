package util;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * @author gy
 */
public class ProtocolParseUtil {
	/**
	 * DREAMINGAME服务端、客户端自动测试工具。
	 * 主要测试客户端向服务端发送消息，消息需要配置xml放入protocol文件夹，命名方式为：消息名.xml
	 * @param args
	 * @throws DocumentException
	 */
	public static String parse(String protocolName) throws DocumentException {
		if(protocolName.isEmpty()) {
			System.out.println("protocolName is empty,please check~~");
			return "";
		}
		File file = new File("protocol");
		if(file.isDirectory()) {
			String[] filePath = file.list();
			for(String path : filePath) {
//				System.out.print(getProtocolNameByFileName(path) + "----");
				if(protocolName.equals(getProtocolNameByFileName(path))) {
					return operationProtocolXml(file.getPath() + "\\" + path);
				}
			}
		} else {
			System.out.println("path is not a directory,check file path input~~~");
		}
		
		System.out.println("maybe u r not put the protocol.xml file into the directory : protocol~");
		return "";
	}
	/**
	 * DREAMINGAME服务端、客户端自动测试工具。
	 * 主要测试客户端向服务端发送消息，消息需要配置xml放入protocol文件夹，命名方式为：消息名.xml
	 * @param args
	 * @throws DocumentException
	 */
	public static String parseEntertainment(String protocolName) throws DocumentException {
		if(protocolName.isEmpty()) {
			System.out.println("protocolName is empty,please check~~");
			return "";
		}
		File file = new File("entertainment");
		if(file.isDirectory()) {
			String[] filePath = file.list();
			for(String path : filePath) {
//				System.out.print(getProtocolNameByFileName(path) + "----");
				if(protocolName.equals(getProtocolNameByFileName(path))) {
					return operationProtocolXml(file.getPath() + "\\" + path);
				}
			}
		} else {
			System.out.println("path is not a directory,check file path input~~~");
		}
		
		System.out.println("maybe u r not put the protocol.xml file into the directory : protocol~");
		return "";
	}
	
	private static StringBuilder appendValue(String value) {
		return new StringBuilder("\"").append(value).append("\"");
	}
	
	@SuppressWarnings("rawtypes")
	private static String operationProtocolXml(String path) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(path);
		Element root = doc.getRootElement();
		Element foo, foooo;
		
		StringBuilder result = new StringBuilder();
		for (Iterator i = root.elementIterator("protocol"); i.hasNext();) {
			foo = (Element) i.next();
			StringBuilder subElement = new StringBuilder(appendValue(foo.attributeValue("name"))).append(":").append("{");
			//用来判断是不是没有param的情况，如果没有，就默认subElement加入一对双引号
			boolean flag = true;
			for(Iterator j = foo.elementIterator("param");j.hasNext();) {
				flag = false;
				foooo = (Element)j.next();
				if("string".equals(foooo.attributeValue("type"))) {
					subElement.append(appendValue(foooo.attributeValue("name"))).append(":").append(appendValue(foooo.getStringValue())).append(",");
				} else {
					subElement.append(appendValue(foooo.attributeValue("name"))).append(":").append(foooo.getStringValue()).append(",");
				}
			}
			if(flag) {
				subElement.append("}").append(",");
			} else {
				subElement = new StringBuilder(subElement.substring(0, subElement.length() - 1)).append("}").append(",");
			}
			result.append(subElement);
		}
		return "{" + result.substring(0, result.length() - 1) + "}";
	}
	
	private static String getProtocolNameByFileName(String fileName) {
		int lastIndex = fileName.indexOf(".xml");
		if(lastIndex == -1) {
			return null;
		}
		return fileName.substring(0, lastIndex);
	}
	
	public static Map<String, String> paramParse(String fileName) throws DocumentException {
		if(fileName.isEmpty()) {
			System.out.println("protocolName is empty,please check~~");
			return null;
		}
		File file = new File("configuration");
		if(file.isDirectory()) {
			String[] filePath = file.list();
			for(String path : filePath) {
//				System.out.print(getProtocolNameByFileName(path) + "----");
				if(fileName.equals(getProtocolNameByFileName(path))) {
					return getParamMap(file.getPath() + "\\" + path);
				}
			}
		} else {
			System.out.println("path is not a directory,check file path input~~~");
		}
		
		System.out.println("maybe u r not put the protocol.xml file into the directory : protocol~");
		return null;
	}
	
	private static Map<String, String> getParamMap(String path) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(path);
		Element root = doc.getRootElement();
		Element foo;
		
		Map<String, String> map = new HashMap<String, String>();
		for (@SuppressWarnings("unchecked")
		Iterator<Element> i = root.elementIterator("param"); i.hasNext();) {
			foo = i.next();
			
			map.put(foo.attributeValue("name"), foo.getStringValue());
		}
		return map;
	}
	
	public static void main(String args[]) throws DocumentException {
		Map<String, String> map = paramParse("addNode");
		
		for(String key : map.keySet()) {
			System.out.println(key + " ======= " + map.get(key));
		}
	}
}