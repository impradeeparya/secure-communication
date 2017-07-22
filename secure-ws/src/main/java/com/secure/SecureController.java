package com.secure;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by pradeep on 21/7/17.
 */

@RestController
public class SecureController {

    @Autowired
    private Environment environment;

    @Autowired
    private AsymmetricCryptography ac;

    @RequestMapping(value = "/encrypt")
    public void encryptData(@RequestBody RequestDto requestDto) throws Exception {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        switch (requestDto.getVendor()) {
            case CLIENT1:
                PublicKey publicKey = ac.getPublic("/home/pradeep/KeyPair/client1/publicKey");
                headers.add("Authorization", "Basic " + ac.encryptText(environment.getProperty(requestDto.getVendor() + ".token"), publicKey));
                headers.add("Content-Type", "application/json");
                HttpEntity<String> request = new HttpEntity<>(ac.encryptText(requestDto.getData(), publicKey), headers);
                restTemplate.postForEntity(environment.getProperty(requestDto.getVendor() + ".url"), request, Boolean.class);
                break;
            case CLIENT2:
                publicKey = ac.getPublic("/home/pradeep/KeyPair/client2/publicKey");
                headers.add("Authorization", "Basic " + ac.encryptText(environment.getProperty(requestDto.getVendor() + ".token"), publicKey));
                headers.add("Content-Type", "application/json");
                request = new HttpEntity<>(ac.encryptText(requestDto.getData(), publicKey), headers);
                restTemplate.postForEntity(environment.getProperty(requestDto.getVendor() + ".url"), request, Boolean.class);
                break;
        }

        System.out.println(requestDto);
    }

    @RequestMapping(value = "/sfh")
    public void decryptData(HttpServletRequest httpServletRequest, @RequestBody String data) throws Exception {

        PrivateKey privateKey = ac.getPrivate("/home/pradeep/KeyPair/server/privateKey");

        String authorization = httpServletRequest.getHeader("Authorization");
        System.out.println("Authorization : " + authorization);
        String authToken = ac.decryptText(authorization.split(" ")[1], privateKey);

        String sfhToken = environment.getProperty(authToken);
        System.out.println("Token received : " + authToken);

        if (Vendor.CLIENT1.toString().equals(sfhToken)) {
            System.out.println("Received response for CLIENT1");
            System.out.println("Data received : " + ac.decryptText(data, privateKey));
        } else if (Vendor.CLIENT2.toString().equals(sfhToken)) {
            System.out.println("Received response for CLIENT2");
            System.out.println("Data received : " + ac.decryptText(data, privateKey));
        } else {
            System.out.println("Unknown client");
        }


    }
}
