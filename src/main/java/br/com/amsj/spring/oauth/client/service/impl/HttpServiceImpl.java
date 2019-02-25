package br.com.amsj.spring.oauth.client.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.amsj.spring.oauth.client.errorHandler.CarClientResponseErrorHandler;
import br.com.amsj.spring.oauth.client.model.Client;
import br.com.amsj.spring.oauth.client.service.HttpService;

@Service
public class HttpServiceImpl implements HttpService{
	
	private final String TOKEN_URI = "http://localhost:9091/oauth/token";
	
	private final String REDIRECT_CLIENT_URI = "http://localhost:9090/authCode";
	
	private RestTemplate restTemplate;
	
	// CLIENT-ID AND CLIENT-SECRET 
    String credential = "Basic " + Base64.getEncoder().encodeToString("carServiceClient:clientSecret".getBytes());
	
	public HttpServiceImpl(RestTemplateBuilder restTemplateBuilder, CarClientResponseErrorHandler carClientResponseErrorHandler) {
		
		this.restTemplate = restTemplateBuilder.errorHandler(carClientResponseErrorHandler).build();
	}

    public ResponseEntity<String> getToken(String authCode) throws IOException, JSONException {
        
    	HttpHeaders headers = new HttpHeaders();
    	headers.add(HttpHeaders.AUTHORIZATION, credential);
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	
    	MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("redirect_uri", REDIRECT_CLIENT_URI);
        map.add("code", authCode);
        map.add("grant_type", "authorization_code");
    	
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        
        ResponseEntity<String> responseEntity = null;
       	responseEntity = restTemplate.exchange(TOKEN_URI, HttpMethod.POST, request, String.class);
        	
        return responseEntity;
    }

    public List<Client> getResource(String token) throws IOException, JSONException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:9092/client/list");
        httpGet.setHeader("Authorization", "bearer "+token);
        CloseableHttpResponse response = client.execute(httpGet);

        return responseParser(response);

    }


    public List<Client> responseParser(CloseableHttpResponse response) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        List<Client> clientist = new ArrayList<>();

        // TODO AMSJ - NEED AJUSTMENTS FOR TOKEN PROBLEM
        JSONArray jsonarray = new JSONArray(result.toString());

        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            Client client = mapper.readValue(jsonobject.toString(), Client.class);
            clientist.add(client);
        }

        return clientist;

    }


	@Override
	public String getPublicCarAmount() throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:9092/public/car/amount");

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

        HttpGet httpGet = new HttpGet("http://localhost:9092/public/client/amount");

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
