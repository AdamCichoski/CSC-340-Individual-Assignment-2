package com.csc340.apiPrototype;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@org.springframework.web.bind.annotation.RestController
public class RestController{
    @GetMapping("")
    public String home(){
        return "Welcome to the home page";
    }

    /**
     *
     * @return an ArrayList of Strings with the most relevant data in the api under the term "software"
     */
    @GetMapping("/software")
    public Object getWeather(){
        try{
            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/software";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonListResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonListResponse);
            String meanings = "";
            List<Dictionary> dict = new ArrayList<>();

            //Iterating through the first layer of the Json object, tracing through initial nodes
            for (JsonNode node : root){
                String word = node.get("word").asText();
                Object temp = node.get("meanings");
                meanings = temp.toString();
                dict.add(new Dictionary (word));
            }
            JsonNode root2 = mapper.readTree(meanings); //Stores the meanings node

            String defs = "";

            //Iterating through the values in the meanings Json node to pull out the part of speech
            for(JsonNode node : root2){
                String speech = node.get("partOfSpeech").asText();
                Object temp = node.get("definitions");
                defs = temp.toString();
                dict.get(0).addPartOfSpeech(speech);
            }
            JsonNode root3 = mapper.readTree(defs); //Stores the definitions node

            short count=1;

            //Iterating through the definitions node inside the meanings node in order to extract all definitions of the word
            for(JsonNode node : root3){
                dict.get(0).addDefinition("Definition "+ count+ ": "+node.get("definition").asText());
                count++;
            }
            return dict.get(0).toString();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /weather";
        }
    }

    @GetMapping("/error")
    public String error(){
        return "Something went wrong";
    }
}
