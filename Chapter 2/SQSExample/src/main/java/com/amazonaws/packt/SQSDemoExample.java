package com.amazonaws.packt;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQSDemoExample {

    public static void main(String[] args) throws Exception {

        ProfileCredentialsProvider credentials = new ProfileCredentialsProvider();
        try {
        	credentials.getCredentials();
        } catch (Exception e) {
        	throw new AmazonClientException("Unable to load Credentials.", e);
        }

        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
                               .withCredentials(credentials)
                               .withRegion(Regions.US_WEST_2)
                               .build();

        System.out.println("Amazon SQS Demo Example Starts");

        try {
            // Create a SQS queue
            System.out.println("SQS queue MyDemoQueue Creating.\n");
            CreateQueueRequest createQueue = new CreateQueueRequest("MyDemoQueue");
            String myDemoQueueUrl = sqsClient.createQueue(createQueue).getQueueUrl();
            
            // List all the available SQS queues
            System.out.println("Listing of available queues in your account.\n");
            for (String queueUrl : sqsClient.listQueues().getQueueUrls()) {
                System.out.println("  Queue Url: " + queueUrl);
            }
            System.out.println();

            // Send a message
            System.out.println("Send a message to MyDemoQueue.\n");
            sqsClient.sendMessage(new SendMessageRequest(myDemoQueueUrl, "This is SQS Demo Example Test Message."));
            
            // Receive messages
            System.out.println("Receive message from MyDemoQueue.\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myDemoQueueUrl);
            
            List<Message> messageList = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
            
            System.out.println("Listing of available messages in MyDemoQueue.\n");
            for (Message msgInfo : messageList) {
                System.out.println("MessageId:" + msgInfo.getMessageId());
                System.out.println("ReceiptHandle:"+ msgInfo.getReceiptHandle());
                System.out.println("MD5 Of Body:" + msgInfo.getMD5OfBody());
                System.out.println("Body: " + msgInfo.getBody());
           }

            System.out.println();

            // Delete a message
            System.out.println("Delete a message.\n");
            String messageReceiptHandle = messageList.get(0).getReceiptHandle();
            sqsClient.deleteMessage(new DeleteMessageRequest(myDemoQueueUrl, messageReceiptHandle));

            // Delete a queue
            System.out.println("Delete the MyDemoQueue.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(myDemoQueueUrl));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
