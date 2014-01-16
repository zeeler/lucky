package com.litaook.lottery;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * 抽奖主程序
 * 
 * @author litao
 * 
 */
public class Lucky implements ActionListener {
	// 最大图片数
	final static int NUM_IMAGES = 1000;
	// 开始图片索引
	final static int START_INDEX = 0;
	// 真实图片数
	int real_num_images = 0;
	// 图片对象数组
	ImageIcon[] images = new ImageIcon[NUM_IMAGES];
	// 图片名称数组
	String[] imageNames = new String[NUM_IMAGES];
	// 主要几个面板
	JPanel mainPanel, selectPanel, displayPanel, resultPanel;
	// 操作按键
	JButton phaseChoice = null;
	// 显示结果标签
	JLabel phaseIconLabel = null, phaseResult = null, phaseBlank1 = null,
			phaseBlank2 = null;

	// 屏幕宽
	int screenWidth = 0;
	// 屏幕高
	int screenHeight = 0;
	// 边距
	final static int DISPLAY_MARGIN = 100;
	final static int MARGIN = 10;
	// 按键大小
	final static int BUTTON_WIDTH = 200;
	final static int BUTTON_HEIGHT = 200;
	// 结果框大小
	final static int RESULT_WIDTH = 400;
	final static int RESULT_HEIGHT = 200;

	// 工作目录
	String rootDir = System.getProperty("user.dir") + File.separator;
	// 图片目录
	final static String IMAGE_DIR = "images/";

	// Constructor
	public Lucky() {
		// 检查屏幕尺寸
		checkScreenSize();

		// 创建各个面板区域
		selectPanel = new JPanel();
		selectPanel.setBounds(screenWidth / 2 - BUTTON_WIDTH, screenHeight
				- MARGIN - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT - MARGIN);

		displayPanel = new JPanel();
		displayPanel
				.setBounds(DISPLAY_MARGIN, MARGIN, screenWidth - DISPLAY_MARGIN
						* 2, screenHeight - BUTTON_HEIGHT - MARGIN * 2);

		resultPanel = new JPanel();
		resultPanel.setBounds(screenWidth / 2 + MARGIN, screenHeight - MARGIN
				- BUTTON_HEIGHT, RESULT_WIDTH, RESULT_HEIGHT - MARGIN);

		// 自定义布局面板
		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		// 添加到主面板中
		mainPanel.add(selectPanel);
		mainPanel.add(displayPanel);
		mainPanel.add(resultPanel);

		// 添加插件，读取所有图片入数组
		addWidgets();
	}

