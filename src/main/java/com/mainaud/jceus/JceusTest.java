/*
 * Copyright 2015 Jérôme Mainaud
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mainaud.jceus;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JceusTest {

    private static final String ALGO = "AES/ECB/PKCS5Padding";
    public static final String CLEAR_256 = "Secret 256";
    public static final String CLEAR_128 = "Secret 128";

    private static String KEY_128 = "G1Iu7JtbmQeqqsHzmmbtpg==";
    private static String KEY_256 = "RX8YfUNF2ORfv3n/jSzAnoUxx8XRa3bB7XMex5b43Qg=";

    private static String SECRET_128 = "duH3Rc6apxWfC6MaO3h7Rw==";
    private static String SECRET_256 = "4caltYEF9YIZJVGVEV03qA==";

    public static void main(String[] args) {
        Base64.Decoder decoder = Base64.getDecoder();

        SecretKey secretKey128 = new SecretKeySpec(decoder.decode(KEY_128), "AES");
        SecretKey secretKey256 = new SecretKeySpec(decoder.decode(KEY_256), "AES");

        boolean valid128 = false;
        boolean valid256 = false;
        boolean invalid256Key = false;

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGO);
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey128);
                byte[] clearText = cipher.doFinal(decoder.decode(SECRET_128));
                valid128 = CLEAR_128.equals(new String(clearText, StandardCharsets.UTF_8));

            } catch (Exception e) {
                System.err.println("Error while decrypting : " + e.getMessage());
            }
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey256);
                byte[] clearText = cipher.doFinal(decoder.decode(SECRET_256));
                valid256 = CLEAR_256.equals(new String(clearText, StandardCharsets.UTF_8));

            } catch (InvalidKeyException e) {
                invalid256Key = true;
            } catch (Exception e) {
                System.err.println("Error while decrypting : " + e.getMessage());
            }
            if (!valid128) {
                System.err.println("Le chiffrement ne fonctionne pas.");
            } else if (!valid256) {
                System.out.println("Le chiffrement fonctionne en 128 mais pas en 256.");
                if (invalid256Key) {
                    System.out.println("Java Cryptography Extension (JCE) Unlimited Strength n'est pas installé.");
                }
            } else {
                System.out.println("Le chiffrement fonctionne.");
                System.out.println("Java Cryptography Extension (JCE) Unlimited Strength est installé.");
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Le chiffrement ne fonctionne pas.");
        }

    }
}
