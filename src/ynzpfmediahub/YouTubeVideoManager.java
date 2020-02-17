/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ynzpfmediahub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;







/**
 *https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=YOURKEYWORD&type=video&key=YOURAPIKEY
 * YouTubeAPI key = AIzaSyAwa9vMqJXNH95FDBEiAMsCDvbKhRdFmyY
 * @author yairnavarrete
 */
public class YouTubeVideoManager {
    private String urlString = "";
   
    // NOTE!!  The api key below is Nick Wergeles' api key.  If you build an app that uses the New York Times API
    // get your own api key!!!!!  Get it from: http://developer.nytimes.com
    // I also cannot guarantee that the api key provided will be valid in the future.
    private final String baseUrlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=";
    private final String apiKey = "AIzaSyBhe_rkE86eQNv9OrHjB5QiKgjZj74RmL4";
    private String searchString = "Bernie Sanders";
    
    private URL url;
    private ArrayList<YouTubeVideo> videos;
    
    
    public YouTubeVideoManager() {
        videos = new ArrayList<>();
    }
    
    public void load(String searchString) throws Exception {
        if (searchString == null || searchString.equals("")) {
            throw new Exception("The search string was empty.");
        }
        
        this.searchString = searchString;
        
        String encodedSearchString; 
        
        try {
            encodedSearchString = URLEncoder.encode(searchString, "UTF-8"); 
        } catch(UnsupportedEncodingException ex){
            throw ex; 
        }
        
        urlString = baseUrlString + encodedSearchString + "&type=video&key=" + apiKey;
        
        System.out.println("urlString: " + urlString);
        
        try {
            url = new URL(urlString); 
        } catch(MalformedURLException muex){
            throw muex; 
        }
        
        String jsonString = ""; 
        
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream())); 
            
            String inputLine; 
            
            while((inputLine = in.readLine()) != null){
                jsonString += inputLine; 
            }
            
            in.close();
            
        } catch(IOException ioex){
            throw ioex; 
        }
//        System.out.println("Video JSON String:");
//        System.out.print("jsonString: " + jsonString);
        
        try {
            parseJsonVideoFeed(jsonString); 
        } catch (Exception ex){
            throw ex; 
        }
    }
    
    private void parseJsonVideoFeed(String jsonString) throws Exception {
        
        videos.clear();
        
        if (jsonString == null || jsonString == "") return;
        
        JSONObject jsonObj;
        try {
            jsonObj = (JSONObject)JSONValue.parse(jsonString);
        } catch (Exception ex) {
            throw ex;
        }
        
        if (jsonObj == null) return;
        
        
        JSONArray items;
        try {
            items = (JSONArray)jsonObj.get("items");
        } catch (Exception ex) {
            throw ex;
        }
      
        for (Object item : items) {
            try {
                JSONObject video = (JSONObject)item;
                JSONObject videoIDInfo = (JSONObject)video.getOrDefault("id", "");
                String videoID = (String) videoIDInfo.getOrDefault("videoId","");
                JSONObject videoSnippetInfo = (JSONObject)video.getOrDefault("snippet", "");
                String videoTitle = (String) videoSnippetInfo.getOrDefault("title", "");
                
           
                YouTubeVideo videoResource = new YouTubeVideo(videoID,videoTitle);
                videos.add(videoResource);
                
            } catch (Exception ex) {
                throw ex;
            }
            
        }
        
    }
    
    public ArrayList<YouTubeVideo> getYouTubeVideos() {
        return videos;
    }
    
    public int getNumYouTubeVideos() {        
        return videos.size();
    }   
    
    public void addYouTubeVideo(YouTubeVideo video){
        videos.add(video);
    }
 
}   

