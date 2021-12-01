package com.hitejinro.snop.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES 방식을 이용한 암복호화 클래스
 * @author ykw
 *
 */
public class CryptoAES {
    
    private static final String sKey = "e2b2ea1347d9637fbb3a57bf3685219b"; // - 암복호화에 사용될 키

    private final static Logger logger = LoggerFactory.getLogger(CryptoAES.class);
    
    /**
     * 암호화해서 반환
     * @param sVal
     * @return
     */
    public static String getEncrypted(String sVal) {
        String sEncryptedVal = "";
        try {
            Hex hex = new Hex();
            byte[] skeys = getSecretKeys();
            SecretKeySpec skeySpec = new SecretKeySpec(skeys, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sVal.getBytes());
            sEncryptedVal = new String(hex.encode(encrypted));
        } catch(Exception e) {
            sEncryptedVal = sVal;
            logger.info("Encrypted Error :: original value=" + sVal + " :: " + e.getMessage());
        }
        return sEncryptedVal;
    }
    
    /**
     * 복호화해서 반환
     * @param sVal
     * @return
     */
    public static String getDecrypted(String sVal) {
        String sDecryptedVal = "";
        try {
            Hex hex = new Hex();
            byte[] skeys = getSecretKeys();
            SecretKeySpec skeySpec = new SecretKeySpec(skeys, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(hex.decode(sVal.getBytes()));
            sDecryptedVal = new String(original);
        } catch(Exception e) {
            sDecryptedVal = sVal;
            logger.info("Decrypted Error :: original value=" + sVal + " :: " + e.getMessage());
        }
        return sDecryptedVal;
    }
    
    /**
     * 키를 암복호화에 사용할 상태로 변환
     * @return
     * @throws DecoderException 
     */
    private static byte[] getSecretKeys() throws DecoderException {
        Hex hex = new Hex();
        return hex.decode(sKey.getBytes());
    }
    
    /**
     * 새로운 키값을 생성할 경우 사용. 해당 키값을 sKey 에 담아서 사용하면 된다.
     * 단, 동일 키값으로 암호화된 것만 복호화가 된다.
     * 참고) 기 생성해 둔 키값이 아닌 새로운 키를 사용하려면, 이 펑션을 이용해서 새로 만들어서 사용하면 된다.
     * @return
     */
    public static String getNewKeyString() {
        String sKeyString = "";
        
        try {
            // 1. 128비트 비밀키 생성
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            SecretKey skey = kgen.generateKey();
            Hex hex = new Hex();
            sKeyString = new String(hex.encode(skey.getEncoded()));
        } catch(Exception e) {
            logger.info("AES Key generate Error :: " + e.getMessage());
        }
        return sKeyString;
    }
}
