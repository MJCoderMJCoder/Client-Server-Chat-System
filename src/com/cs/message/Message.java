/**
 * 
 */
package com.cs.message;

/**
 * @author MJCoder
 *
 */
public abstract class Message {
	private long senderId; // 寄件人,发送人

	/**
	 * @param senderId
	 */
	public Message(long senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return the senderId
	 */
	public long getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId
	 *            the senderId to set
	 */
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return the message
	 */
	public abstract String getMessage();

	/**
	 * @param message
	 *            the message to set
	 */
	public abstract void setMessage(String message);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "senderId=" + senderId;
	}

}
