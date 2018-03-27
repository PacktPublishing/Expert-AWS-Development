package com.amazonaws.samples;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class MovieObjectPersistenceExample {

    static AmazonDynamoDB client;

    @SuppressWarnings("deprecation")
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
        
        client = AmazonDynamoDBClientBuilder.standard().build();
        client = new AmazonDynamoDBClient(credentials);
        client.setRegion(usWest2);
    }

    public static void main(String[] args) throws Exception {
        init();
        
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        
        System.out.println("Example for Object Persistence Interface");
        
        MovieObjectMapper movieObjectMapper = new MovieObjectMapper();
        movieObjectMapper.setName("Airplane"); 
        try {
        	MovieObjectMapper result = mapper.load(movieObjectMapper);
            if (result != null) {
                System.out.println(
                "The song was released in "+ result.getYear());
            } else {
                System.out.println("No matching song was found");
            }
        } catch (Exception e) {
            System.err.println("Unable to retrieve data: ");
            System.err.println(e.getMessage());
        }
    }
}
