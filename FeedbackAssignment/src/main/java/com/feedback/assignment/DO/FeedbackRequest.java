package com.feedback.assignment.DO;

import com.google.gson.Gson;

public class FeedbackRequest {

	private String name;
    private int ratings;
    private String email_id;
    private String comments;
    
    public FeedbackRequest(String json) {
    	Gson gson = new Gson();    	
    	FeedbackRequest request = gson.fromJson(json, FeedbackRequest.class);
        this.name = request.getName();
        this.email_id = request.getEmail_id();    	
    	this.ratings = request.getRatings();
    	this.comments = request.getComments();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRatings() {
		return ratings;
	}
	public void setRatings(int ratings) {
		this.ratings = ratings;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
