package com.packt.example;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Worker{
	private static final AmazonSimpleWorkflow simpleWorkflow =
		    AmazonSimpleWorkflowClientBuilder.defaultClient();
	
	public static void main(String[] args) {
	    PollForDecisionTaskRequest task_request =
	        new PollForDecisionTaskRequest()
	            .withDomain(Types.DOMAIN)
	            .withTaskList(new TaskList().withName(Types.TASKLIST));

	    while (true) {
	        System.out.println(
	                "Polling for a decision task from the tasklist '" +
	                Types.TASKLIST + "' in the domain '" +
	                Types.DOMAIN + "'.");

	        DecisionTask task = simpleWorkflow.pollForDecisionTask(task_request);

	        String taskToken = task.getTaskToken();
	        System.out.println("taskToken >> "+taskToken);
	        if (taskToken != null) {
	            try {
	    	        System.out.println("before executeDecisionTask >> ");
	                executeDecisionTask(taskToken, task.getEvents());
	            } catch (Throwable th) {
	                th.printStackTrace();
	            }
	        }
	    }
	}

	private static void executeDecisionTask(String taskToken, List<HistoryEvent> events)
            throws Throwable {
        List<Decision> decisions = new ArrayList<Decision>();
        String workflow_input = null;
        int scheduled_activities = 0;
        int open_activities = 0;
        boolean activity_completed = false;
        String result = null;
        System.out.println("Executing the decision task for history events: [");
        for (HistoryEvent event : events) {
            System.out.println("  " + event);
            switch(event.getEventType()) {
                case "WorkflowExecutionStarted":
                    workflow_input =
                        event.getWorkflowExecutionStartedEventAttributes()
                             .getInput();
                    break;
                case "ActivityTaskScheduled":
                    scheduled_activities++;
                    break;
                case "ScheduleActivityTaskFailed":
                    scheduled_activities--;
                    break;
                case "ActivityTaskStarted":
                    scheduled_activities--;
                    open_activities++;
                    break;
                case "ActivityTaskCompleted":
                    open_activities--;
                    activity_completed = true;
                    result = event.getActivityTaskCompletedEventAttributes()
                                  .getResult();
                    break;
                case "ActivityTaskFailed":
                    open_activities--;
                    break;
                case "ActivityTaskTimedOut":
                    open_activities--;
                    break;
            }
            if (activity_completed) {
                decisions.add(
                    new Decision()
                        .withDecisionType(DecisionType.CompleteWorkflowExecution)
                        .withCompleteWorkflowExecutionDecisionAttributes(
                            new CompleteWorkflowExecutionDecisionAttributes()
                                .withResult(result)));
            } else {
                if (open_activities == 0 && scheduled_activities == 0) {

                    ScheduleActivityTaskDecisionAttributes attrs =
                        new ScheduleActivityTaskDecisionAttributes()
                            .withActivityType(new ActivityType()
                                .withName(Types.ACTIVITY)
                                .withVersion(Types.ACTIVITY_VERSION))
                            .withActivityId(UUID.randomUUID().toString())
                            .withInput(workflow_input);

                    decisions.add(
                            new Decision()
                                .withDecisionType(DecisionType.ScheduleActivityTask)
                                .withScheduleActivityTaskDecisionAttributes(attrs));
                } 
            }

            System.out.println("Exiting the decision task with the decisions " + decisions);

        }
        System.out.println("]");
        
        simpleWorkflow.respondDecisionTaskCompleted(
        	    new RespondDecisionTaskCompletedRequest()
        	        .withTaskToken(taskToken)
        	        .withDecisions(decisions));
	}
}
