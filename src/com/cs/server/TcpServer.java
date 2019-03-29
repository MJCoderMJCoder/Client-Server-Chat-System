/**
 * 
 */
package com.cs.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.cs.user.User;
import com.cs.util.GZipUtils;

/**
 * @author MJCoder
 *
 */
public class TcpServer extends Thread {
	private ServerSocket serverSocket;
	private static List<User> users = new ArrayList<User>(); // 缓存当前已在线的客户端信息

	public TcpServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort());
				Socket server = serverSocket.accept();
				System.out.println("server：" + server);
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						boolean isConnecting = true;
						while (isConnecting) {
							try {
								// System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
								InputStream inputStream = server.getInputStream();
								// DataInputStream in = new DataInputStream(inputStream);
								// String userData = in.readUTF();
								if (inputStream.available() > 0) {
									byte buffer[] = new byte[inputStream.available()];
									inputStream.read(buffer);
									String userData = new String(GZipUtils.uncompress(buffer));
									System.out.println("客户端请求：" + userData);
									if (userData.contains("username=")) {
										users.add(new User(Long.parseLong(userData.substring(userData.indexOf("=") + 1, userData.indexOf("&"))),
												userData.substring(userData.lastIndexOf("=") + 1), server));
										DataOutputStream out = new DataOutputStream(server.getOutputStream());
										out.writeUTF("服务器：欢迎来到 C/S Chat System");
										UdpServer.udpSend(users);
									} else if (userData.contains("=disconnect")) {
										for (User user : users) {
											if (user.getUserId() == Long.parseLong(userData.substring(userData.lastIndexOf("=") + 1))) {
												isConnecting = false;
												users.remove(user);
												break;
											}
										}
										UdpServer.udpSend(users);
									} else {
										String[] sendTo = userData.split(";");
										for (int i = 0; i < sendTo.length; i++) {
											System.out.println(sendTo[i]);
											for (User user : users) {
												if (user.getUserId() == Long.parseLong(sendTo[i].substring(sendTo[i].indexOf("userId=") + 7, sendTo[i].indexOf("&")))) {
													System.out.println(user.getSocket());
													DataOutputStream out = new DataOutputStream(user.getSocket().getOutputStream());
													for (User temp : users) {
														if (temp.getUserId() == Long.parseLong(sendTo[i].substring(sendTo[i].indexOf("senderId=") + 9))) {
															// out.writeUTF(
															// temp.getUsername() + ":" + sendTo[i].substring(sendTo[i].indexOf("message=") + 8, sendTo[i].lastIndexOf("&")));
															out.write(GZipUtils.compress(
																	temp.getUsername() + ":" + sendTo[i].substring(sendTo[i].indexOf("message=") + 8, sendTo[i].lastIndexOf("&"))));
														}
													}
													break;
												}
											}
										}
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			Thread t = new TcpServer(6003); // 端口号：1024~65535
			t.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
