/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ynzpfmediahub;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author yairnavarrete
 */
public class YouTubeVideo implements MediaObjectInterface{
    public String videoID;
    public String videoTitle;
    public String videoURL;
    
    public YouTubeVideo(String videoID, String videoTitle) {
        
        this.videoID = videoID;
        this.videoTitle = videoTitle;
        this.videoURL = "http://www.youtube.com/embed/" + videoID + "?autoplay=1";
    }
    
    public String toJsonString() {
        JSONObject obj=new JSONObject();
        if (videoURL != null) obj.put("URL", this.videoURL);
        if (videoTitle != null)  obj.put("Title", this.videoTitle);

        return obj.toJSONString(); 
    }
    
    public void initFromJsonString(String jsonString) {
        this.videoTitle = "";
        this.videoURL = "";
        this.videoID = "";
        
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

        
        this.videoTitle = (String)jsonObj.getOrDefault("Title", "");
        this.videoURL = (String)jsonObj.getOrDefault("URL", "");
        
        

    }
}
