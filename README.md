Hadoop tweet sentiment analysis
===============

Tweet Map 
- A Java Web App that does hadoop analysis of the tweets to analyze the sentiment of a particular topic over time.
 
Contributors :
- Abhyuday Polineni <ap3318@columbia.edu> , UNI : ap3318
- Samarth Dhingra <sd2900@columbia.edu> , UNI : sd2900
- Utkarsha Prakash <up2127@columbia.edu>, UNI : up2127
- Vishal Vyas <vnv2102@columbia.edu>, UNI : vnv2102 
 
 
Features of the Web App : 
   1.SENTIMENT ANALYSIS OF USING WORD CLOUD, BAR CHAR
      Using the frequecny and the sentiment value of the tweet keywords, we get a sense of the trending topics in twitter
      
   2. TIME VARIATION OF THE SENTIMENT SHOWN on a LINE GRAPH
   
   3. FETCHING RELATED TWEETS
      
   
   -----------------------FEATURES FROM ASSIGNMENT 1 AND 2 -------------------------------------------
    - 1.REAL TIME PRESENTATION OF THE TWEEETS ON A GOOGLE MAP USING SERVER SENT EVENTS. 
  1.1 Tweets are updated in real time with a time interval of 2 seconds with Server Sent Events used for updating the UI.
  1.2 Tweet details like ScreenName, content, time, lat, longitude are displayed by hovering the mouse over the markers (tweets) of the google map.
     
 
- 2.SENTIMENT ANALYSIS OF THE REAL TIME AS WELL AS STORED TWEETS , GAUGE METER AND WORD CLOUD
  2.1 Used the third party ALCHEMY API for sentiment analysis of the real time as well as stored tweets. 
  2.2 Sentiments are segregated into Positive (Green), Negative (Red) and Neutral (Yellow) and are displayed as Markers on the google Map
  2.3 Used a Gauge Meter for the sentiment analysis that is updated in real time on every new tweet to reflect positive/negative sentiments.
  2.4 Built a WORD CLOUD using d3.js that visualizes the frequency of the tweet keywords. This gives a sense of the trending topics in twitter
   
  
    - 3.CLUSTERING OF THE STORED TWEETS (IN AMAZON RDS DB) ON A GOOGLE MAP 
  3.1 HEATMAP shows the density of the tweets on the google map, based on the location where the people are tweeting the most from.
  3.2 Clustering of tweets is also shown using google markers with one marker per group of tweets in same location.
  3.3 Clustering based on the Latitude and Longitude showing the count and the sentiment (Positive, Negative and Neutral) of the clusterd tweets.
  3.4 User can filter the tweets based on some predefined keywords based on recent world events like (Obama, Narendra Modi, Ebola)
 
    - 4.CLOUD TECHNOLOGIES USED 
       4.1 This web application is deployed programmatically using Amazon Elastic BeanStalk java AWS api, which automatically creates 
   a load balancer, an auto scaling launch configuration and a cloudwatch alarm.
  4.2 Used Amazon SQS (Simple Queue Service) as a queue service for asynchronous processing of the tweets.
  4.3 Separate Worker Pool threads process the tweet text using the Alchemy API for sentiment analysis
  4.4 Used Amazon SNS (Simple Notification Service) that sends a notification to the User Interface to update the UI and display the processed information (sentiment) about the tweet.
  
- 5. Technical details - Used Amazon SQS and SNS
When the user enters live streaming mode, then the twitter sampling starts. Unprocessed tweets are read into an SQS queue. A workerthreadManager is
then initialized, which spawns worker threads. Each worker thread reads a message from the queue. It then processes it, i.e. makes a call to the 
Alchemy API to get the sentiment. This processed tweet is then sent to an end point using Amazon from where it is sent to the client and the tweet is 
shown on the google map in real time.
 
Steps to test the App : 
   - Go to URL : 
   - The app may be down due to stopped Amazon EC2 servers (so that cost is not incurred unnecessarily), please email anyone of the below and we shall respond within a few hours.
    - Abhyuday Polineni <ap3318@columbia.edu> , UNI : ap3318
- Samarth Dhingra <sd2900@columbia.edu> , UNI : sd2900
- Utkarsha Prakash <up2127@columbia.edu>, UNI : up2127
- Vishal Vyas <vnv2102@columbia.edu>, UNI : vnv2102 
 
 
Steps to test the App on your own machine :
   1. Download the repository source code.
   2. Add all relevant library dependencies so that build is successful.
   3. Add the AWS credentials in the AWS credentials file.
   4. Export a 'war' of the web application code.
   5. Modify the EBSSetup.java to set the correct war file path and the AWS credentials file.
   6. Run EBSSetup.java to deploy the war on the AWS Elastic BeanStalk.
 
Library dependencies :
   - gson-2.2.2.jar
   - gson-2.2.2-javadoc.jar
   - gson-2.2.2-sources.jar
   - javax.servlet-api-3.0.1.jar
   - json-20141113.jar
   - mysql-connector-java-5.1.18-bin.jar
   - twitter4j-async-4.0.2.jar    
   - twitter4j-core-4.0.2.jar
   - twitter4j-examples-4.0.2.jar
   - twitter4j-media-support-4.0.2.jar
   - twitter4j-stream-4.0.2.jar
All the required jars are uploaded to the github repository.
