/**
 * 
 */
package com.cs.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.cs.user.User;
import com.cs.util.GZipUtils;

/**
 * @author MJCoder
 *
 */
public class UdpServer {

	public static void udpSend(List<User> users) {
		String info = "";
		for (User user : users) {
			info += user.getUserId() + "=" + user.getUsername() + ";";
		}
		for (User user : users) {
			try {
				// 创建发送端的DatagramSocket对象。无需指定端口号（系统会分配一个空闲的端口号）。
				DatagramSocket datagramSocket = new DatagramSocket();
				System.out.println(user.getSocket().getInetAddress() + "：" + (user.getSocket().getPort() + 1));
				// 指定封装数据的字节数组、数据的大小和数据包的目标IP地址、端口号。用于发送端（在发送数据时必须指定接收端的IP地址和端口号）。
				// DatagramPacket datagramPacket = new DatagramPacket(info.getBytes(), info.getBytes().length, user.getSocket().getInetAddress(), user.getSocket().getPort() + 1);
				byte[] infoCompress = GZipUtils.compress(info);
				DatagramPacket datagramPacket = new DatagramPacket(infoCompress, infoCompress.length, user.getSocket().getInetAddress(), user.getSocket().getPort() + 1);
				datagramSocket.send(datagramPacket);
				// datagramSocket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
