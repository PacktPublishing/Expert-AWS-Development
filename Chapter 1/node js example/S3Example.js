// Load the AWS-SDK for AWS services and UUID for pseudo number generation
var AWS = require('aws-sdk');
var uuid = require('uuid');

// Create S3 client
var s3 = new AWS.S3();

// Create a bucket with some random number generation
var bucketName = 'node-sdk-sample-' + uuid.v4();
var params={Bucket: bucketName}

s3.createBucket(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else console.log("Successfully Created Bucket: "+bucketName);  // successful response
 });

 // Delete bucket if bucket exists
 s3.waitFor('bucketExists', params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else { 
		s3.deleteBucket(params, function(err, data) {
			if (err) console.log(err, err.stack); // an error occurred
			else     console.log("Successfully Deleted Bucket: "+bucketName);   // successful response
		});
  }  
});
