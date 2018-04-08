/**
 * 
 */
package com.cs.message;

/**
 * @author MJCoder
 *
 */
public class MessageTo extends Message {
	private String message;
	private long userId; // 收件人

	/**
	 * @param senderId
	 * @param message
	 * @param userId
	 */
	public MessageTo(long senderId, String message, long userId) {
		super(senderId);
		this.message = message;
		this.userId = userId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "userId=" + userId + "&message=" + message + "&" + super.toString();
	}
}
