/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ynzpfmediahub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javafx.stage.FileChooser;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * code from:https://dev.to/awwsmm/how-to-encrypt-a-password-in-java-42dh
 * @author yairnavarrete
 */
public class LoginManager extends MediaHubAbstractModel implements java.io.Serializable {
    
    
  private static final SecureRandom RAND = new SecureRandom();
  private static final int ITERATIONS = 65536;
  private static final int KEY_LENGTH = 512;
  private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
  
  private String userName;
  private String key;
  
  
  
  public static Optional<String> generateSalt (final int length) {
      
      if (length < 1) {
          System.err.println("error in generateSalt: length must be > 0");
          return Optional.empty();
      }
      
      byte[] salt = new byte[length];
      RAND.nextBytes(salt);
      
      return Optional.of(Base64.getEncoder().encodeToString(salt));
  }
  
  

  public static Optional<String> hashPassword (String password, String salt) {

    char[] chars = password.toCharArray();
    byte[] bytes = salt.getBytes();

    PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

    Arrays.fill(chars, Character.MIN_VALUE);

    try {
      SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
      byte[] securePassword = fac.generateSecret(spec).getEncoded();
      return Optional.of(Base64.getEncoder().encodeToString(securePassword));

    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
      System.err.println("Exception encountered in hashPassword()");
      return Optional.empty();

    } finally {
      spec.clearPassword();
    }
  }
  
  
  
    public static boolean verifyPassword (String password, String key, String salt) {
        Optional<String> optEncrypted = hashPassword(password, salt);
        if (!optEncrypted.isPresent()) return false;
        return optEncrypted.get().equals(key);
      }
    
    
    
    
    public String toJsonString(String userName, String key) {
        JSONObject obj=new JSONObject();
        if (userName != null) obj.put("UserName", userName);
        if (key != null)  obj.put("Key", key);


        return obj.toJSONString(); 
    }
    
    public void initFromJsonString(String jsonString) {
        userName = "";
        key = "";

        
        if (jsonString == null || jsonString == "") return;
        
        JSONObject jsonObj;
        try {
            jsonObj = (JSONObject)JSONValue.parse(jsonString);
        } catch (Exception ex) {
            return;
        }
        
        if (jsonObj == null) {
            return;
        }

        
        userName = (String)jsonObj.getOrDefault("User", "");
        key = (String)jsonObj.getOrDefault("Key", "");
        
        
//        // get on a number will be a long
//        Object ageObj = jsonObj.getOrDefault("age", null);
//        
//        if (ageObj != null) {
//            if (ageObj instanceof Long) {
//                Long longAge = (Long)ageObj;
//                age = new Integer(longAge.intValue());
//            } else {
//                age = null;
//            } 
//        }



    
}
    


}