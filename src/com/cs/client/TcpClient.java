/**
 * 
 */
package com.cs.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author MJCoder
 *
 */
public class TcpClient {
	private static Socket client;

	/**
	 * @return the client
	 */
	public static Socket getClient() {
		return client;
	}

	public static void login(long userId, String username) {
		try {
			client = new Socket("localhost", 6003);
			System.out.println("client：" + client);
			// System.out.println("远程主机地址：" + client.getRemoteSocketAddress());

			UdpClient.udpReceive(client.getLocalPort() + 1);

			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF("userId=" + userId + "&username=" + username);
			// InputStream inFromServer = client.getInputStream();
			// DataInputStream in = new DataInputStream(inFromServer);
			// System.out.println("服务器响应： " + in.readUTF());
			// client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
