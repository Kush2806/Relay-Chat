package com.relaychat.Matching.Service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_session")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean matched;

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
    public void setUserId(String id){
        this.userId = id;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setActive(boolean status){
        this.active = status;
    }
    public boolean isActive(){
        return this.active;
    }
    public void setMatched(boolean status){
        this.matched = status;
    }
    public boolean isMatched(){
        return this.matched;
    }
}