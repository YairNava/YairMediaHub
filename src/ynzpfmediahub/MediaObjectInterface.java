/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ynzpfmediahub;

/**
 *
 * @author yairnavarrete
 */
public interface MediaObjectInterface {
    public String toJsonString();
    public void initFromJsonString(String jsonString);
    
}
