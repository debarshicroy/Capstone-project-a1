package com.feedback.assignment.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.feedback.assignment.DO.FeedbackRequest;

@CrossOrigin
public class SaveFeedbackHandler implements RequestStreamHandler{

	private DynamoDB dynamoDb;
	private String DYNAMODB_TABLE_NAME = "Feedback";
    private Regions REGION = Regions.US_WEST_2;	
    
    @SuppressWarnings("unchecked")
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub
    	JSONParser parser = new JSONParser();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONObject responseJson = new JSONObject();
        this.initDynamoDbClient();
        
        try {
			JSONObject event = (JSONObject) parser.parse(reader);
			
			if (event.get("body") != null) {
	            FeedbackRequest feedback = new FeedbackRequest((String) event.get("body"));
	            
	            persistData(feedback);
	        }
			
			JSONObject responseBody = new JSONObject();
	        responseBody.put("message", "New item created");
	 
	        JSONObject headerJson = new JSONObject();
	        headerJson.put("x-custom-header", "my custom header value");
	 
	        responseJson.put("statusCode", 200);
	        responseJson.put("headers", headerJson);
	        responseJson.put("body", responseBody.toString());
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			responseJson.put("statusCode", 400);
	        responseJson.put("exception", e);
			
			e.printStackTrace();
		}
        
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
	}
    
    
    public void handleGetByParam(
    	      InputStream inputStream, OutputStream outputStream, Context context)
    	      throws IOException {
    	 
    	        // implementation
    	    }
    
    
	/*
	 * @Override public FeedbackResponse handleRequest(FeedbackRequest input,
	 * Context context) { // TODO Auto-generated method stub
	 * 
	 * this.initDynamoDbClient();
	 * 
	 * persistData(input);
	 * 
	 * FeedbackResponse personResponse = new FeedbackResponse();
	 * personResponse.setMessage("Saved Successfully!!!"); return personResponse;
	 * 
	 * }
	 */
	
	private PutItemOutcome persistData(FeedbackRequest feedbackRequest) 
		      throws ConditionalCheckFailedException {
		        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
		          .putItem(
		            new PutItemSpec().withItem(new Item()
		              .withPrimaryKey("feedbackId", UUID.randomUUID().toString())
		              .withString("name", feedbackRequest.getName())
		              .withString("email_id", feedbackRequest.getEmail_id())
		              .withString("comments", feedbackRequest.getComments())
		              .withInt("ratings", feedbackRequest.getRatings())));
	}
	
	private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }

	

}
