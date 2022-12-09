package edu.northeastern.cs5520group7.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Reader {

    private String email;
    private String password;
    private List<Book> list;
    private List<Book> discovery;
    private Map<String, Post> posts;

    public Reader(){

    }

    //initiate a new reader that doesn't have list and posts
    public Reader(String email, String password, List<Book> discovery){
        this.email = email;
        this.password = password;
        this.discovery = discovery;
        this.list = new ArrayList<>();
        this.posts = new HashMap<>();
    }

    //a reader that doesn't have posts
    public Reader(String email, String password, List<Book>list, List<Book> discovery){
        this.email = email;
        this.password = password;
        this.discovery = discovery;
        this.list = list;
        this.posts = new HashMap<>();
    }

    public Reader(String email, String password, List<Book>list, List<Book> discovery, Map<String, Post> posts){
        this.email = email;
        this.password = password;
        this.discovery = discovery;
        this.list = list;
        this.posts = posts;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Map<String, Post> getPosts(){
        return posts;
    }
}
