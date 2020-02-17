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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * FXML Controller class
 *
 * @author yairnavarrete
 */
public class HomeController extends Switchable implements Initializable, java.io.Serializable {
    
    
    private Stage stage;
    
    private NYTNewsManager newsManager;
    ArrayList<NYTNewsStory> stories;
    
    private WebEngine webEngineNews;
    ObservableList<String> newsListItems;
    private NYTNewsStory selectedArticle;
    
    
    private YouTubeVideoManager videoManager;
    ArrayList<YouTubeVideo> videos;
    
    private WebEngine webEngineVideo;
    ObservableList<String> videoListItems;    
    private YouTubeVideo selectedVideo;
    
    private YouTubeVideoManager videoQue;
    private NYTNewsManager newsQue;
    
    private String searchString = "BernieSanders";
    
    private Integer numberOfSavedStories;
    private Integer numberOfSavedVideos;

    
    @FXML
    private ListView newsListView;
    @FXML
    private ListView videoListView;
    @FXML
    private WebView videoWebView;
    @FXML
    private TextField searchTextField;
    @FXML
    private WebView newsWebView;
    @FXML
    private RadioButton nytRadioButton;
    @FXML
    private RadioButton newsApiRadioButton;
    @FXML
    private TextArea savedTextArea;
    
    

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.stage = (Stage) Switchable.stage;
        this.newsQue = new NYTNewsManager();
        this.videoQue = new YouTubeVideoManager();

    }    


    @FXML
    private void handleSearch(ActionEvent event) {
        
        displayArticles();
        displayVideos();
        
        
    }
    

    
    
    @FXML
    public void handleAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("MediaHub version 1.0");
        alert.setContentText("This application was developed by Yair Navarrete for CS3330 final project at the University of Missouri.");
        
        TextArea textArea = new TextArea("This application uses http get requests to display media on the UI");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    
        
   private void loadVideos() {
        try {
          videoManager.load(searchString); 
        } catch(Exception ex){
            displayExceptionAlert(ex); 
            return; 
        }
        
        videos = videoManager.getYouTubeVideos(); 
        videoListItems.clear(); 

        
        for(YouTubeVideo video : videos){
            videoListItems.add(video.videoTitle); 
        }
        
        videoListView.setItems(videoListItems);
        
        if(videos.size() > 0){
            videoListView.getSelectionModel().select(0);
            videoListView.getFocusModel().focus(0); 
            videoListView.scrollTo(0); 
        }
    }
       
   
   
   
    private void loadNews() {
        try {
          newsManager.load(searchString); 
        } catch(Exception ex){
            displayExceptionAlert(ex); 
            return; 
        }
        
        stories = newsManager.getNewsStories(); 
        newsListItems.clear(); 
        
        for(NYTNewsStory story : stories){
            newsListItems.add(story.headline); 
        }
        
        newsListView.setItems(newsListItems);
        
        if(stories.size() > 0){
            newsListView.getSelectionModel().select(0);
            newsListView.getFocusModel().focus(0); 
            newsListView.scrollTo(0); 
        }
    }
    
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
    
    
    private void displayExceptionAlert(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("An Exception Occurred!");
        alert.setContentText("An exception occurred.  View the exception information below by clicking Show Details.");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    
    
    
    private void displayVideos(){
        //this.stage = (Stage) Switchable.stage;
                
        webEngineVideo = videoWebView.getEngine();
        videoManager = new YouTubeVideoManager();
        videoListItems = FXCollections.observableArrayList();
        
        
        videoListView.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                    if((int) new_val < 0 || (int) new_val > (videos.size() - 1)){
                        return; 
                    }
                    
                    selectedVideo = videos.get((int) new_val); 
                    webEngineVideo.load(selectedVideo.videoURL);    
                }
        ); 

        if (searchTextField.getText().equals("")) {
            displayErrorAlert("The search field cannot be blank. Enter one or more search words.");
            return;
        }
        
        
        searchString = searchTextField.getText();
        loadVideos();
        
    }
    
    
    
    
    
    private void displayArticles(){
        
        //this.stage = (Stage) Switchable.stage;
                
        webEngineNews = newsWebView.getEngine();
        newsManager = new NYTNewsManager();
        newsListItems = FXCollections.observableArrayList();
        
        
        newsListView.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                    if((int) new_val < 0 || (int) new_val > (stories.size() - 1)){
                        return; 
                    }
                    
                    selectedArticle = stories.get((int) new_val); 
                    webEngineNews.load(selectedArticle.webUrl);    
                }
        ); 
        

        if (searchTextField.getText().equals("")) {
            displayErrorAlert("The search field cannot be blank. Enter one or more search words.");
            return;
        }
        
        
        searchString = searchTextField.getText();
        loadNews();
        
        

    }

    @FXML
    private void handleSaveVideo(ActionEvent event) {
        if (selectedVideo.videoURL == null) {
            return;
        }
 
       
        this.savedTextArea.setWrapText(true);
        this.savedTextArea.appendText(selectedVideo.videoTitle + "\n" + selectedVideo.videoURL+"\n\n"); 
        
        videoQue.addYouTubeVideo(selectedVideo);

        
        
        
    }

    @FXML
    private void handleSaveArticle(ActionEvent event) {
        if (selectedArticle.webUrl == null) {
            return;
        }
        this.savedTextArea.setWrapText(true);
        this.savedTextArea.appendText(selectedArticle.headline + "\n" + selectedArticle.webUrl +"\n\n"); 

        newsQue.addNYTNewsStory(selectedArticle);
        
        
 
    }

    private boolean confirmContinueOnUnsavedData() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Data");
        alert.setHeaderText("Changes have not been saved.");
        alert.setContentText("Are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            return true;
        } else {
            // ... user chose CANCEL or closed the dialog
            return false;
        }
    }  



    @FXML
    private void handleSaveQue(ActionEvent event) {
        ArrayList<NYTNewsStory> stories = newsQue.getNewsStories();
        ArrayList<YouTubeVideo> videos = videoQue.getYouTubeVideos();
        JSONObject combined = new JSONObject();
        
        

        
        for (numberOfSavedStories = 0; numberOfSavedStories < stories.size(); numberOfSavedStories++) {

            combined.put("savedStory"+ numberOfSavedStories.toString(), stories.get(numberOfSavedStories).toJsonString());
    }
            

        for (numberOfSavedVideos = 0; numberOfSavedVideos < videos.size(); numberOfSavedVideos++) {

            combined.put("savedVideo"+ numberOfSavedVideos.toString(), videos.get(numberOfSavedVideos).toJsonString());
    }


        
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            try {
               String jsonString = combined.toJSONString();
               savedTextArea.appendText(jsonString);
               PrintWriter out = new PrintWriter(file.getPath());
               out.print(jsonString);
               out.close();
               
               
            } catch(IOException ioex) {
                displayExceptionAlert(ioex);
            } 
        }  
        
    }

    @FXML
    private void importSavedJSON(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                
                FileReader fileReader = new FileReader(file.getPath());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
 
                String json = "";
                String line = null;
                while((line = bufferedReader.readLine()) != null) {
                    json += line;
                }
                bufferedReader.close();
                fileReader.close();
                if (json == null || json == "") return;

                JSONObject jsonObj;
                try {
                    jsonObj = (JSONObject)JSONValue.parse(json);
                } catch (Exception ex) {
                    return;
                }

                if (jsonObj == null) {
                    return;
                }     
                
    
                
                String story =(String) jsonObj.getOrDefault("savedStory0", "");
                String video =(String) jsonObj.getOrDefault("savedVideo0", "");
                
                NYTNewsStory loadedStory = new NYTNewsStory("","","","","","","");
                YouTubeVideo loadedVideo = new YouTubeVideo("","");
                
                
                
                loadedStory.initFromJsonString(story);
                loadedVideo.initFromJsonString(video);
                

                webEngineNews = newsWebView.getEngine();
                webEngineNews.load(loadedStory.webUrl);    
                
                webEngineVideo = videoWebView.getEngine();
                webEngineVideo.load(loadedVideo.videoURL);  

                
            } catch(IOException ioex) {
                
               displayExceptionAlert(ioex);
            }

        }
    }


    
}
