package com.litaook.lottery;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {

	public static void choose() {
		String path = null;
		// 设置界面风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFileChooser jdir = new JFileChooser();
		// 设置选择路径模式
		jdir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// 设置对话框标题
		jdir.setDialogTitle("请选择路径");
		if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {// 用户点击了确定
			path = jdir.getSelectedFile().getAbsolutePath();// 取得路径选择
		}
		System.out.println(path);
	}

	public static void main(String[] args) {
		Test.choose();
	}

}
