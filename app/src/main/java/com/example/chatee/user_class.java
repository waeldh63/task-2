package com.example.chatee;

public class user_class {


    public String name,image,status,key;
public user_class(){


}


public user_class(String name,String image,String status,String key){
this.name = name;
this.image=image;
this.status=status;
    this.key=key;
}

    public String getImage(){

        return image;
    }

    public String getStatus(){

        return status;
    }

public String getName(){

    return name;
}
public void setName(String name){
    this.name=name;


}

    public void setImage(String image){
        this.image=image;


    }
    public void setStatus(String status){
        this.status=status;


    }
    public void setKey(String key){
        this.key=key;


    }



    public String getkey(){

        return key;
    }




}
