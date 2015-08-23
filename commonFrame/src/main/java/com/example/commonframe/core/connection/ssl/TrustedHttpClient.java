package com.example.commonframe.core.connection.ssl;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.content.Context;

/**
 * @author Tyrael
 * @since January 2014
 * @version 1.0 <br>
 */
@SuppressWarnings("deprecation")
public class TrustedHttpClient extends DefaultHttpClient {

	private static final int HTTP_DEFAULT_PORT = 80;
	private static final String HTTP_SCHEME = "http";
	private static final int HTTP_DEFAULT_HTTPS_PORT = 443;
	private static final String HTTP_SSL_SCHEME = "https";
	private Context context;
	private int keyStoreId;
	private String password;

	public TrustedHttpClient(Context context, int keyStoreId, String password) {
		this.context = context;
		this.keyStoreId = keyStoreId;
		this.password = password;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme(HTTP_SCHEME, PlainSocketFactory
				.getSocketFactory(), HTTP_DEFAULT_PORT));
		// Register for port 443 our SSLSocketFactory with our keystore
		// to the ConnectionManager
		registry.register(new Scheme(HTTP_SSL_SCHEME,
				getTrustedSslSocketFactory(), HTTP_DEFAULT_HTTPS_PORT));

		return new ThreadSafeClientConnManager(getParams(), registry);
	}

	private SSLSocketFactory getTrustedSslSocketFactory() {
		try {
			// Get an instance of the Bouncy Castle KeyStore format
			KeyStore trusted = KeyStore.getInstance("BKS");
			// Get the raw resource, which contains the keystore with
			// your trusted certificates (root and any intermediate certs)
			InputStream in = context.getResources().openRawResource(keyStoreId);
			try {
				// Initialize the keystore with the provided trusted
				// certificates
				// Also provide the password of the keystore
				trusted.load(in, password.toCharArray());
			} finally {
				in.close();
			}
			// Pass the keystore to the SSLSocketFactory. The factory is
			// responsible
			// for the verification of the server certificate.
			SSLSocketFactory sf = new SSLSocketFactory(trusted);
			// Hostname verification from certificate
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			return sf;
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
