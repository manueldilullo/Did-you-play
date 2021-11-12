package it.uniroma2.pjdm.didyouplayed.entity;

public class OutOfRangeException extends Exception {
	private static final long serialVersionUID = 1L;

	public OutOfRangeException() {
		super("Value out of range!");
	}

	public OutOfRangeException(String message) {
		super("Value out of range! " + message);
	}
}
