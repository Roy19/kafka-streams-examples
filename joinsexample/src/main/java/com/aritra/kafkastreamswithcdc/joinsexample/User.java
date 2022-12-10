package com.aritra.kafkastreamswithcdc.joinsexample;

public class User implements JSONSerdeCompatible {
    public Integer id;
    public String username;
	
    public User() {
    }
    
    public User(Integer id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}    
    
}
