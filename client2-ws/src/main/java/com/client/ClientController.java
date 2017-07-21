package com.client;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;

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

            System.out.println("Data Received : " + data);
            String decrypted_msg = ac.decryptText(data, privateKey);
            System.out.println("Actual Data : " + decrypted_msg);
        } else {
            System.out.println("unauthorized");
        }
    }
}
