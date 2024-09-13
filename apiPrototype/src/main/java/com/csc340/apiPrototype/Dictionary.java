package com.csc340.apiPrototype;

import java.util.ArrayList;

public class Dictionary {
    private String word;
    private ArrayList<String> definition, partOfSpeech;

    /**
     * @param word
     */
    public Dictionary(String word){
        this.word = word;
    }


    public void setWord(String word){
        this.word = word;
    }

    public void setPartOfSpeech(ArrayList<String> partOfSpeech){
        this.partOfSpeech = partOfSpeech;
    }

    public void addPartOfSpeech(String partOfSpeech){
        if(this.partOfSpeech == null){
            this.partOfSpeech = new ArrayList<>();
        }
        this.partOfSpeech.add(partOfSpeech);
    }

    public void setDefinition(ArrayList<String> definition){
        this.definition = definition;
    }

    public void addDefinition(String definition){
        if(this.definition == null){
            this.definition = new ArrayList<>();
        }
        this.definition.add(definition);
    }

    public String getWord(){
        return word;
    }
    public String getPartOfSpeech(){
        return partOfSpeech.toString();
    }

    public String getDefinition(){
        return definition.toString();
    }

    @Override
    public String toString(){
        return word +": " + getPartOfSpeech() + " " + getDefinition();
    }
}
