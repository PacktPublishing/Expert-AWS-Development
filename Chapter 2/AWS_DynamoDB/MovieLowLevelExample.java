package com.amazonaws.samples;
import java.util.HashMap;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class MovieLowLevelExample {

    static AmazonDynamoDBClient dynamoDB;

    private static void init() throws Exception {
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\Atul\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usWest2);
    }

    public static void main(String[] args) throws Exception {
        init();

            String tableName = "Movies";

            System.out.println("Example for Low-level Interface");
            // DynamoDB Low level interfaces
            HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
            key.put("name", new AttributeValue().withS("Airplane"));
            
            GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

            try {
                GetItemResult result = dynamoDB.getItem(request);

                if (result.getItem() != null) {
                    AttributeValue yearObj = result.getItem().get("year");
                    System.out.println("The movie was released in " + yearObj.getN());
                } else {
                    System.out.println("No matching movie was found");
                }
                
	        } catch (Exception e) {
	            System.err.println("Unable to retrieve data: ");
	            System.err.println(e.getMessage());
	        } 
    }
}