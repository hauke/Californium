package ch.ethz.inf.vs.californium.dtls;

import java.util.List;
import java.util.logging.Logger;

import ch.ethz.inf.vs.californium.util.DatagramWriter;

/**
 * A non-anonymous server can optionally request a certificate from the client,
 * if appropriate for the selected cipher suite. This message, if sent, will
 * immediately follow the {@link ServerKeyExchange} message (if it is sent;
 * otherwise, this message follows the server's {@link CertificateMessage}
 * message). See <a href="http://tools.ietf.org/html/rfc5246#section-7.4.4">RFC
 * 5246, 7.4.4. Certificate Request</a>.
 * 
 * @author Stefan Jucker
 * 
 */
public class CertificateRequest extends HandshakeMessage {

	// Logging ////////////////////////////////////////////////////////

	protected static final Logger LOG = Logger.getLogger(ClientHello.class.getName());

	// DTLS-specific constants ////////////////////////////////////////
	
	/* See http://tools.ietf.org/html/rfc5246#section-7.4.4 for message format. */

	private static final int CERTIFICATE_TYPES_LENGTH_BITS = 8;
	
	private static final int CERTIFICATE_TYPE_BITS = 8;

	private static final int SUPPORTED_SIGNATURE_LENGTH_BITS = 16;

	private static final int CERTIFICATE_AUTHORITIES_LENGTH_BITS = 16;

	// Members ////////////////////////////////////////////////////////

	/** A list of the types of certificate types that the client may offer. */
	private List<ClientCertificateType> certificateTypes;

	/**
	 * A list of the hash/signature algorithm pairs that the server is able to
	 * verify, listed in descending order of preference.
	 */
	private List<SignatureAndHashAlgorithm> supportedSignatureAlgorithms;

	/**
	 * A list of the distinguished names of acceptable certificate_authorities,
	 * represented in DER-encoded format.
	 */
	List<DistinguishedName> certificateAuthorities;

	// Constructors ///////////////////////////////////////////////////
	
	public CertificateRequest() {
		// TODO Auto-generated constructor stub
	}

	// Methods ////////////////////////////////////////////////////////

	@Override
	public HandshakeType getMessageType() {
		return HandshakeType.CERTIFICATE_REQUEST;
	}

	@Override
	public int getMessageLength() {
		// fixed: certificate type length field (1 byte) + supported signature
		// algorithms length field (2 bytes) + certificate authorities (2 bytes)
		// = 5
		return 5 + certificateTypes.size() + (supportedSignatureAlgorithms.size() * 2) + (certificateAuthorities.size() * 2);
	}

	// Serialization //////////////////////////////////////////////////

	@Override
	public byte[] toByteArray() {
		DatagramWriter writer = new DatagramWriter();
		writer.writeBytes(super.toByteArray());
		
		writer.write(certificateTypes.size(), CERTIFICATE_TYPES_LENGTH_BITS);
		for (ClientCertificateType certificateType : certificateTypes) {
			writer.write(certificateType.getCode(), CERTIFICATE_TYPE_BITS);
		}
		
		writer.write(supportedSignatureAlgorithms.size() * 2, SUPPORTED_SIGNATURE_LENGTH_BITS);
		for (SignatureAndHashAlgorithm signatureAndHashAlgorithm : supportedSignatureAlgorithms) {
			// TODO
		}
		
		// TODO
		
		return writer.toByteArray();
	}

	public static HandshakeMessage fromByteArray(byte[] byteArray) {
		// TODO Auto-generated method stub

		return null;

	}

	// /////////////////////////////////////////////////////////////////

	/**
	 * Certificate types that the client may offer. See <a
	 * href="http://tools.ietf.org/html/rfc5246#section-7.4.4">RFC 5246</a> for
	 * details.
	 * 
	 * @author Stefan Jucker
	 * 
	 */
	public enum ClientCertificateType {
		RSA_SIGN(1), DSS_SIGN(2), RSA_FIXED_DH(3), DSS_FIXED_DH(4), RSA_EPHEMERAL_DH_RESERVED(5), DSS_EPHEMERAL_DH_RESERVED(6), FORTEZZA_DMS_RESERVED(20), ECDSA_SIGN(64), RSA_FIXED_ECDH(65), ECDSA_FIXED_ECDH(66);

		private int code;

		private ClientCertificateType(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	/**
	 * See <a href="http://tools.ietf.org/html/rfc5246#appendix-A.4.1">RFC
	 * 5246</a> for details. Code is at most 255 (1 byte needed for
	 * representation).
	 * 
	 * @author Stefan Jucker
	 * 
	 */
	public enum HashAlgorithm {
		NONE(0), MD5(1), SHA1(2), SHA224(3), SHA256(4), SHA384(5), SHA512(6);

		private int code;

		private HashAlgorithm(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	}

	/**
	 * See <a href="http://tools.ietf.org/html/rfc5246#appendix-A.4.1">RFC
	 * 5246</a> for details. Code is at most 255 (1 byte needed for
	 * representation).
	 * 
	 * @author Stefan Jucker
	 * 
	 */
	public enum SignatureAlgorithm {
		ANONYMOUS(0), RSA(1), DSA(2), ECDSA(3);

		private int code;

		private SignatureAlgorithm(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	}

	/**
	 * See <a href="http://tools.ietf.org/html/rfc5246#appendix-A.4.1">RFC
	 * 5246</a> for details.
	 * 
	 * @author Stefan Jucker
	 * 
	 */
	public class SignatureAndHashAlgorithm {

		private HashAlgorithm hash;

		private SignatureAlgorithm signature;

		public SignatureAndHashAlgorithm(HashAlgorithm hashAlgorithm, SignatureAlgorithm signatureAlgorithm) {
			this.signature = signatureAlgorithm;
			this.hash = hashAlgorithm;
		}

		public SignatureAlgorithm getSignature() {
			return signature;
		}

		public void setSignature(SignatureAlgorithm signature) {
			this.signature = signature;
		}

		public HashAlgorithm getHash() {
			return hash;
		}

		public void setHash(HashAlgorithm hash) {
			this.hash = hash;
		}
	}

	public class DistinguishedName {
		byte[] name;

		public DistinguishedName(byte[] name) {
			this.name = name;
		}
	}

	public List<ClientCertificateType> getCertificateTypes() {
		return certificateTypes;
	}

	public List<SignatureAndHashAlgorithm> getSupportedSignatureAlgorithms() {
		return supportedSignatureAlgorithms;
	}

}
