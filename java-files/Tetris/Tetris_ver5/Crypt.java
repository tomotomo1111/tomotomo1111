import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ENCRYPT_KEY = "yourEncryptKey01";
    private static final String INIT_VECTOR = "yourInitVector01";
    
    private final SecretKeySpec key = new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES");
    private final IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes());

    public Crypt() {

    }

    String encryptIP(String token) throws Exception {

        Cipher encrypter = Cipher.getInstance(ALGORITHM);
        encrypter.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
        byte[] byteToken = encrypter.doFinal(token.getBytes());

        // System.out.println("[ENCRYPT] : " + token + " -> " + new String(Base64.getEncoder().encode(byteToken)));
        return new String(Base64.getEncoder().encode(byteToken));
    }

    String decryptIP(String encryptedToken) throws Exception {

        Cipher decrypter = Cipher.getInstance(ALGORITHM);
        decrypter.init(Cipher.DECRYPT_MODE, this.key, this.iv);
        byte[] byteToken = Base64.getDecoder().decode(encryptedToken);
        
        // System.out.println("[DECRYPT] : " + encryptedToken + " -> " + new String(decrypter.doFinal(byteToken)));
        return new String(decrypter.doFinal(byteToken));
    }
}
