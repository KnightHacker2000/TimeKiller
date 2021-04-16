package edu.osu.timekiller;

public class Card {
    String title;
    String description;
    public Card(){

    }
    public Card(String title, String des){
        this.title = title;
        this.description = des;
    }
    public String getTitle(){
        return  title;
    }
    public String getDescription(){
        return description;
    }
    public void setTitle(String newT){
        this.title = newT;
    }
    public void setDescription(String newD){
        this.description = newD;
    }
}
