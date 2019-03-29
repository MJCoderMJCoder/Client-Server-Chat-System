/**
 * 
 */
package com.cs.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import com.cs.GUI.ChatUI;
import com.cs.util.GZipUtils;

/**
 * @author MJCoder
 *
 */
public class UdpClient {

	public static void udpReceive(int port) {
		System.out.println(port);
		try {
			// 创建接收端的DatagramSocket对象时，必须指定一个端口号（监听指定的端口）。
			DatagramSocket datagramSocket = new DatagramSocket(port);
			new Thread() {
				public void run() {
					while (true) {
						try {
							// 指定封装数据的字节数组和数据的大小。用于接收端（接收端不需要明确知道数据的来源，只需要接收到数据即可）。
							byte[] buf = new byte[9999990];
							DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
							datagramSocket.receive(datagramPacket); // 从数据报中取出数据
							// String onlineUsers = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
							String onlineUsers = new String(GZipUtils.uncompress(datagramPacket.getData()));
							ChatUI.updateOnline(onlineUsers);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			}.start();
			// datagramSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
