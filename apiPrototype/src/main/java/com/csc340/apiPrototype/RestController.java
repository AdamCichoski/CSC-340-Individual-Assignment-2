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
    /**
     * Home page endpoint for a base look up
     * @return
     */
    @GetMapping("")
    public String home(){
        return "Welcome to the home page";
    }

    /**
     * Definition of software endpoint from Dictionary API
     * @return an ArrayList of Strings with the most relevant data in the api under the term "software"
     */
    @GetMapping("/software")
    public Object getWeather(){
        /*
        I wanted to pull out the word, the type of speech it is used for, and any definitions.
        The response from the API looks like this:
                {
                    "word": "software",
                    "phonetic": "/ˈsɑftˌwɛɹ/",
                    "phonetics": [
                        {
                            "text": "/ˈsɑftˌwɛɹ/",
                            "audio": ""
                        },
                        {
                            "text": "/ˈsɒftˌwɛə/",
                            "audio": ""
                        },
                        {
                            "text": "/ˈsɔftˌwɛɹ/",
                            "audio": "https://api.dictionaryapi.dev/media/pronunciations/en/software-us.mp3",
                            "sourceUrl": "https://commons.wikimedia.org/w/index.php?curid=2100853",
                            "license": {
                                "name": "BY-SA 3.0",
                                "url": "https://creativecommons.org/licenses/by-sa/3.0"
                            }
                        }
                    ],
                    "meanings": [
                        {
                            "partOfSpeech": "noun",
                            "definitions": [
                                {
                                    "definition": "Encoded computer instructions, usually modifiable (unless stored in some form of unalterable memory such as ROM).",
                                    "synonyms": [],
                                    "antonyms": []
                                },
                                {
                                    "definition": "The human beings involved in warfare, as opposed to hardware such as weapons and vehicles.",
                                    "synonyms": [],
                                    "antonyms": []
                                }
                            ],
                            "synonyms": [],
                            "antonyms": []
                        }
                    ],
                    "license": {
                        "name": "CC BY-SA 3.0",
                        "url": "https://creativecommons.org/licenses/by-sa/3.0"
                    },
                    "sourceUrls": [
                        "https://en.wiktionary.org/wiki/software"
                    ]
                }
         */

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
