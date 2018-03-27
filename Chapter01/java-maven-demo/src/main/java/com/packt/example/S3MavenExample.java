package com.packt.example;

import java.util.UUID;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3MavenExample {

    public static void main(String[] args) {

        AmazonS3 s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.AP_SOUTHEAST_1);
        s3.setRegion(usWest2);
		
        String bucketName = "s3-maven-bucket-" + UUID.randomUUID();

        System.out.println("Amazon S3 will create and delete bucket");

		/*
		 * Create a new S3 bucket .
		 */
		System.out.println("Creating bucket " + bucketName + "\n");
		s3.createBucket(bucketName);

		/*
		 * Delete a bucket.
		 */
		System.out.println("Deleting bucket " + bucketName + "\n");
		s3.deleteBucket(bucketName);
    }
}
