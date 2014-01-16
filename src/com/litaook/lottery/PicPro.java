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
import java.awt.GridLayout;

public class PicPro extends WindowAdapter implements ActionListener {
	JFrame frame;
	JPanel panel1, panel3, panel4, panel5;
	JTextField tf1, tf2, tf3, tf4;
	JTextArea ta;
	JLabel label1, label2, label3, label4;
	JButton button, button1, button2;
	Font font1 = new Font("宋体", Font.PLAIN, 20);
	Font font2 = new Font("宋体", Font.PLAIN, 16);

	File filein;
	File fileout;
	File[] filelist;
	File wzw = new File("wzw");
	int width;
	int height;
	boolean flag_in;
	boolean flag_out;
	boolean flag_num;

	PicPro() {
		frame = new JFrame("批量图片处理");
		frame.setSize(800, 800);
		frame.setLocation(300, 10);
		frame.setResizable(false);
		panel1 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		tf1 = new JTextField(30);
		tf2 = new JTextField(30);
		tf3 = new JTextField(7);
		tf4 = new JTextField(7);
		tf1.setFont(font1);
		tf2.setFont(font1);
		tf3.setFont(font1);
		tf4.setFont(font1);
		label1 = new JLabel("源文件夹");
		label2 = new JLabel("目标文件夹");
		label3 = new JLabel("转换后X:");
		label4 = new JLabel("转换后Y:");
		label1.setFont(font2);
		label2.setFont(font2);
		label3.setFont(font2);
		label4.setFont(font2);
		ta = new JTextArea();
		ta.setFont(font2);
		ta.setEditable(false);
		ta.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(ta);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add(scroller, BorderLayout.CENTER);
		ta.setText("请填写路径");
		button = new JButton("Change");
		button1 = new JButton("Check");
		button2 = new JButton("Check");
		button.setFont(font2);
		button1.setFont(font2);
		button2.setFont(font2);
		panel1.setLayout(new GridLayout(3, 1));
		panel1.add(panel3);
		panel1.add(panel4);
		panel1.add(panel5);
		panel3.add(label1);
		panel3.add(tf1);
		panel3.add(button1);
		panel4.add(label2);
		panel4.add(tf2);
		panel4.add(button2);
		panel5.add(label3);
		panel5.add(tf3);
		panel5.add(label4);
		panel5.add(tf4);
		panel5.add(button);
		frame.add(panel1, BorderLayout.NORTH);
		frame.setVisible(true);

		button.addActionListener(this);
		button1.addActionListener(this);
		button2.addActionListener(this);
		frame.addWindowListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			flag_num = false;
			if (checkNum(tf3.getText()) == true && checkNum(tf4.getText())) {
				width = (int) Double.parseDouble(tf3.getText());
				height = (int) Double.parseDouble(tf4.getText());
				flag_num = true;
			} else {
				flag_num = false;
				ta.append("XY填写格式不正确,请重新填写\n");
			}
			if (flag_in == true && flag_out == true && flag_num == true) {
				ta.append("目标文件列表\n");
				int cnt = 0;
				for (int i = 0; i < filelist.length; i++) {
					if (filelist[i] != wzw) {
						File outtemp;
						try {
							RenderedImage im = convert(width, height,
									ImageIO.read(filelist[i]));
							String outputFilename = filelist[i].getName();
							String format = outputFilename
									.substring(outputFilename.lastIndexOf('.') + 1);
							outtemp = new File(fileout + "/" + outputFilename);
							// System.out.println(outputFilename);
							if (outtemp.exists() == true) {
								int restartChoose = JOptionPane
										.showConfirmDialog(frame,
												"有相同名称的文件，是否覆盖？", "提示",
												JOptionPane.OK_CANCEL_OPTION);
								if (restartChoose == JOptionPane.OK_OPTION) {
									ImageIO.write(im, format, outtemp);
									ta.append(outtemp.getAbsolutePath());
									ta.append("\t\t" + "Finished" + "\n");
									cnt++;
								} else {
									return;
								}
							} else {
								ImageIO.write(im, format, outtemp);
								ta.append(outtemp.getAbsolutePath());
								ta.append("\t\t" + "Finished" + "\n");
								cnt++;
							}
						} catch (IOException e2) {
							e2.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				ta.append("目标文件共" + cnt + "个\n");
			}
		}

		else if (e.getSource() == button1) {
			flag_in = false;
			filein = new File(tf1.getText());
			if (filein.exists() == true && filein.isDirectory() == true) {
				ta.append("\n源文件夹填写格式正确\n");
				flag_in = true;
				filelist = filein.listFiles();
				for (int i = 0; i < filelist.length; i++) {
					if (filelist[i].isFile() == false) {
						filelist[i] = wzw;
					}
				}
				String ispic;
				for (int i = 0; i < filelist.length; i++) {
					if (filelist[i] != wzw) {
						String temp;
						temp = filelist[i].getName();
						ispic = temp.substring(temp.lastIndexOf('.') + 1);
						if ((ispic.equals("jpg") == false)
								&& (ispic.equals("bmp") == false)
								&& (ispic.equals("gif") == false)
								&& (ispic.equals("png") == false)
								&& (ispic.equals("JPG") == false)
								&& (ispic.equals("BMP") == false)
								&& (ispic.equals("GIF") == false)
								&& (ispic.equals("PNG") == false)) {
							filelist[i] = wzw;
						}
					}
				}
				ta.append("源文件列表:\n");
				int num = 0;
				for (int i = 0; i < filelist.length; i++) {
					if (filelist[i] != wzw) {
						ta.append(filelist[i] + "\n");
						num++;
					}
				}
				ta.append("源文件共" + num + "个\n");
				ta.append("\n");
			} else {
				ta.append("\n源文件夹不存在或者输入格式错误！\n");
				flag_in = false;
			}

		} else if (e.getSource() == button2) {
			flag_out = false;
			fileout = new File(tf2.getText());
			if (fileout.exists() == true && fileout.isDirectory() == true) {
				ta.append("目标文件夹填写格式正确\n");
				flag_out = true;

			} else {
				ta.append("\n目标文件夹不存在或者输入格式错误！\n");
				flag_out = false;
			}
		}
	}

	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	/**
	 * 转换尺寸
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

			output = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_RGB);
			output.getGraphics().drawImage(
					input.getScaledInstance(newWidth, newHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
		} else {
			output = input;
		}
		return output;
	}

	public static boolean checkNum(String s) { // 判断String是否为可转化为数字的字符串
		int i;
		boolean flag = false;
		char paste_ch[] = s.toCharArray();
		for (i = 0; i < s.length(); i++) {
			if ((paste_ch[i] != '1') && (paste_ch[i] != '2')
					&& (paste_ch[i] != '3') && (paste_ch[i] != '4')
					&& (paste_ch[i] != '5') && (paste_ch[i] != '6')
					&& (paste_ch[i] != '7') && (paste_ch[i] != '8')
					&& (paste_ch[i] != '9') && (paste_ch[i] != '0')
					&& (paste_ch[i] != '.') && (paste_ch[i] != '-')) {
				flag = false;
				break;
			}
			if ((s.contains("-") == true && s.startsWith("-") == false)
					|| (s.contains(".") == true && s.startsWith(".") == true)) {
				flag = false;
				break;
			}
			flag = true;
		}
		return flag;
	}

	public static void main(String[] args) throws Exception {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		new PicPro();
	}

}
