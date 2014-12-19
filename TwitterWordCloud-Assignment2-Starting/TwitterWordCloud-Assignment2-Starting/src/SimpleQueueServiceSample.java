/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p>
 * WANRNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 */
public class SimpleQueueServiceSample {

	public static AmazonSQS sqs;
	//create a new SNS client and set endpoint
	public static AmazonSNSClient snsClient;	                          
	public static String tweetQueue = "TweetQueue";
	public static String processedTweetQueue = "ProcessedTweetQueue";
	public static String rawQueueURL = "https://sqs.us-west-2.amazonaws.com/314401218375/TweetQueue";
	public static String processedQueueURL = "https://sqs.us-west-2.amazonaws.com/314401218375/ProcessedTweetQueue";
	public static String topicARN = "arn:aws:sns:us-west-2:314401218375:TweetSNSService";
    public static String processedQueueARN = "arn:aws:sqs:us-west-2:314401218375:ProcessedTweetQueue";
    public static String subscriptionARN = "arn:aws:sns:us-west-2:314401218375:TweetSNSService:48257d3b-b70e-4760-a6a1-62b0cce30bb7";
    
	public SimpleQueueServiceSample() throws Exception {

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * ().
         */
        AWSCredentials credentials = null;
        try {
        	credentials = new PropertiesCredentials(
        			DatabaseWrapper.class.getResourceAsStream("AwsCredentials.properties"));
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/daniel/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);
        snsClient = new AmazonSNSClient(credentials);
        snsClient.setRegion(usWest2);
        // Create a queue
        //QueueURL = createQueue(tweetQueue);
        
        //delete a queue
        //deleteQueue(tweetQueue);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
        //ListSubscriptionsByTopicResult res = snsClient.listSubscriptionsByTopic(topicARN);
        //List<Subscription> ls = res.getSubscriptions();
        //for(Subscription s : ls) {
        //	System.out.println("Subscription = " + s.getEndpoint());
        //}
        /*
        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called MyQueue.\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(tweetQueue);
            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();

            // Send a message
            System.out.println("Sending a message to MyQueue.\n");
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));

            // Receive messages
            System.out.println("Receiving messages from MyQueue.\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
                System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("  Attribute");
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
            }
            System.out.println("Second list = ");
            
            List<Message> messages2 = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages2) {
                System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("  Attribute");
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
            }
            
            // Delete a message
            System.out.println("Deleting a message.\n");
            String messageRecieptHandle = messages.get(0).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            //sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
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
        */
    }
	
	/*
	public static SimpleQueueServiceSample getInstance(){
        return awssqsUtil;
    }
 
    public AmazonSQS getAWSSQSClient(){
         return awssqsUtil.sqs;
    }
 
    public String getQueueName(){
         return awssqsUtil.tweetQueue;
    }
    */
	
    public static String getRawQueueURL(){
        return rawQueueURL;
   }
    
    /**
     * Creates a queue in your region and returns the url of the queue
     * @param queueName
     * @return
     */
    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        String queueUrl = this.sqs.createQueue(createQueueRequest).getQueueUrl();
        return queueUrl;
    }
    
    /**
     * Deletes a queue in your region and returns the url of the queue
     * @param queueName
     * @return
     */
    public void deleteQueue(String queueName){
        DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest(queueName);
        this.sqs.deleteQueue(deleteQueueRequest);
    }
 
    /**
     * returns the queueurl for for sqs queue if you pass in a name
     * @param queueName
     * @return
     */
    public static String getQueueUrl(String queueName){
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        return sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
    }
 
    /**
     * lists all your queue.
     * @return
     */
    public static ListQueuesResult listQueues(){
       return sqs.listQueues();
    }
 
    /**
     * send a single message to your sqs queue
     * @param queueUrl
     * @param message
     */
    public static void sendMessageToQueue(String queueUrl, String message){
        SendMessageResult messageResult =  sqs.sendMessage(new SendMessageRequest(queueUrl, message));
        //System.out.println(messageResult.toString());
    }
 
    /**
     * gets messages from your queue
     * @param queueUrl
     * @return
     */
    public static List<Message> getMessagesFromQueue(String queueUrl){
       ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
       List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
       return messages;
    }
 
    /**
     * deletes a single message from your queue.
     * @param queueUrl
     * @param message
     */
    public static void deleteMessageFromQueue(String queueUrl, Message message){
        String messageRecieptHandle = message.getReceiptHandle();
        //System.out.println("message deleted : " + message.getBody() + "." + message.getReceiptHandle());
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));
    }
}
