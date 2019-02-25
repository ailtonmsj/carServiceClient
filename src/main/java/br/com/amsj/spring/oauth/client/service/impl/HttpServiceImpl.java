package br.com.amsj.spring.oauth.client.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.amsj.spring.oauth.client.errorHandler.CarClientResponseErrorHandler;
import br.com.amsj.spring.oauth.client.model.Client;
import br.com.amsj.spring.oauth.client.service.HttpService;

@Service
public class HttpServiceImpl implements HttpService{
	
	private final String TOKEN_URI = "http://localhost:9091/oauth/token";
	
	private final String REDIRECT_CLIENT_URI = "http://localhost:9090/authCode";
	
	private RestTemplate restTemplate;
	
	@Value("${resource-server.url}")
	private String RESOURCE_SERVER_URL;
	
	@Value("${application.oauth.configuration.client-id}:${application.oauth.configuration.client-secret}")
	private String credential;
	
    private String encodeCredential;
    
    @PostConstruct
    private void encodeCredencial() {
    	this.encodeCredential = "Basic " + Base64.getEncoder().encodeToString(credential.getBytes());
    }
	
	public HttpServiceImpl(RestTemplateBuilder restTemplateBuilder, CarClientResponseErrorHandler carClientResponseErrorHandler) {
		
		this.restTemplate = restTemplateBuilder.errorHandler(carClientResponseErrorHandler).build();
	}

    public ResponseEntity<String> getToken(String authCode) throws IOException, JSONException {
        
    	HttpHeaders headers = new HttpHeaders();
    	headers.add(HttpHeaders.AUTHORIZATION, encodeCredential);
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	
    	MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("redirect_uri", REDIRECT_CLIENT_URI);
        map.add("code", authCode);
        map.add("grant_type", "authorization_code");
    	
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(TOKEN_URI, HttpMethod.POST, httpEntity, String.class);
        	
        return responseEntity;
    }

    public ResponseEntity<Client[]> getResource(String token) throws IOException, JSONException {
    	
    	final String URL = RESOURCE_SERVER_URL + "/client/list";
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.add(HttpHeaders.AUTHORIZATION, "bearer " + token);
    	
    	HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(null, headers);
    	
		ResponseEntity<Client[]> responseEntity = this.restTemplate.exchange(URL, HttpMethod.GET, httpEntity, Client[].class);
		
		return responseEntity;
    }

	@Override
	public String getPublicCarAmount() throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(RESOURCE_SERVER_URL + "/public/car/amount");

        CloseableHttpResponse response = client.execute(httpGet);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
	}

	@Override
	public String getPublicClientAmount() throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(RESOURCE_SERVER_URL + "/public/client/amount");

        CloseableHttpResponse response = client.execute(httpGet);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
	}

}
