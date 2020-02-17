/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ynzpfmediahub;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author yairnavarrete
 */
public class LoginController extends Switchable implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    
    private String checkUser;
    private String checkPass;
    
    private String user = "user";
    private String salt = LoginManager.generateSalt(512).get();
    private String key = "J7TV7fiVRHJp/dWGasrQC+/fD2uMYfguY5k6R3k+uFVwp44eA4hS4FJH6qY+J2u+dYxh5ZiUI+mrQIewOT92RA==";
    @FXML
    private TextField errorDisplayTextField;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleGoToHome(ActionEvent event) {
        
        
        this.key = LoginManager.hashPassword("pass", this.salt).get();

        
        checkUser = username.getText().toString();
        checkPass = password.getText().toString();
        
        
        
        if(checkUser.equals(user) && LoginManager.verifyPassword(checkPass, key, this.salt)){
                errorDisplayTextField.setText("Acess Granted!");
                Switchable.switchTo("Home");        
        
        
        
        }
        else{
                errorDisplayTextField.setText("Incorrect username or password. Please Try Again!");


        }


}
    
}
