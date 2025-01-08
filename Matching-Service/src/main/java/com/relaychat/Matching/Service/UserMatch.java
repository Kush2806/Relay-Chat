package com.relaychat.Matching.Service;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "active_users")
public class UserMatch {

    @Id
    private String id;

    private String userId1;
    private String userId2;
    private boolean active;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getUserId1(){
        return this.userId1;
    }
    public void setUserId1(String id){
        this.userId1 = id;
    }
    public String getUserId2(){
        return this.userId2;
    }
    public void setUserId2(String id){
        this.userId2 = id;
    }
    public void setActive(boolean status){
        this.active = status;
    }
    public boolean isActive(){
        return this.active;
    }
}
