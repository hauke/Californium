package ch.ethz.inf.vs.californium.dtls;

import java.security.SecureRandom;

/**
 * A session identifier is a value generated by a server that identifies a
 * particular session.
 * 
 * @author Stefan Jucker
 */
public class SessionId {
	private byte[] sessionId; // opaque SessionID<0..32>;

	public SessionId() {
		sessionId = new Random(new SecureRandom()).getRandomBytes();
	}
	
	public SessionId(byte[] sessionId) {
		this.sessionId = sessionId;
	}
	
	public int length() {
		return sessionId.length;
	}
	
	public byte[] getSessionId() {
		return sessionId;
	}
}