package com.amazonaws.samples;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.PutRecordBatchRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordBatchResult;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;

public class KinesisFirehoseExample {

	private static AmazonKinesisFirehoseClient client;

	private static void init() throws Exception {

		ProfileCredentialsProvider credentials;
		credentials = new ProfileCredentialsProvider();
		try {
			credentials.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Unable to load Credentials", e);
		}

		client = new AmazonKinesisFirehoseClient(credentials);

		client.withRegion(Regions.US_EAST_1);
		client.builder();

	}

	public static void main(String[] args) throws Exception {
		init();

		String data = "My Kinesis Firehose data";
		String myFirehoseStream = "MyFirehoseStream";
		Record record = new Record();
		record.setData(ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8)));
		PutRecordRequest putRecordRequest = new PutRecordRequest()
				.withDeliveryStreamName(myFirehoseStream)
				.withRecord(record);
		putRecordRequest.setRecord(record);

		PutRecordResult putRecordResult = client.putRecord(putRecordRequest);
		System.out.println("Put Request Record ID : " + putRecordResult.getRecordId());

		PutRecordBatchRequest putRecordBatchRequest = new PutRecordBatchRequest()
				.withDeliveryStreamName("MyFirehoseStream")
				.withRecords(getBatchRecords());
		 
		PutRecordBatchResult putRecordBatchResult = client.putRecordBatch(putRecordBatchRequest);
		
		for(int i=0;i<putRecordBatchResult.getRequestResponses().size();i++){
			System.out.println("Put Batch Request Record ID :"+i+": " + putRecordBatchResult.getRequestResponses().get(i).getRecordId());
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Record> getBatchRecords(){
		  List<Record> records = new ArrayList<Record>();
		 
		  JSONObject jsonObject = new JSONObject();
		  jsonObject.put("userid", "userid_1");
		  jsonObject.put("password", "password1");
		  Record record = new Record().withData(ByteBuffer.wrap(jsonObject.toString().getBytes()));
		  records.add(record);
		 
		  jsonObject.put("userid", "userid_2");
		  jsonObject.put("password", "password2");
		  record = new Record().withData(ByteBuffer.wrap(jsonObject.toString().getBytes()));
		  records.add(record);
		 
		  return records;
		}
}