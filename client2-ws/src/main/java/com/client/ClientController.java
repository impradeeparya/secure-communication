package com.client;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by pradeep on 21/7/17.
 */

@RestController
public class ClientController {

    @Autowired
    private Environment environment;

    @Autowired
    private AsymmetricCryptography ac;

    @PostMapping(value = "/sfh")
    public void sfh(HttpServletRequest httpServletRequest, @RequestBody String data) throws Exception {
        PrivateKey privateKey = ac.getPrivate("/home/pradeep/KeyPair/client2/privateKey");

        String sfhToken = environment.getProperty("sfhToken");

        String authorization = httpServletRequest.getHeader("Authorization");
        System.out.println("CLIENT2");
        System.out.println("Authorization : " + authorization);
        String authToken = ac.decryptText(authorization.split(" ")[1], privateKey);
        System.out.println("Token received : " + authToken);
        if (sfhToken.equals(authToken)) {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            AsyncRestTemplate restTemplate = new AsyncRestTemplate();

            System.out.println("Data Received : " + data);
            String decrypted_msg = ac.decryptText(data, privateKey);
            System.out.println("Actual Data : " + decrypted_msg);

            PublicKey publicKey = ac.getPublic("/home/pradeep/KeyPair/server/publicKey");
            headers.add("Authorization", "Basic " + ac.encryptText(authToken, publicKey));
            headers.add("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(ac.encryptText("clientTwoData", publicKey), headers);
            restTemplate.postForEntity(environment.getProperty("sfhUrl"), request, Boolean.class);

        } else {
            System.out.println("unauthorized");
        }
    }
}
