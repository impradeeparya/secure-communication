package com.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

        RestTemplate restTemplate = new RestTemplate();
        switch (requestDto.getVendor()) {
            case CLIENT1:
                PublicKey publicKey = ac.getPublic("/home/pradeep/KeyPair/client1/publicKey");
                headers.add("Authorization", "Basic " + ac.encryptText(environment.getProperty(requestDto.getVendor() + ".token"), publicKey));
                headers.add("Content-Type", "application/json");
                HttpEntity<String> request = new HttpEntity<>(ac.encryptText(requestDto.getData(), publicKey), headers);
                restTemplate.postForObject(environment.getProperty(requestDto.getVendor() + ".url"), request, Boolean.class);
                break;
            case CLIENT2:
                publicKey = ac.getPublic("/home/pradeep/KeyPair/client2/publicKey");
                headers.add("Authorization", "Basic " + ac.encryptText(environment.getProperty(requestDto.getVendor() + ".token"), publicKey));
                headers.add("Content-Type", "application/json");
                request = new HttpEntity<>(ac.encryptText(requestDto.getData(), publicKey), headers);
                restTemplate.postForObject(environment.getProperty(requestDto.getVendor() + ".url"), request, Boolean.class);
                break;
        }

        System.out.println(requestDto);
    }
}
