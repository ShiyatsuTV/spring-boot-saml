package com.shiyatsu.basic;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class UserDto {

	private UUID id;
	private String name;
	private String password;
	private String creationDate;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	public UserDto() {
		this.id = UUID.randomUUID();
		this.name = "User-" + this.id;
		this.creationDate = sdf.format(System.currentTimeMillis());
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
}
