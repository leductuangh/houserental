package com.example.commonframe.connection.ssl;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import com.example.commonframe.util.CentralApplication;

/**
 * @author Tyrael
 * @since January 2014
 * @version 1.0 <br>
 */
@SuppressWarnings("deprecation")
public class SslHttpClient extends DefaultHttpClient {
	private static final int HTTP_DEFAULT_PORT = 80;
	private static final String HTTP_SCHEME = "http";
	private static final int HTTP_DEFAULT_HTTPS_PORT = 443;
	private static final String HTTP_SSL_SCHEME = "https";

	private int mKeyStoreId;
	private String mKeyStoreType;
	private String mKeyStorePassword;
	private int mHttpsPort;

	public SslHttpClient(int keyStoreId, String keyStorePassword,
			String keyStoreType) {
		mKeyStoreId = keyStoreId;
		mKeyStorePassword = keyStorePassword;
		mKeyStoreType = keyStoreType;
		mHttpsPort = HTTP_DEFAULT_HTTPS_PORT;
	}

	public SslHttpClient(int keyStoreId, String keyStorePassword,
			String keyStoreType, int httpsPort) {
		mKeyStoreId = keyStoreId;
		mKeyStorePassword = keyStorePassword;
		mKeyStoreType = keyStoreType;
		mHttpsPort = httpsPort;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme(HTTP_SCHEME, PlainSocketFactory
				.getSocketFactory(), HTTP_DEFAULT_PORT));
		// Register for port 443 our SSLSocketFactory with our keystore
		// to the ConnectionManager
		registry.register(new Scheme(HTTP_SSL_SCHEME,
				getTrustedSslSocketFactory(), mHttpsPort));

		return new ThreadSafeClientConnManager(getParams(), registry);
	}

	public void setHttpsPort(int httpsPort) {
		mHttpsPort = httpsPort;
	}

	private SSLSocketFactory getTrustedSslSocketFactory() {
		try {
			// Get an instance of the Bouncy Castle KeyStore format
			KeyStore trusted = KeyStore.getInstance(mKeyStoreType);
			// Get the raw resource, which contains the keystore with
			// your trusted certificates (root and any intermediate certs)
			InputStream in = CentralApplication.getContext().getResources()
					.openRawResource(mKeyStoreId);
			try {
				// Initialize the keystore with the provided trusted
				// certificates
				// Also provide the password of the keystore
				trusted.load(in, mKeyStorePassword.toCharArray());
			} finally {
				in.close();
			}
			// Pass the keystore to the SSLSocketFactory. The factory is
			// responsible
			// for the verification of the server certificate.
			SSLSocketFactory sf = new SSLSocketFactory(trusted);
			// Hostname verification from certificate
			// sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			return sf;
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
