package com.client;

import org.springframework.beans.factory.annotation.Autowired;
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
    private AsymmetricCryptography ac;

    @PostMapping(value = "/sfh")
    public void sfh(@RequestBody String data) throws Exception {

        System.out.println("CLIENT2");
        System.out.println("Data Received : " + data);
        PrivateKey privateKey = ac.getPrivate("/home/pradeep/KeyPair/client2/privateKey");
        String decrypted_msg = ac.decryptText(data, privateKey);
        System.out.println("Actual Data : " + decrypted_msg);
    }
}
