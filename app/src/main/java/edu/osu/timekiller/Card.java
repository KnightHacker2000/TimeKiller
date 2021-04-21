package edu.osu.timekiller;

public class Card {
    String title;
    String description;
    String place_name;
    public Card(){

    }

//    public Card(String title, String des){
//        this.title = title;
//        this.description = des;
//        this.place_name = place_name;
//    }


    public Card(String title, String des, String place_name){
        this.description = des;
        this.title = title;
        this.place_name = place_name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace_name() {
        return place_name;
    }


    public void setTitle(String newT){
        this.title = newT;
    }
    public void setDescription(String newD){
        this.description = newD;
    }
    public void setPlace_name(String newP){
        this.place_name = newP;
    }
}
