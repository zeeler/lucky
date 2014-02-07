package com.litaook.lottery;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import com.litaook.lottery.util.Tools;

import java.awt.GridLayout;

/**
 * 批量处理图片，将图片缩小到统一尺寸范围内，并且将处理完的保存到新目录中
 * 
 * @author litao
 * 
 */
public class ResizeImage extends WindowAdapter implements ActionListener {

	// 主Frame
	JFrame frame;
	// 几个Panel，其中源文件路径、目标文件路径和新尺寸几个Panel都放到mainPanel中
	JPanel mainPanel, srcPathPanel, dstPathPanel, newSizePanel;
	// 文本框
	JTextField srcPath, dstPath, newWidth, newHeight;
	// 文字输出
	JTextArea txtArea;
	// 文字标签
	JLabel srcPathLabel, dstPathLabel, newWidthLabel, newHeightLabel;
	// 按键
	JButton convertButton, srcPathButton, dstPathButton;
	// 文件选择器
	JFileChooser fileChooser = new JFileChooser(".");
	// 字体设置
	Font font = new Font("宋体", Font.PLAIN, 12);
	Font errFont = new Font("黑体", Font.BOLD, 14);
	// 窗口尺寸设置
	static final int FRAME_WIDTH = 600;
	static final int FRAME_HEIGHT = 600;
	// 窗口位置
	static final int LOCAL_X = 300;
	static final int LOCAL_Y = 10;
	// 指定调整后的图片尺寸
	int width = 0;
	int height = 0;

	/**
	 * 构造函数
	 */
	ResizeImage() {
		// 主窗口
		frame = new JFrame("批量图片缩放处理");
		// 设置主窗口大小
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		// 设置主窗口起始位置
		frame.setLocation(LOCAL_X, LOCAL_Y);
		frame.setResizable(true);
		// 主面板
		mainPanel = new JPanel();

		// 设置源路径控件
		setSrcPath();
		// 设置目标路径控件
		setDstPath();
		// 设置新尺寸控件
		setNewSize();
		// 设置文本区控件
		setTxtArea();

		// 三行一列布局
		mainPanel.setLayout(new GridLayout(3, 1));
		mainPanel.add(srcPathPanel);
		mainPanel.add(dstPathPanel);
		mainPanel.add(newSizePanel);

		// 添加文本控件
		JScrollPane scroller = new JScrollPane(txtArea);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add(scroller, BorderLayout.CENTER);
		// 添加主面板
		frame.add(mainPanel, BorderLayout.NORTH);
		frame.setVisible(true);
		// 添加监听器
		frame.addWindowListener(this);
	}

	/**
	 * 设置原路径面板
	 */
	void setSrcPath() {
		srcPathPanel = new JPanel();
		// 源文件夹路径输入框
		srcPath = new JTextField(30);
		srcPath.setFont(font);
		srcPathLabel = new JLabel("源文件夹");
		srcPathLabel.setFont(font);
		srcPathButton = new JButton("浏览...");
		srcPathButton.setFont(font);
		srcPathPanel.add(srcPathLabel);
		srcPathPanel.add(srcPath);
		srcPathPanel.add(srcPathButton);
		// 添加监听器
		srcPathButton.addActionListener(this);
	}

	/**
	 * 设置目标路径面板
	 */
	void setDstPath() {
		dstPathPanel = new JPanel();
		// 目标文件夹路径输入框
		dstPath = new JTextField(30);
		dstPath.setFont(font);
		dstPathLabel = new JLabel("目标文件夹");
		dstPathLabel.setFont(font);
		dstPathButton = new JButton("浏览...");
		dstPathButton.setFont(font);
		dstPathPanel.add(dstPathLabel);
		dstPathPanel.add(dstPath);
		dstPathPanel.add(dstPathButton);
		// 添加监听器
		dstPathButton.addActionListener(this);
	}

	/**
	 * 设置新尺寸面板
	 */
	void setNewSize() {
		setNewWidthHeight();

		newSizePanel = new JPanel();
		newSizePanel.add(newWidthLabel);
		newSizePanel.add(newWidth);
		newSizePanel.add(newHeightLabel);
		newSizePanel.add(newHeight);

		// 转换按键
		convertButton = new JButton("开始处理图片");
		convertButton.setFont(font);
		// 按键添加到面板里
		newSizePanel.add(convertButton);
		// 添加监听器
		convertButton.addActionListener(this);
	}

	/**
	 * 设置新宽高面板
	 */
	void setNewWidthHeight() {
		// 转换后宽
		newWidth = new JTextField(7);
		// 转换后高
		newHeight = new JTextField(7);

		newWidth.setFont(font);
		newHeight.setFont(font);

		newWidthLabel = new JLabel("请设定转换后的尺寸 ==> 宽:");
		newHeightLabel = new JLabel("高:");

		newWidthLabel.setFont(font);
		newHeightLabel.setFont(font);
	}

	/**
	 * 设置文本框参数
	 */
	void setTxtArea() {
		txtArea = new JTextArea();
		// 设置自动上滚文本区内容
		DefaultCaret caret = (DefaultCaret) txtArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		txtArea.setFont(font);
		txtArea.setEditable(false);
		txtArea.setLineWrap(true);
		txtArea.setText("请选择源文件夹和目标文件夹...\n");
	}

