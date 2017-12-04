package edu.brandeis.cs.brianali.letsketchuptest.model;

import edu.brandeis.cs.brianali.letsketchuptest.R;

public class Message {

    private String sender;
    private String message;
    private String contentType = "";
    private String contentLocation = "";


    public Message(){

    }

    //Constructor for plain text message
    public Message(String sender, String message, String time){
        this.sender = sender;
        this.message = message;
    }

    //Constructor for initial message
    public Message(String sender){
        this.sender = "system";
        this.message = "If you would like to leave this chat, please select the \"leave\" option";
    }



    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getContentLocation() {
        return contentLocation;
    }



    public String getContentType() {
        return contentType;
    }
}
