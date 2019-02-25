package br.com.amsj.spring.oauth.client.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import br.com.amsj.spring.oauth.client.model.Client;


public interface HttpService {
	
	ResponseEntity<String> getToken(String authCode) throws IOException, JSONException;

    List<Client> getResource(String token) throws IOException, JSONException;

    List<Client> responseParser(CloseableHttpResponse response) throws IOException, JSONException;

	String getPublicCarAmount() throws ClientProtocolException, IOException;

	String getPublicClientAmount() throws ClientProtocolException, IOException;
}

