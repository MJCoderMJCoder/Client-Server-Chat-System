/**
 * 
 */
package com.cs.user;

import java.net.Socket;

/**
 * @author MJCoder
 *
 */
public class User {
	private long userId;
	private String username;
	private Socket socket; // 存服务端的serverSocket.accept()

	/**
	 * @param userId
	 * @param username
	 * @param socket
	 */
	public User(long userId, String username, Socket socket) {
		this.userId = userId;
		this.username = username;
		this.socket = socket;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket
	 *            the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", socket=" + socket + "]";
	}
}
