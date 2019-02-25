package br.com.amsj.spring.oauth.client.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.amsj.spring.oauth.client.model.Client;
import br.com.amsj.spring.oauth.client.service.HttpService;

@Controller
public class HttpController {

	private String authCode;
	private String token;
	
	@Autowired
	private HttpService httpService;
	
	@RequestMapping("/authCode")
    public String showAuthCode(@RequestParam("code") String authCode, Model model) {
        this.authCode = authCode;

        System.out.println(authCode);

        model.addAttribute("authCode", authCode);

        return "index";
    }
	
	@RequestMapping("/token")
    public String showAccessToken(Model model) {

        model.addAttribute("token", token);

        return "index";
    }
	
	@RequestMapping("/getAuthCode")
    public String redirect() {
		
		System.out.println("--> getAuthCode");
        
		return "redirect:http://localhost:9091/oauth/authorize?response_type=code&client_id=carServiceClient&redirect_uri=http://localhost:9090/authCode";
    }
	
	@RequestMapping("/getToken")
    public String getToken(Model model) throws IOException {
        
		 ResponseEntity<String> responseEntity = httpService.getToken(this.authCode);
		 
		 if(responseEntity.getStatusCode() == HttpStatus.OK) {
			 JSONObject jsonObject = new JSONObject(responseEntity.getBody().toString());
			 System.out.println("--> jsonObject: " + jsonObject.getString("access_token"));
			 this.token = (String) jsonObject.get("access_token");
		 }else {
			 this.token = responseEntity.getStatusCode().toString();
		 }

        model.addAttribute("authCode", "Auth Code Expired");
        model.addAttribute("token", token);

        return "index";
    }
	
	@RequestMapping("/getResource")
    public String getResource(Model model) throws IOException {
        List<Client> clientList = httpService.getResource(this.token);

        model.addAttribute("authCode", "Auth Code Expired");
        model.addAttribute("token", token);
        model.addAttribute("clientList", clientList);

        return "index";
    }
	
	@RequestMapping("/getPublicResource/carAmount")
    public String getPublicResourceCarAmount(Model model) throws IOException {
        String carAmount = httpService.getPublicCarAmount();
        model.addAttribute("carAmount", carAmount);

        return "index";
    }
	
	@RequestMapping("/getPublicResource/clientAmount")
    public String getPublicResourceClientAmount(Model model) throws IOException {
        String clientAmount = httpService.getPublicClientAmount();
        model.addAttribute("clientAmount", clientAmount);

        return "index";
    }
}
