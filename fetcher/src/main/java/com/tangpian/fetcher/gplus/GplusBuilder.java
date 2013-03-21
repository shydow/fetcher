package com.tangpian.fetcher.gplus;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;

import com.google.api.client.auth.security.PrivateKeys;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;

public class GplusBuilder {

	private static final String privateKeyPath = "/com/tangpian/fetcher/gplus/privatekey.p12";
	// private static final String privateKeyPath = "/privatekey.p12";
	private static final String privateKeyType = "PKCS12";
	private static final String serviceAccountEmail = "1007895513614@developer.gserviceaccount.com";

	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static JsonFactory JSON_FACTORY = new JacksonFactory();
	private static Plus plus;

	public static Plus getServicePlus() {

		if (null == plus) {
			InputStream inputStream = Plus.class
					.getResourceAsStream(privateKeyPath);
			PrivateKey key;
			try {
				key = PrivateKeys.loadFromKeyStore(
						KeyStore.getInstance(privateKeyType), inputStream,
						"notasecret", "privatekey", "notasecret");
				GoogleCredential credential = new GoogleCredential.Builder()
						.setTransport(HTTP_TRANSPORT)
						.setJsonFactory(JSON_FACTORY)
						.setServiceAccountId(serviceAccountEmail)
						.setServiceAccountScopes(PlusScopes.PLUS_ME)
						.setServiceAccountPrivateKey(key).build();

				plus = new Plus.Builder(HTTP_TRANSPORT, JSON_FACTORY,
						credential).setApplicationName("gplustrend").build();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return plus;
	}

}
