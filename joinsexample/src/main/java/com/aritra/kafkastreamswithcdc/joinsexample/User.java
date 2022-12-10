package com.aritra.kafkastreamswithcdc.joinsexample;

public class User implements JSONSerdeCompatible {
    public int id;
    public String username;
	
    public User() {
    }
    
    public User(int id, String username) {
		super();
		this.id = id;
		this.username = username;
	}
}
