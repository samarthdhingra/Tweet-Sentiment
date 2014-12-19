Tweet Map 
	- A Java Web App that does geo-location analysis of real time as well as stored tweets .

Contributors :
	- Abhyuday Polineni <ap3318@columbia.edu> , UNI : ap3318
	- Samarth Dhingra <sd2900@columbia.edu> , UNI : sd2900

Features of the App : 
    - 1.Real time presentation of the tweets on a google map using Server Sent Events. 
	   1.1 Tweets are updated in real time with a time interval of 2 seconds.
	   1.2 Tweet details like ScreenName, content are displayed by hovering the mouse over the markers (tweets) of the google map.
	
	- 2.Representation of the stored tweets (in Amazon RDS DB) on a google map.
		- HEATMAP shows the density of the tweets on the google map, based on the location where the people are tweeting the most from.
		- Clustering of tweets is also shown using google markers with one marker per tweet.
		- User can filter the tweets based on some predefined keywords based on recent world events like (Obama, Narendra Modi, Ebola)
		
    - 3.This web application is deployed programmatically using Amazon Elastic BeanStalk java AWS api, which automatically creates 
	    a load balancer, an auto scaling launch configuration and a cloudwatch alarm.
		
Steps to test the App : 
   - Go to URL : 
   - The app may be down due to stopped Amazon EC2 servers (so that cost is not incurred unnecessarily), please email anyone of the below and we shall respond within a few hours.
     1. Abhyuday Polineni <ap3318@columbia.edu>
	 2. Samarth Dhingra <sd2900@columbia.edu>
	 
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
     gson-2.2.2-sources.jar
   - javax.servlet-api-3.0.1.jar
   - mysql-connector-java-5.1.18-bin.jar
   - twitter4j-async-4.0.2.jar	  	
   - twitter4j-core-4.0.2.jar
   - twitter4j-examples-4.0.2.jar
   - twitter4j-media-support-4.0.2.jar
   - twitter4j-stream-4.0.2.jar
All the required jars are uploaded to the github repository.
   
   
	