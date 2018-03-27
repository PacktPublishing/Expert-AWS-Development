package com.amazonaws.samples;

import java.nio.ByteBuffer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.CreateStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import com.amazonaws.services.kinesis.model.StreamDescription;

public class KinesisDemoExample {

    static AmazonKinesis client;

    private static void init() throws Exception {
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Unable to load Credentials.", e);
        }

        client = AmazonKinesisClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-east-1")
            .build();
    }

    public static void main(String[] args) throws Exception {
        init();

        final String streamName = "MyExampleStream";
        final Integer streamSize = 1;

        DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest().withStreamName(streamName);
        try {
            StreamDescription streamDescription = client.describeStream(describeStreamRequest).getStreamDescription();
            System.out.printf("Kinesis Stream \"%s\" has a status of \"%s\".\n", streamName, streamDescription.getStreamStatus());
        } catch (ResourceNotFoundException ex) {
            System.out.printf("Request Stream \"%s\" does not exist. Please create new stream.\n", streamName);

            CreateStreamRequest createStreamRequest = new CreateStreamRequest();
            createStreamRequest.setStreamName(streamName);
            createStreamRequest.setShardCount(streamSize);
            client.createStream(createStreamRequest);
        }

        System.out.printf("Putting records in stream : \"%s\" until this application is stopped...\n", streamName);
        while (true) {
            long createTime = System.currentTimeMillis();
            PutRecordRequest putRecordRequest = new PutRecordRequest();
            putRecordRequest.setStreamName(streamName);
            putRecordRequest.setData(ByteBuffer.wrap(String.format("testData-%d", createTime).getBytes()));
            putRecordRequest.setPartitionKey(String.format("partitionKey-%d", createTime));
            
            PutRecordResult putRecordResult = client.putRecord(putRecordRequest);
            System.out.printf("Success : Partition key \"%s\", ShardID \"%s\" and SequenceNumber \"%s\".\n",
                    putRecordRequest.getPartitionKey(), putRecordResult.getShardId(), putRecordResult.getSequenceNumber());
        }
    }
}
