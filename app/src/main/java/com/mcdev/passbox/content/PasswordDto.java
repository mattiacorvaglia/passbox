package com.mcdev.passbox.content;

import java.util.LinkedList;

public class PasswordDto {
	
	private long id;
	private String title;
	private String description;
	private String username;
	private String password;
	private String webUrl;
	private String color;
	private LinkedList<RecoveryDto> recoveryList;
	
	// Empty Constructor
	public PasswordDto() {
		this.recoveryList = new LinkedList<RecoveryDto>();
	}

	/**
	 * Getters and Setters
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public LinkedList<RecoveryDto> getRecoveryList() {
		return recoveryList;
	}

	public void setRecoveryList(LinkedList<RecoveryDto> recoveryList) {
		this.recoveryList = recoveryList;
	}
	
	/**
	 * Utils
	 */
	public void addRecovery(RecoveryDto recovery) {
		recoveryList.add(recovery);
	}
	
}
