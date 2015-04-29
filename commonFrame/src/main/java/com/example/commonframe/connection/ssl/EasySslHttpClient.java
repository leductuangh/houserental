package com.example.commonframe.connection.ssl;

import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

@SuppressWarnings("deprecation")
public class EasySslHttpClient extends DefaultHttpClient {
	private static final int HTTP_DEFAULT_PORT = 80;
	private static final String HTTP_SCHEME = "http";
	private static final int HTTP_DEFAULT_HTTPS_PORT = 443;
	private static final String HTTP_SSL_SCHEME = "https";
	private int mHttpsPort;

	public EasySslHttpClient() {
		mHttpsPort = HTTP_DEFAULT_HTTPS_PORT;
	}

	public EasySslHttpClient(int httpsPort) {
		mHttpsPort = httpsPort;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme(HTTP_SCHEME, PlainSocketFactory
				.getSocketFactory(), HTTP_DEFAULT_PORT));
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			registry.register(new Scheme(HTTP_SSL_SCHEME,
					new EasySslSocketFactory(trustStore), mHttpsPort));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ThreadSafeClientConnManager(getParams(), registry);
	}
}
