package com.packt.example;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.*;

public class Starter 
{
	private static final AmazonSimpleWorkflow simpleWorkflow =
		    AmazonSimpleWorkflowClientBuilder.defaultClient();
	
    public static final String WORKFLOW_EXECUTION = "ExampleWorkflowExecution";

    public static void main(String[] args) {
        String workflow_input = "Amazon SWF";
        if (args.length > 0) {
            workflow_input = args[0];
        }

        System.out.println("Starting the workflow execution '" + WORKFLOW_EXECUTION +
                "' with input '" + workflow_input + "'.");

        WorkflowType wf_type = new WorkflowType()
            .withName(Types.WORKFLOW)
            .withVersion(Types.WORKFLOW_VERSION);

        Run run = simpleWorkflow.startWorkflowExecution(new StartWorkflowExecutionRequest()
            .withDomain(Types.DOMAIN)
            .withWorkflowType(wf_type)
            .withWorkflowId(WORKFLOW_EXECUTION)
            .withInput(workflow_input)
            .withExecutionStartToCloseTimeout("90"));

        System.out.println("Workflow execution started with the run id '" +
                run.getRunId() + "'.");
    }	

}
