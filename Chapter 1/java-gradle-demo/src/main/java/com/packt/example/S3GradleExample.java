package com.packt.example;

import java.util.UUID;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3GradleExample {

    public static void main(String[] args) {

        AmazonS3 s3 = new AmazonS3Client();
        Region s3Region = Region.getRegion(Regions.AP_SOUTHEAST_1);
        s3.setRegion(s3Region);
		
        String bucketName = "s3-gradle-bucket-" + UUID.randomUUID();
        System.out.println("Amazon S3 will create/delete bucket");

        // Create a new bucket
        System.out.println("Creating bucket " + bucketName + "\n");
        s3.createBucket(bucketName);
 
        // Delete a bucket.
        System.out.println("Deleting bucket " + bucketName + "\n");
        s3.deleteBucket(bucketName);
    }
}
