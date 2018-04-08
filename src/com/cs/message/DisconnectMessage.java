/**
 * 
 */
package com.cs.message;

/**
 * @author MJCoder
 *
 */
public class DisconnectMessage extends Message {

	private String message = "disconnect";

	/**
	 * @param sender
	 * @param message
	 */
	public DisconnectMessage(long sender, String message) {
		super(sender);
		this.message = "disconnect";
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	@Override
	public void setMessage(String message) {
		// TODO Auto-generated method stub
		message = "disconnect";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "message=" + message + "&" + super.toString();
	}
}
