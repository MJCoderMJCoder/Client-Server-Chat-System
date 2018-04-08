/**
 * 
 */
package com.cs.GUI;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.cs.client.TcpClient;
import com.cs.client.UdpClient;
import com.cs.server.TcpServer;

/**
 * @author MJCoder
 *
 */
public class MainUI extends JFrame implements ActionListener {

	// 定义组件
	JButton jb1, jb2 = null; // , jb3
	// JRadioButton jrb1, jrb2 = null;
	JPanel jp1, jp4 = null; // jp2, jp3,
	JTextField jtf = null;
	JLabel jlb1 = null; // , jlb2, jlb3
	// JPasswordField jpf = null;
	// ButtonGroup bg = null;

	public static void main(String[] args) {
		MainUI mUI = new MainUI();
	}

	public MainUI() {
		// 创建组件
		jb1 = new JButton("登录");
		jb2 = new JButton("重置");
		// jb3 = new JButton("退出");

		// 设置监听
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		// jb3.addActionListener(this);

		// jrb1 = new JRadioButton("教师");
		// jrb2 = new JRadioButton("学生");
		// bg = new ButtonGroup();
		// bg.add(jrb1);
		// bg.add(jrb2);
		// jrb2.setSelected(true); // 初始页面默认选择权限为 学生

		jp1 = new JPanel();
		// jp2 = new JPanel();
		// jp3 = new JPanel();
		jp4 = new JPanel();

		jlb1 = new JLabel("用户名：");
		// jlb2 = new JLabel("密 码：");
		// jlb3 = new JLabel("权 限：");

		jtf = new JTextField(10);
		// jpf = new JPasswordField(10);
		// 加入到JPanel中
		jp1.add(jlb1);
		jp1.add(jtf);

		// jp2.add(jlb2);
		// jp2.add(jpf);

		// jp3.add(jlb3); // 添加标签
		// jp3.add(jrb1);
		// jp3.add(jrb2);

		jp4.add(jb1); // 添加按钮
		jp4.add(jb2);
		// jp4.add(jb3);

		// 加入JFrame中
		this.add(jp1);
		// this.add(jp2);
		// this.add(jp3);
		this.add(jp4);

		this.setLayout(new GridLayout(2, 1)); // 选择GridLayout布局管理器
		this.setTitle("C/S Chat System");
		this.setSize(200, 100);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置当关闭窗口时，保证JVM也退出
		// 获取本机屏幕横向分辨率
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		// 获取本机屏幕纵向分辨率
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 将窗口置中
		this.setLocation((w - this.getWidth()) / 2, (h - this.getHeight()) / 2);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) { // 事件判断

		if (e.getActionCommand() == "登录") {
			// // 如果选中教师登录
			// if (jrb1.isSelected()) {
			// tealogin(); // 连接到教师的方法 页面
			// } else if (jrb2.isSelected()) // 学生在登录系统
			// {
			// stulogin(); // 连接到学生的方法 页面
			// }
			login();
		} else if (e.getActionCommand() == "重置") {
			clear();
		}
	}

	private void login() {
		if (jtf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入用户名！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else {
			String username = jtf.getText().toString();
			JOptionPane.showMessageDialog(null, "登录成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
			dispose();
			clear();
			ChatUI chatUI = new ChatUI(username); // 创建新界面
		}
	}

	// 学生登录判断方法
	public void stulogin() {
		// if (stu_name.equals(jtf.getText()) && stu_pwd.equals(jpf.getText())) {
		// JOptionPane.showMessageDialog(null, "登录成功！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// dispose();
		// clear();
		// StdUI ui = new StdUI(); // 创建新界面
		// } else if (jtf.getText().isEmpty() && jpf.getText().isEmpty()) {
		// JOptionPane.showMessageDialog(null, "请输入用户名和密码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// } else if (jtf.getText().isEmpty()) {
		// JOptionPane.showMessageDialog(null, "请输入用户名！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// } else if (jpf.getText().isEmpty()) {
		// JOptionPane.showMessageDialog(null, "请输入密码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// } else {
		// JOptionPane.showMessageDialog(null, "用户名或者密码错误！\n请重新输入", "提示消息",
		// JOptionPane.ERROR_MESSAGE);
		// // 清空输入框
		// clear();
		// }
	}

	// 教师登录判断方法
	public void tealogin() {
		// if (tea_name.equals(jtf.getText()) && tea_pwd.equals(jpf.getText())) {
		//
		// JOptionPane.showMessageDialog(null, "登录成功！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// clear();
		// dispose();
		// TerUI ui = new TerUI(); // 创建一个新界面
		// } else if (jtf.getText().isEmpty() && jpf.getText().isEmpty()) {
		// JOptionPane.showMessageDialog(null, "请输入用户名和密码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// } else if (jtf.getText().isEmpty()) {
		// JOptionPane.showMessageDialog(null, "请输入用户名！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// } else if (jpf.getText().isEmpty()) {
		// JOptionPane.showMessageDialog(null, "请输入密码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// } else {
		// JOptionPane.showMessageDialog(null, "用户名或者密码错误！\n请重新输入", "提示消息",
		// JOptionPane.ERROR_MESSAGE);
		// clear(); // 清空输入框
		// }
	}

	// 清空文本框和密码框
	public void clear() {
		jtf.setText("");
		// jpf.setText("");
	}
}
