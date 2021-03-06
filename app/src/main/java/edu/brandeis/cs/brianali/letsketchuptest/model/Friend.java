package edu.brandeis.cs.brianali.letsketchuptest.model;

public class Friend {
    private String email;

    public Friend(){

    }

    public Friend(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(!(o instanceof Friend)){
            return false;
        }
        Friend friend = (Friend) o;
        return this.email == friend.email;
    }

    public int hashCode(){
        return email.hashCode();
    }
}
