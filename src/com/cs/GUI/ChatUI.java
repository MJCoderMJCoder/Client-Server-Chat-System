/**
 * 
 */
package com.cs.GUI;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.cs.client.TcpClient;
import com.cs.client.UdpClient;
import com.cs.message.DisconnectMessage;
import com.cs.message.Message;
import com.cs.message.MessageTo;
import com.cs.user.User;
import com.cs.util.GZipUtils;

/**
 * @author MJCoder
 *
 */
public class ChatUI {
	public static long userId;
	public static Socket socket;
	public static boolean isConnecting = true;

	// static StringBuilder uidReceiver = null; // 消息接收者uid

	private static ClientFrame cframe = null;

	/**
	 * @param socket
	 */
	public ChatUI(String username) {
		// 创建客户端窗口对象
		cframe = new ClientFrame(username);
		// 窗口关闭键无效，必须通过退出键退出客户端以便善后
		cframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // JFrame.DO_NOTHING_ON_CLOSE
		// 获取本机屏幕横向分辨率
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		// 获取本机屏幕纵向分辨率
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 将窗口置中
		cframe.setLocation((w - cframe.WIDTH) / 2, (h - cframe.HEIGHT) / 2);
		// 设置客户端窗口为可见
		cframe.setVisible(true);

		userId = System.currentTimeMillis();
		TcpClient.login(userId, username);
		socket = TcpClient.getClient();

		try {
			InputStream inFromServer = socket.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			// 获取服务端欢迎信息并将欢迎信息打印在聊天消息框内
			cframe.jtaChat.append(in.readUTF());
			cframe.jtaChat.append("\n");
		} catch (Exception e) {
			cframe.jtaChat.append("服务器挂了.....\n");
			e.printStackTrace();
		}
		new Thread() {
			public void run() {
				// 持续等待接收服务器信息直至退出
				while (isConnecting) {
					try {
						InputStream inputStream = socket.getInputStream();
						if (inputStream.available() > 0) {
							byte buffer[] = new byte[inputStream.available()];
							inputStream.read(buffer);
							String message = new String(GZipUtils.uncompress(buffer));
							System.out.println("客户端接收到的服务端消息：" + message);
							// String message = new DataInputStream(socket.getInputStream()).readUTF();
							String sender = message.substring(0, message.indexOf(":"));
							String word = message.substring(message.indexOf(":") + 1);
							// 在聊天窗打印聊天信息
							cframe.jtaChat.append(cframe.simpleDateFormat.format(new Date()) + "\t" + sender + ":\n\t" + word + "\n\n");
							// 显示最新消息
							cframe.jtaChat.setCaretPosition(cframe.jtaChat.getDocument().getLength());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	/**
	 * 更新在线用户
	 */
	public static void updateOnline(String onlineUsers) {
		// 提取在线列表的数据模型
		DefaultTableModel tbm = (DefaultTableModel) cframe.jtbOnline.getModel();
		// 清除在线名单列表
		tbm.setRowCount(0);
		// 更新在线名单
		String[] onlinelist = onlineUsers.split(";");
		// 逐一添加当前在线者
		for (String member : onlinelist) {
			System.out.println(member);
			String[] rowData = new String[2];
			rowData[0] = member.substring(0, member.indexOf("="));
			rowData[1] = member.substring(member.indexOf("=") + 1);
			// 如果是自己则不在名单中显示
			if (userId == Long.parseLong(rowData[0])) {
				continue;
			} else {
				// 添加当前在线者之一
				tbm.addRow(rowData);
			}
		}
		// 提取在线列表的渲染模型
		DefaultTableCellRenderer tbr = new DefaultTableCellRenderer();
		// 表格数据居中显示
		tbr.setHorizontalAlignment(JLabel.CENTER);
		cframe.jtbOnline.setDefaultRenderer(Object.class, tbr);
		cframe.jtbOnline.invalidate();
	}
}

// 客户端窗口
class ClientFrame extends JFrame {
	// 时间显示格式
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	// 窗口宽度
	final int WIDTH = 700;
	// 窗口高度
	final int HEIGHT = 700;
	// 创建发送按钮
	JButton btnSend = new JButton("发送");
	// 创建清除按钮
	JButton btnClear = new JButton("清屏");
	// 创建退出按钮
	JButton btnExit = new JButton("退出");

	// 创建消息接收者标签
	JLabel lblReceiver = new JLabel("发送给：");

	// 创建文本输入框, 参数分别为行数和列数
	JTextArea jtaSay = new JTextArea();

	// 创建聊天消息框
	JTextArea jtaChat = new JTextArea();

	// 当前在线列表的列标题
	// String[] colTitles = { "用户名", "IP地址", "端口号" };
	String[] colTitles = { "账号", "用户名" };
	// 当前在线列表的数据
	String[][] rowData = null;
	// 创建当前在线列表
	JTable jtbOnline = new JTable(new DefaultTableModel(rowData, colTitles) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// 表格不可编辑，只可显示
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	});

	// 创建聊天消息框的滚动窗
	JScrollPane jspChat = new JScrollPane(jtaChat);

	// 创建当前在线列表的滚动窗
	JScrollPane jspOnline = new JScrollPane(jtbOnline);

	// 设置默认窗口属性，连接窗口组件
	public ClientFrame(String username) {
		// 标题
		setTitle(username);
		// 大小
		setSize(WIDTH, HEIGHT);
		// 不可缩放
		setResizable(false);
		// 设置布局:不适用默认布局，完全自定义
		setLayout(null);

		// 设置按钮大小和位置
		btnSend.setBounds(20, 600, 200, 40);
		btnClear.setBounds(240, 600, 200, 40);
		btnExit.setBounds(460, 600, 200, 40);

		// 设置标签大小和位置
		lblReceiver.setBounds(20, 420, 600, 30);

		// 设置按钮文本的字体
		btnSend.setFont(new Font("宋体", Font.BOLD, 18));
		btnClear.setFont(new Font("宋体", Font.BOLD, 18));
		btnExit.setFont(new Font("宋体", Font.BOLD, 18));

		// 添加按钮
		this.add(btnSend);
		this.add(btnClear);
		this.add(btnExit);

		// 添加标签
		this.add(lblReceiver);

		// 设置文本输入框大小和位置
		jtaSay.setBounds(20, 460, 650, 120);
		// 设置文本输入框字体
		jtaSay.setFont(new Font("楷体", Font.BOLD, 16));
		// 添加文本输入框
		this.add(jtaSay);

		// 聊天消息框自动换行
		jtaChat.setLineWrap(true);
		// 聊天框不可编辑，只用来显示
		jtaChat.setEditable(false);
		// 设置聊天框字体
		jtaChat.setFont(new Font("楷体", Font.BOLD, 16));

		// 设置滚动窗的水平滚动条属性:不出现
		jspChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// 设置滚动窗的垂直滚动条属性:需要时自动出现
		jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// 设置滚动窗大小和位置
		jspChat.setBounds(20, 20, 360, 400);
		// 添加聊天窗口的滚动窗
		this.add(jspChat);

		// 设置滚动窗的水平滚动条属性:不出现
		jspOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// 设置滚动窗的垂直滚动条属性:需要时自动出现
		jspOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// 设置当前在线列表滚动窗大小和位置
		jspOnline.setBounds(420, 20, 250, 400);
		// 添加当前在线列表
		this.add(jspOnline);

		// 添加发送按钮的响应事件
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (jtaSay.getText().contains(";") || jtaSay.getText().contains("=disconnect") || jtaSay.getText().contains("username=")) {
					jtaSay.setText("不能包含特殊字符，比如：;=&");
				} else {
					try {
						// 取得在线列表的数据模型
						DefaultTableModel tbm = (DefaultTableModel) jtbOnline.getModel();
						// 提取鼠标选中的行作为消息目标，最少一个人，最多全体在线者接收消息
						int[] selectedIndex = jtbOnline.getSelectedRows();
						if (selectedIndex.length > 0) {
							StringBuilder stringBuilder = new StringBuilder("");
							for (int i = 0; i < selectedIndex.length; i++) {
								Message message = new MessageTo(ChatUI.userId, jtaSay.getText(), Long.parseLong((String) tbm.getValueAt(selectedIndex[i], 0)));
								stringBuilder.append(message.toString());
								stringBuilder.append(";");
							}
							// 在聊天窗打印发送动作信息
							jtaChat.append(simpleDateFormat.format(new Date()) + "\t" + username + "：\n\t");
							// 显示发送消息
							jtaChat.append(jtaSay.getText() + "\n\n");
							// 向服务器发送聊天信息
							OutputStream outToServer = ChatUI.socket.getOutputStream();
							DataOutputStream out = new DataOutputStream(outToServer);
							// out.writeUTF(stringBuilder.toString());
							out.write(GZipUtils.compress(stringBuilder.toString()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						// 文本输入框清除
						jtaSay.setText("");
					}
					// 显示最新消息
					jtaChat.setCaretPosition(jtaChat.getDocument().getLength());
				}
			}
		});
		// 添加清屏按钮的响应事件
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// 聊天框清屏
				jtaChat.setText("");
			}
		});
		// 添加退出按钮的响应事件
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// 向服务器发送退出信息
				ChatUI.isConnecting = false;
				try {
					OutputStream outToServer = ChatUI.socket.getOutputStream();
					DataOutputStream out = new DataOutputStream(outToServer);
					Message message = new DisconnectMessage(ChatUI.userId, "");
					// out.writeUTF(message.toString());
					out.write(GZipUtils.compress(message.toString()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// 退出
					System.exit(0);
				}
			}
		});
		// 添加在线列表项被鼠标选中的相应事件
		jtbOnline.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent event) {
				// 取得在线列表的数据模型
				DefaultTableModel tbm = (DefaultTableModel) jtbOnline.getModel();
				// 提取鼠标选中的行作为消息目标，最少一个人，最多全体在线者接收消息
				int[] selectedIndex = jtbOnline.getSelectedRows();
				StringBuilder stringBuilder = new StringBuilder("");
				for (int i = 0; i < selectedIndex.length; i++) {
					// stringBuilder.append((String) tbm.getValueAt(selectedIndex[i], 0));
					// stringBuilder.append("=");
					stringBuilder.append((String) tbm.getValueAt(selectedIndex[i], 1));
					if (i != selectedIndex.length - 1) {
						stringBuilder.append("；");
					}
				}
				lblReceiver.setText("发送给：" + stringBuilder.toString());
			}

			@Override
			public void mousePressed(MouseEvent event) {
			};

			@Override
			public void mouseReleased(MouseEvent event) {
			};

			@Override
			public void mouseEntered(MouseEvent event) {
			};

			@Override
			public void mouseExited(MouseEvent event) {
			};
		});
	}
}
