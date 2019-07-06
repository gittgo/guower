package com.ourslook.guower.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 类的说明：生成验证码工具
 * @author 张林 
 * 创建时间：2013-12-2 下午02:50:02
 */
public final class ImageUtil {

	private static final char[] chars = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z'};
	private static final int SIZE = 4;
	private static final int LINES = 15;
	private static final int WIDTH = 75;
	private static final int HEIGHT = 30;
	private static final int FONT_SIZE = 20;

	public static Map<String, BufferedImage> createImage() {
		StringBuilder sb = new StringBuilder();
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		Random random = new Random();
		//  画随机字符
		for (int i = 1; i <= SIZE; i++) {
			int r = random.nextInt(chars.length);
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
			g.drawString(chars[r] + "", (i - 1) * WIDTH / SIZE, 23);
			// 将字符串保存在session
			sb.append(chars[r]);
		}
		// 画干扰线
		for (int i = 1; i <= LINES; i++) {
			g.setColor(getRandomColor());
			g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT),
					random.nextInt(WIDTH), random.nextInt(HEIGHT));
		}
		Map<String, BufferedImage> map = new HashMap<String, BufferedImage>();
		map.put(sb.toString(), bi);
		return map;
	}

	public static InputStream getInputStream(BufferedImage image)
			throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bao);
		encoder.encode(image);
		byte[] imageBts = bao.toByteArray();
		InputStream in = new ByteArrayInputStream(imageBts);
		return in;
	}

	public static Color getRandomColor() {
		Random random = new Random();
		Color color = new Color(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));
		return color;
	}

}