	/**
	 * 检查屏幕大小
	 */
	private void checkScreenSize() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screensize.getWidth();// 得到宽
		screenHeight = (int) screensize.getHeight();// 得到高
	}

	/**
	 * 检查图片文件夹是否存在
	 * 
	 * @return
	 */
	private boolean imageDirExists() {
		boolean res = false;
		File dir = new File(rootDir + IMAGE_DIR);
		if (dir.exists() && dir.isDirectory()) {
			res = true;
		}
		return res;
	}

	/**
	 * 读取图片并存入指定数组
	 */
	private void readImagesToArrays() {
		// 图片计数器
		int j = 1;

		// 获取图片路径，并存入数组
		String path = new StringBuffer(rootDir).append(IMAGE_DIR).toString();

		// 避免因找不到图片目录而出现空指针异常
		if (!imageDirExists()) {
			StringBuffer msg = new StringBuffer("没有找到图片目录: ").append(path);
			System.out.println(msg);
			System.exit(0);
		}

		// 读取图片
		File dir = new File(path);
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				continue; // 忽略目录
			}
			// 文件名
			String fileName = files[i].getName();

			// 忽略系统文件
			if (fileName.equals("Thumbs.db") || fileName.startsWith("."))
				continue;

			// 文件名后缀(转换为小写,否则不能兼容大写后缀名)
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();

			// 忽略非jpg格式图片
			if (!suffix.equals("jpg")) {
				continue;
			}

			// 读入图
			ImageIcon icon = null;
			try {
				// 可直接使用files[i], 无需构造URL
				BufferedImage bi = ImageIO.read(files[i]);
				if (bi == null)
					continue;
				icon = new ImageIcon(bi);
			} catch (IOException e) {
				continue;
			}

			// 图片入数组
			images[j] = icon;
			// 图片名称入数组
			imageNames[j] = fileName;
			j++;
		}
		real_num_images = j;
	}

	/**
	 * 创建插件，并显示图
	 */
	private void addWidgets() {
		// 读取图片
		readImagesToArrays();

		// 使用JLabel来显示图片，全部居中显示
		phaseIconLabel = new JLabel();
		phaseIconLabel.setHorizontalAlignment(JLabel.CENTER);
		phaseIconLabel.setVerticalAlignment(JLabel.CENTER);
		phaseIconLabel.setVerticalTextPosition(JLabel.CENTER);
		phaseIconLabel.setHorizontalTextPosition(JLabel.CENTER);
		phaseIconLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		phaseResult = new JLabel();
		phaseBlank1 = new JLabel();
		phaseBlank2 = new JLabel();

		// 创建控制按键
		phaseChoice = new JButton("开始/停止");

		// 显示第一张图片
		phaseIconLabel.setIcon(images[START_INDEX]);
		phaseIconLabel.setText("");

		// 各区域设置边框
		selectPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("操作区"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		resultPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("结果区"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// 图片区域设置边框
		displayPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("照片列表"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// 在显示面板上添加各
		selectPanel.add(phaseChoice);
		displayPanel.add(phaseIconLabel);
		resultPanel.add(phaseBlank1);
		resultPanel.add(phaseResult);
		resultPanel.add(phaseBlank2);

		// 给控制按键设置监听器
		phaseChoice.addActionListener(this);
	}

	/**
	 * 产生随机ID
	 * 
	 * @return
	 */
	private int random(int max) {
		return (int) (Math.random() * real_num_images) + 1;
	}

	boolean run = false;

	// 监控并显示中奖结果
	public void actionPerformed(ActionEvent event) {
		if (run) {
			run = false;
			phaseBlank1.setText("恭喜");
			phaseBlank2.setText("中奖啦！");
			// 删除获奖照片
			removeLuckyImage(phaseResult.getText());
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			System.out.println(df.format(new Date()) + ", 获奖照片 : " + phaseResult.getText());
			// 重建图片文件列表
			readImagesToArrays();

		} else {
			run = true;

			new Thread() {
				public void run() {
					while (run) {
						// 随机显示图片
						int a = random(real_num_images);
						phaseIconLabel.setIcon(images[a]);
						phaseBlank1.setText("");
						// 显示图片名称，不要扩展名
						String imageName = imageNames[a];
						phaseResult.setText(imageName.substring(0,
								imageName.lastIndexOf(".")));
						//System.out.println(imageName);
						phaseBlank2.setText("");
						try {
							Thread.sleep(100);
						} catch (Exception e) {
							System.err.println(e);
						}
					}
				}
			}.start();
		}
	}

	/**
	 * 删除获奖人照片
	 */
	private void removeLuckyImage(String imageName) {
		String path = new StringBuffer(rootDir).append(IMAGE_DIR).append(imageName).append(".jpg").toString();
		//System.out.println(path);
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
	}

	/**
	 * 主函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Lucky luck = new Lucky();

		// 创建JFrame
		JFrame frame = new JFrame("抽奖啦");

		// 设置外观
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		// 设置frame参数
		frame.setContentPane(luck.mainPanel);
		frame.setExtendedState(6);
		frame.setUndecorated(false);
		// 关闭窗口退出
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 开始显示
		frame.pack();
		frame.setVisible(true);
	}
}