	/**
	 * 转换按键事件处理
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == srcPathButton) {// 是否按下原路径按键
			new Thread() {// 开启一个新线程
				// 覆盖run()
				public void run() {
					srcPath.setText(getOpenDirPath());
				}
			}.start();// 启动线程
		} else if (e.getSource() == dstPathButton) {// 是否按下目标路径按键
			new Thread() {// 开启一个新线程
				// 覆盖run()
				public void run() {
					dstPath.setText(getOpenDirPath());
				}
			}.start();// 启动线程
		} else if (e.getSource() == convertButton) {// 是否按下按键
			// 判断源文件路径和目标文件路径是否合法
			if (checkSrcPath() && checkDstPath()) {
				// 判断输入的数字是否合法
				if (Tools.checkNum(newWidth.getText())
						&& Tools.checkNum(newWidth.getText())) {
					// 获取输入的宽和高
					width = (int) Double.parseDouble(newWidth.getText());
					height = (int) Double.parseDouble(newHeight.getText());
					new Thread() {// 开启一个新线程
						public void run() {// 开始转换图片
							convertButton();
						}
					}.start();
				} else {
					txtArea.append("XY填写格式不正确,请重新填写\n");
				}
			} else {
				txtArea.append("XY填写格式不正确,请重新填写\n");
			}
		}
	}

	/**
	 * 打开文件目录，并返回选择后的路径
	 */
	String getOpenDirPath() {
		String path = "";
		// 限制近浏览目录
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("打开文件夹");
		int ret = fileChooser.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			// 获取目录绝对路径并设置给srcPath
			path = fileChooser.getSelectedFile().getAbsolutePath();
		}
		return path;
	}

	/**
	 * 检查源文件路径是否合法
	 * 
	 * @return
	 */
	boolean checkSrcPath() {
		boolean result = false;
		File f = new File(srcPath.getText());
		if (f.exists() && f.isDirectory()) {
			result = true;
		}
		return result;
	}

	/**
	 * 检查目标文件路径是否合法
	 * 
	 * @return
	 */
	boolean checkDstPath() {
		boolean result = false;
		File f = new File(dstPath.getText());
		if (f.exists() && f.isDirectory()) {
			result = true;
		}
		return result;
	}

	/**
	 * 转换按键事件，缩放图片
	 */
	void convertButton() {
		txtArea.append("\n缩放后的文件列表 : \n");

		// 从源文件文件夹依次取图片
		File[] filelist = new File(srcPath.getText()).listFiles();
		// 有效文件计数器
		int cnt = 0;
		// 开始循环处理文件
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i] != null && filelist[i].exists()) {
				File outFullPath = null;
				try {
					// 转换尺寸
					RenderedImage ri = convert(width, height,
							ImageIO.read(filelist[i]));
					// 输出文件名
					String outputFilename = filelist[i].getName();
					// 文件格式
					String format = outputFilename.substring(outputFilename
							.lastIndexOf('.') + 1);
					// 输出文件绝对路径和文件名
					outFullPath = new File(dstPath.getText() + "/"
							+ outputFilename);
					if (outFullPath.exists() == true) {// 目标文件已经存在，是否覆盖
						int restartChoose = JOptionPane.showConfirmDialog(
								frame, "有相同名称的文件，是否覆盖？", "提示",
								JOptionPane.OK_CANCEL_OPTION);
						if (restartChoose == JOptionPane.OK_OPTION) {
							// 写文件
							writeToDstFile(ri, format, outFullPath);
							cnt++;
						} else {
							return;
						}
					} else {// 目标文件不存在，写新文件
						// 写文件
						writeToDstFile(ri, format, outFullPath);
						cnt++;
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		txtArea.append("\n共处理有效文件 " + cnt + " 个!\n");
	}

	/**
	 * 写目标文件
	 * 
	 * @param ri
	 * @param format
	 * @param outFullPath
	 */
	void writeToDstFile(RenderedImage ri, String format, File outFullPath) {
		try {
			ImageIO.write(ri, format, outFullPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		txtArea.append(outFullPath.getAbsolutePath());
		txtArea.append("\t\t" + "Done" + "\n");
	}

	/**
	 * 关闭窗口
	 */
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	/**
	 * 按照原图比例缩小尺寸
	 * 
	 * @param width
	 * @param height
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage convert(int width, int height,
			BufferedImage input) throws Exception {
		BufferedImage output = null;

		// 获得缩放的比例
		double ratio = 1.0;
		// 判断如果高、宽都不大于设定值，则不处理
		if (input.getHeight() > height || input.getWidth() > width) {
			if (input.getHeight() > input.getWidth()) {
				ratio = (double) height / (double) input.getHeight();
			} else {
				ratio = (double) width / (double) input.getWidth();
			}
			// 计算新的图面宽度和高度
			int newWidth = (int) ((double) input.getWidth() * ratio);
			int newHeight = (int) ((double) input.getHeight() * ratio);
			// 先创建个新尺寸的空图文件
			output = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			// 再把缩小后的图形数据填充到空图文件中
			output.getGraphics().drawImage(
					input.getScaledInstance(newWidth, newHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
		} else {
			output = input;
		}
		return output;
	}

	/**
	 * 选择路径
	 * 
	 * @return
	 */
	public static String chooseDir() {
		String path = "";
		JFileChooser jdir = new JFileChooser();
		// 设置选择路径模式
		jdir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// 设置对话框标题
		jdir.setDialogTitle("请选择路径");
		if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {// 用户点击了确定
			path = jdir.getSelectedFile().getAbsolutePath();// 取得路径选择
		}
		return path;
	}

	/**
	 * 主函数，从这里开始执行
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		new ResizeImage();
	}

}
