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
 * @author Professor Wergeles
 */
public class NYTNewsStory implements MediaObjectInterface {
    public String webUrl;
    public String headline;
    public String snippet;
    public String leadParagraph;
    public String newsDesk;
    public String sectionName;
    public String source;
    
    
    public NYTNewsStory(String webUrl, String headline, String snippet, 
            String leadParagraph, String newsDesk, String sectionName, String source ) {
        
        this.webUrl = webUrl;
        this.headline = headline;
        this.snippet = snippet;
        this.leadParagraph = leadParagraph;
        this.newsDesk = newsDesk;
        this.sectionName = sectionName;
    }
    
    public String toJsonString() {
        JSONObject obj=new JSONObject();
        if (this.headline != null) obj.put("Headline", this.headline);
        if (this.webUrl != null)  obj.put("URL", this.webUrl);

        return obj.toJSONString(); 
    }
    
    
    public void initFromJsonString(String jsonString) {
        this.webUrl = "";
        this.headline = "";
        this.snippet = "";
        this.leadParagraph = "";
        this.newsDesk = "";
        this.sectionName = "";
        
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

        
        webUrl = (String)jsonObj.getOrDefault("URL", "");
        headline = (String)jsonObj.getOrDefault("Headline", "");
        
    }
}
