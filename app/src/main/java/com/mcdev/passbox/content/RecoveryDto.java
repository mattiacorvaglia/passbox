package com.mcdev.passbox.content;

public class RecoveryDto {
	
	private long id;
	private long passwordKey;
	private String question;
	private String answer;
	
	// Empty Constructor
	public RecoveryDto() {}
	
	/**
	 * Getters and Setters
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getPasswordKey() {
		return passwordKey;
	}

	public void setPasswordKey(long passwordKey) {
		this.passwordKey = passwordKey;
	}
	
}
