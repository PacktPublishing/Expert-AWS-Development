package com.packt.example;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.*;

public class Types 
{
	public static final String DOMAIN = "MyDomain";
	public static final String TASKLIST = "MyTasklist";
	public static final String WORKFLOW = "MyWorkflow";
	public static final String WORKFLOW_VERSION = "1.0";
	public static final String ACTIVITY = "MyActivity";
	public static final String ACTIVITY_VERSION = "1.0";

	
	public static void registerDomain(AmazonSimpleWorkflow swf) {
	    try {
	        System.out.println("Register the domain '" + DOMAIN + "'.");
	        swf.registerDomain(new RegisterDomainRequest()
	            .withName(DOMAIN)
	            .withWorkflowExecutionRetentionPeriodInDays("1"));
	    } catch (DomainAlreadyExistsException e) {
	        System.out.println("Exception: Domain Already exists!");
	    }
	}
	
	public static void registerActivityType(AmazonSimpleWorkflow swf) {
	    try {
	        System.out.println("Register Activity Type '" + ACTIVITY +
	            "-" + ACTIVITY_VERSION + "'.");
	        swf.registerActivityType(new RegisterActivityTypeRequest()
	            .withDomain(DOMAIN)
	            .withName(ACTIVITY)
	            .withVersion(ACTIVITY_VERSION)
	            .withDefaultTaskList(new TaskList().withName(TASKLIST))
	            .withDefaultTaskScheduleToStartTimeout("30")
	            .withDefaultTaskStartToCloseTimeout("600")
	            .withDefaultTaskScheduleToCloseTimeout("630")
	            .withDefaultTaskHeartbeatTimeout("10")); 
	    } catch (TypeAlreadyExistsException e) {
	        System.out.println("Exception: Activity type already exists!");
	    }
	}

	public static void registerWorkflowType(AmazonSimpleWorkflow swf) {
	    try {
	        System.out.println("Register Workflow Type '" + WORKFLOW +
	            "-" + WORKFLOW_VERSION + "'.");
	        swf.registerWorkflowType(new RegisterWorkflowTypeRequest()
	            .withDomain(DOMAIN)
	            .withName(WORKFLOW)
	            .withVersion(WORKFLOW_VERSION)
	            .withDefaultChildPolicy(ChildPolicy.TERMINATE)
	            .withDefaultTaskList(new TaskList().withName(TASKLIST))
	            .withDefaultTaskStartToCloseTimeout("30")); 
	    } catch (TypeAlreadyExistsException e) {
	        System.out.println("Exception: Workflow type already exists!");
	    }
	}

	public static void main(String[] args) {
		ProfileCredentialsProvider credentials = new ProfileCredentialsProvider();
        try {
        	credentials.getCredentials();
        } catch (Exception e) {
        	throw new AmazonClientException("Unable to load Credentials.", e);
        }
        
		AmazonSimpleWorkflow swf =
			    AmazonSimpleWorkflowClientBuilder.standard()
			    		.withCredentials(credentials)
			    		.withRegion(Regions.US_WEST_2)
                		.build();
		
		registerDomain(swf);
	    registerWorkflowType(swf);
	    registerActivityType(swf);
	}

}
