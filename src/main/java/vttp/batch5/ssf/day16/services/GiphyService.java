package vttp.batch5.ssf.day16.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonArray;

import java.io.StringReader;

import static vttp.batch5.ssf.day16.Constants.*;

@Service
public class GiphyService {

    @Value("${giphy.api.key}")
    private String API_KEY;
    
    public List<String> search(String query, String limit, String rating) {

        // Initialise array to store the images URL
        List<String> imagesUrl = new ArrayList<>();

        String searchUrl = "https://api.giphy.com/v1/gifs/search?api_key=%s&q=%s&limit=%s&offset=%s&rating=%s&lang=%s&bundle=%s";

        // Format the url with input from the form
        String formattedUrl = searchUrl.formatted(API_KEY, query, limit, OFFSET, rating, LANG, BUNDLE);

        // USING UriComponentsBuilder

        /*
        
            String url = UriComponentsBuilder
                        .fromUriString(SEARCH_URL)
                        .queryParam("api_key", apiKey)

                        ... ^^^

                        .toUriString();
         */
    
        System.out.println(formattedUrl);

        RequestEntity<Void> req = RequestEntity
            .get(formattedUrl)
            .accept(MediaType.APPLICATION_JSON)
            .build();
    
        RestTemplate template = new RestTemplate();
        
        ResponseEntity<String> resp = null;
    
        try {

            resp = template.exchange(req, String.class);

            String payload = resp.getBody();
            
            JsonReader reader = Json.createReader(new StringReader(payload));

            JsonObject result = reader.readObject();

            JsonArray results = result.getJsonArray("data"); 

            for (int i = 0; i < results.size(); i++) {

                JsonObject data = results.getJsonObject(i);

                JsonObject images = data.getJsonObject("images");

                JsonObject fixed_height = images.getJsonObject("fixed_height");

                String imageUrl = fixed_height.getString("url");

                imagesUrl.add(imageUrl);
            }   
        }
        catch (Exception e) {
            System.out.println("Error occured");
            System.out.println(e.getMessage());
        }
        
        return imagesUrl;
    }
}
