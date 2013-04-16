package util;

import java.util.Random;

/**
 * RandomUtil.java
 * @author <a href="mailto:chenzhe.xu@dreamingame.com">chenzhe.xu</a>
 * @version 1.0 2012-9-7 下午2:25:50
 */
public class RandomUtil {
	private static final Random r = new Random();
	
	/**
	 * 随机丄1�7定个数的byte
	 * @param count
	 * @return
	 */
	public static byte[] genRandomByte(int count){
		byte[] randomB = new byte[count];
		 r.nextBytes(randomB);
		 return randomB;
	}
}
