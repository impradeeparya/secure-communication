package com.secure.keys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

/**
 * Created by pradeep on 21/7/17.
 */
public class KeyGenerator {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeyGenerator(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public static void main(String[] args) {
        KeyGenerator gk;
        try {
            gk = new KeyGenerator(1024);
            gk.createKeys();
            gk.writeToFile("/home/pradeep/KeyPair/server/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("/home/pradeep/KeyPair/server/privateKey", gk.getPrivateKey().getEncoded());

            gk.createKeys();
            gk.writeToFile("/home/pradeep/KeyPair/client1/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("/home/pradeep/KeyPair/client1/privateKey", gk.getPrivateKey().getEncoded());

            gk.createKeys();
            gk.writeToFile("/home/pradeep/KeyPair/client2/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("/home/pradeep/KeyPair/client2/privateKey", gk.getPrivateKey().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}
