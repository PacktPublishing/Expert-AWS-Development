package com.amazonaws.samples;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class MovieDocumentInterfaceExample {

    static AmazonDynamoDB client;

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
        Region usWest2 = Region.getRegion(Regions.US_EAST_1);
        
        client = new AmazonDynamoDBClient(credentials);
        client.setRegion(usWest2);
    }

    public static void main(String[] args) throws Exception {
        init();

            try {
            	String tableName = "Movies";
            	DynamoDB docClient = new DynamoDB(client);
                System.out.println("Example for Document Interface");

                Table movieTable = docClient.getTable(tableName);
                 
                GetItemOutcome outcome = movieTable.getItemOutcome(
                        "name","Airplane");

                int yearObj = outcome.getItem().getInt("year");
                System.out.println("The movie was released in " + yearObj);
               
	        } catch (Exception e) {
	            System.err.println("Unable to retrieve data: ");
	            System.err.println(e.getMessage());
	        }
    }


}
