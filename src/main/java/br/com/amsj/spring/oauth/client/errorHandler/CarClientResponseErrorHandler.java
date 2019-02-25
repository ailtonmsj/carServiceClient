package br.com.amsj.spring.oauth.client.errorHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class CarClientResponseErrorHandler implements ResponseErrorHandler {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<HttpStatus> acceptableStatus;
	
	public CarClientResponseErrorHandler(@Value("${acceptableErrorStatus}") String acceptableErrorStatus) {
		
		acceptableStatus = 
				Arrays.stream(acceptableErrorStatus.split(","))
					.map(HttpStatus::valueOf)
					.collect(Collectors.toList());
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return acceptableStatus.contains(response.getStatusCode());
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		log.error("--> handleError: {} {}", response.getStatusCode(), response.getStatusText());
	}

}
