package edu.osu.timekiller;

public class Card {
    String title;
    String description;
    String place_name;
    String post_id;

    public Card(){

    }


    public Card(String title, String des, String place_name, String post_id){
        this.description = des;
        this.title = title;
        this.place_name = place_name;
        this.post_id = post_id;
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

    public String getPost_id() { return post_id; }

    public void setTitle(String newT){
        this.title = newT;
    }
    public void setDescription(String newD){
        this.description = newD;
    }
    public void setPlace_name(String newP){
        this.place_name = newP;
    }
    public void setPost_id(String post_id) { this.post_id = post_id; }
}
