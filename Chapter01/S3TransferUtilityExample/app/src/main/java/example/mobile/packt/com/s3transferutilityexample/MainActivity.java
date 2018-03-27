package example.mobile.packt.com.s3transferutilityexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Create S3 instance
    AmazonS3 s3Client;

    //Create Transfer Utility instance to upload and download file
    TransferUtility transferUtility;

    // Create the File instance with file name and path.
    File uploadFilePath = new File("/storage/sdcard0/DCIM/Screenshots/upload_photo.png");
    File downloadFilePath = new File("/storage/sdcard0/DCIM/downloaded_photo.png");

    /**
     *  This method is used to Initialize Activity.
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This method is used to layout your resource by defining the UI
        setContentView(R.layout.activity_main);

        // This Method to used to create Cognito creadential instance.
        getCognitoCredentials();

        // This Method to used to create Transfer Utility instance.
        createTransferUtility();
    }

    public void getCognitoCredentials(){
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                //"us-east-1:aa9cca2f-ab7b-41a4-9d01-e7178779f678", // Identity Pool ID
                "us-east-1:63e4a4cf-1a50-493f-a4a0-7b797a4838a3",
                Regions.US_EAST_1 // Amazon Cognito Region
        );

        createS3Client(credentials);
    }

    /**
     *  Create a S3Client
     */
    public void createS3Client(CognitoCachingCredentialsProvider credentials){
        // Create S3 client with cognito Credentials.
        s3Client = new AmazonS3Client(credentials);
        // Set S3 bucket region
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    /**
     *  This method is used to Instantiate
     **/
    public void createTransferUtility(){
        // Create Transfer Utility Instance
        transferUtility = new TransferUtility(s3Client, getApplicationContext());
    }

    /**
     * This method will create TransferUtility class to upload the file in S3
     */
    public void uploadFile(View view){

        TransferObserver transferObserver = transferUtility.upload(
                "mycognitobucket",     /* S3 bucket name*/
                "downloaded_photo.png",       /* Upload object key name*/
                uploadFilePath       /* Upload File Path */
        );
    }

    /**
     *  This method will create transferUtility class to download the file from S3
     **/
    public void downloadFile(View view){

        TransferObserver transferObserver = transferUtility.download(
                "mycognitobucket",     /* S3 bucket name*/
                "downloaded_photo.png",    /* Download object key name*/
                downloadFilePath        /* Download File Path*/
        );
    }
}
