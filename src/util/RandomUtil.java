package util;

import java.util.Random;

/**
 * RandomUtil.java
 * @author <a href="mailto:chenzhe.xu@dreamingame.com">chenzhe.xu</a>
 * @version 1.0 2012-9-7 涓嬪崍2:25:50
 */
public class RandomUtil {
	private static final Random r = new Random();
	
	/**
	 * 闅忔満涓�瀹氫釜鏁扮殑byte
	 * @param count
	 * @return
	 */
	public static byte[] genRandomByte(int count){
		byte[] randomB = new byte[count];
		 r.nextBytes(randomB);
		 return randomB;
	}
}
