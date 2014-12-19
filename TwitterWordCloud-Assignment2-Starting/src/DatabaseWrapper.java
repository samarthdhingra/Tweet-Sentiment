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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DomainMetadataRequest;
import com.amazonaws.services.simpledb.model.DomainMetadataResult;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;

/**
 * Welcome to your new AWS Java SDK based project!
 *
 * This class is meant as a starting point for your console-based application that
 * makes one or more calls to the AWS services supported by the Java SDK, such as EC2,
 * SimpleDB, and S3.
 *
 * In order to use the services in this sample, you need:
 *
 *  - A valid Amazon Web Services account. You can register for AWS at:
 *       https://aws-portal.amazon.com/gp/aws/developer/registration/index.html
 *
 *  - Your account's Access Key ID and Secret Access Key:
 *       http://aws.amazon.com/security-credentials
 *
 *  - A subscription to Amazon EC2. You can sign up for EC2 at:
 *       http://aws.amazon.com/ec2/
 *
 *  - A subscription to Amazon SimpleDB. You can sign up for Simple DB at:
 *       http://aws.amazon.com/simpledb/
 *
 *  - A subscription to Amazon S3. You can sign up for S3 at:
 *       http://aws.amazon.com/s3/
 */
public class DatabaseWrapper {

    /*
     * WANRNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    static AmazonEC2      ec2;
    static AmazonS3       s3;
    static AmazonSimpleDB sdb;
    public static Connection connection;
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://mydb.c0oi6q4ph6mi.us-west-2.rds.amazonaws.com:3306/";
    static final String DB_NAME = "mydatabase";
    //  Database credentials
    static final String USER = "awsuser";
    static final String PASS = "mypassword";
 
    /**
     * RDS Database Functions
     * @param args
     */
    public static void main(String[] args) {
    	/* Assignment 1 Dead code
    	createTable("TwitData");
    	ArrayList<Twit> tList = TweetGet.readJSON("Obama");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("Bieber");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("America");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("India");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("Narendra Modi");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("Ebola");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("Columbia");
    	insertRecord("TwitData", tList);
    	tList = TweetGet.readJSON("New York");
    	insertRecord("TwitData", tList);
    	selectRecord("TwitData", "Obama");
    	//deleteTable("TwitData");
    	 */
    }
    
    public static void createTable(String tableName) {
    	   Connection conn = null;
    	   Statement stmt = null;
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver");

    	      //STEP 3: Open a connection
    	      System.out.println("Connecting to a selected database...");
    	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
    	      System.out.println("Connected database successfully...");
    	      stmt = conn.createStatement();
    	      
    	      String sql = "CREATE TABLE " + tableName + " " +
                      "(id BIGINT not NULL, " +
                      " latitude DOUBLE, " + 
                      " longitude DOUBLE, " +
                      " keyword VARCHAR(255), " + 
                      " screenName VARCHAR(255), " + 
                      " time VARCHAR(255), " +
                      " sentiment VARCHAR(255), " +
                      " content BLOB, " +  
                      " PRIMARY KEY ( id ))"; 

    	      stmt.executeUpdate(sql);
    	      System.out.println("Created table in given database...");

    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	      try{
    	         if(stmt!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	      }// do nothing
    	      try{
    	         if(conn!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	         se.printStackTrace();
    	      }//end finally try
    	   }//end try
    	   System.out.println("Goodbye!");
    	}//end main
    
    public static void insertTweet(String tableName, Twit twit) {
  	   Connection conn = null;
  	   Statement stmt = null;
  	   try{
  	      //STEP 2: Register JDBC driver
  	      Class.forName("com.mysql.jdbc.Driver");

  	      //STEP 3: Open a connection
  	      //System.out.println("Connecting to a selected database...");
  	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
  	      //System.out.println("Connected database successfully...");
  	      
  	      //STEP 4: Execute a query
  	      //System.out.println("Inserting records into the table...");
  	      stmt = conn.createStatement();
  	      PreparedStatement st;
  	      st = conn.prepareStatement("insert into " + tableName + " (id,latitude,longitude,keyword, screenName, time, sentiment, content) values (?,?,?,?,?,?,?,?);");
  	      //now you bind the data to your parameters
  	      st.setLong(1, twit.id);
  	      st.setDouble(2, twit.latitude);
  	      st.setDouble(3, twit.longitude);
  	      st.setNString(4, twit.keyword);
  	      st.setNString(5, twit.screenName);
  	      st.setNString(6, twit.time);
  	      st.setNString(7, twit.sentiment);
  	      st.setNString(8, twit.content);
  	      //and then you can execute it
  	      st.executeUpdate();
  	      //System.out.println("Inserted records into the table...");

  	   }catch(SQLException se){
  	      //Handle errors for JDBC
  	      se.printStackTrace();
  	   }catch(Exception e){
  	      //Handle errors for Class.forName
  	      e.printStackTrace();
  	   }finally{
  	      //finally block used to close resources
  	      try{
  	         if(stmt!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	      }// do nothing
  	      try{
  	         if(conn!=null)
  	            conn.close();
  	      }catch(SQLException se){
  	         se.printStackTrace();
  	      }//end finally try
  	   }//end try
  	   System.out.println("Tweet inserted...");
  	   System.out.println("Goodbye!");
  	}
    
    public static void insertRecord(String tableName, ArrayList<Twit> tList) {
 	   Connection conn = null;
 	   Statement stmt = null;
 	   try{
 	      //STEP 2: Register JDBC driver
 	      Class.forName("com.mysql.jdbc.Driver");

 	      //STEP 3: Open a connection
 	      System.out.println("Connecting to a selected database...");
 	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
 	      System.out.println("Connected database successfully...");
 	      
 	      //STEP 4: Execute a query
 	      
 	      System.out.println("Inserting records into the table...");
 	      stmt = conn.createStatement();
 	      PreparedStatement st;
 	      String sql = "";
 	      Twit twit;
 	      for(int i=0; i< tList.size(); i++){
 	    	  twit = tList.get(i);
 	    	  //System.out.println(twit.id);
 	    	  //System.out.println(twit.content);
 	    	  //first you "prepare" your statement (where the '?' acts as a kind of placeholder)
 	    	  st = conn.prepareStatement("insert into " + tableName + " (id,latitude,longitude,keyword, screenName, content) values (?,?,?,?,?,?);");
 	    	  //now you bind the data to your parameters
 	    	  st.setLong(1, twit.id);
 	    	  st.setDouble(2, twit.latitude);
 	    	  st.setDouble(3, twit.longitude);
 	    	  st.setNString(4, twit.keyword);
 	    	  st.setNString(5, twit.screenName);
 	    	  st.setNString(6, twit.content);
 	    	  //and then you can execute it
 	    	  st.executeUpdate();
 	      }
 	      System.out.println("Inserted records into the table...");

 	   }catch(SQLException se){
 	      //Handle errors for JDBC
 	      se.printStackTrace();
 	   }catch(Exception e){
 	      //Handle errors for Class.forName
 	      e.printStackTrace();
 	   }finally{
 	      //finally block used to close resources
 	      try{
 	         if(stmt!=null)
 	            conn.close();
 	      }catch(SQLException se){
 	      }// do nothing
 	      try{
 	         if(conn!=null)
 	            conn.close();
 	      }catch(SQLException se){
 	         se.printStackTrace();
 	      }//end finally try
 	   }//end try
 	   System.out.println("Goodbye!");
 	}//end main
    
    public static void deleteRecord(String tableName, long id) {
    	   Connection conn = null;
    	   Statement stmt = null;
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver");

    	      //STEP 3: Open a connection
    	      System.out.println("Connecting to a selected database...");
    	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
    	      System.out.println("Connected database successfully...");
    	      
    	      //STEP 4: Execute a query
    	      System.out.println("Creating statement...");
    	      stmt = conn.createStatement();
    	      String sql = "DELETE FROM " + tableName + " " + 
    	                   "WHERE id = " + id;
    	      stmt.executeUpdate(sql);
    	      
    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	      try{
    	         if(stmt!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	      }// do nothing
    	      try{
    	         if(conn!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	         se.printStackTrace();
    	      }//end finally try
    	   }//end try
    	   System.out.println("Goodbye!");
    	}//end main
    
    public static void deleteAllRecords(String tableName) {
 	   Connection conn = null;
 	   Statement stmt = null;
 	   try{
 	      //STEP 2: Register JDBC driver
 	      Class.forName("com.mysql.jdbc.Driver");

 	      //STEP 3: Open a connection
 	      System.out.println("Connecting to a selected database...");
 	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
 	      System.out.println("Connected database successfully...");
 	      
 	      //STEP 4: Execute a query
 	      System.out.println("Creating statement...");
 	      stmt = conn.createStatement();
 	      String sql = "DELETE FROM " + tableName;
 	      stmt.executeUpdate(sql);
 	      
 	   }catch(SQLException se){
 	      //Handle errors for JDBC
 	      se.printStackTrace();
 	   }catch(Exception e){
 	      //Handle errors for Class.forName
 	      e.printStackTrace();
 	   }finally{
 	      //finally block used to close resources
 	      try{
 	         if(stmt!=null)
 	            conn.close();
 	      }catch(SQLException se){
 	      }// do nothing
 	      try{
 	         if(conn!=null)
 	            conn.close();
 	      }catch(SQLException se){
 	         se.printStackTrace();
 	      }//end finally try
 	   }//end try
 	   System.out.println("Goodbye!");
 	}//end main
 
    public static ArrayList<Twit>  selectClusteredRecords(String tableName, String filterKeyword,int zoom){
    	  Connection conn = null;
   	   Statement stmt = null;
   	   ArrayList<Twit> tList = new ArrayList<Twit>();
   	   try{
   	      //STEP 2: Register JDBC driver
   	      Class.forName("com.mysql.jdbc.Driver");

   	      //STEP 3: Open a connection
   	      System.out.println("Connecting to a selected database...");
   	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);

   	      System.out.println("Connected database successfully...");
   	      
   	      //STEP 4: Execute a query
   	      System.out.println("Creating statement...");
   	      stmt = conn.createStatement();

   	      String sql = "SELECT round(latitude,"+zoom+") as latitude, round(longitude,"+zoom+") as longitude, sentiment, count(*) as content FROM "+tableName ;
		
   	      if(!filterKeyword.equalsIgnoreCase("All")) {
   	    	  sql = sql + " WHERE keyword" + " = '" + filterKeyword +"'";
   	      }
   	      sql= sql + " group by round(latitude,"+zoom+"),round(longitude,"+zoom+"),sentiment ";
   	      
   	      //System.out.println(sql);
   	      ResultSet rs = stmt.executeQuery(sql);

   	      //STEP 5: Extract data from result set
   	      while(rs.next()){
   	         //Retrieve by column name
   	         long id  = 0;//rs.getLong("id");
   	         String keyword =  "".equals(filterKeyword)? filterKeyword :"ClusteredTweet";//rs.getString("keyword");
   	         String screenName = "ScreenName";//rs.getString("screenName");
  
   	         double latitude = rs.getDouble("latitude");
   	         double longitude = rs.getDouble("longitude");
   	         String time = (new Date()).toString(); //rs.getString("time");
   	         String sentiment = rs.getString("sentiment");
 	         String content = "Tweets : " + rs.getString("content")+ ", sentiment " + sentiment;
   	         Twit t = new Twit(screenName, content, latitude, longitude, keyword, id, time);
   	         t.sentiment = sentiment;
   	         tList.add(t);
   	         //Display values   
   	         System.out.print("===================Markers_Clustered by sentiments======================= ");
   	         System.out.print("keyword: " + keyword);
   	         System.out.print(", screenName: " + screenName);
   	         System.out.print(", content: " + content);
   	         System.out.println(", latitude: " + latitude);
   	         System.out.print(", longitude: " + longitude);
   	         System.out.print(", time: " + longitude);
   	         System.out.println(", id: " + id);
   	         System.out.println(", sentiment: " + sentiment);
   	      }
   	      rs.close();
   	   }catch(SQLException se){
   	      //Handle errors for JDBC
   	      se.printStackTrace();
   	   }catch(Exception e){
   	      //Handle errors for Class.forName
   	      e.printStackTrace();
   	   }finally{
   	      //finally block used to close resources
   	      try{
   	         if(stmt!=null)
   	            conn.close();
   	      }catch(SQLException se){
   	      }// do nothing
   	      try{
   	         if(conn!=null)
   	            conn.close();
   	      }catch(SQLException se){
   	         se.printStackTrace();
   	      }//end finally try
   	   }//end try
   	   System.out.println("Goodbye!");
   	   return tList;
    }
    public static ArrayList<Twit> selectRecord(String tableName, String filterKeyword) {
    	   Connection conn = null;
    	   Statement stmt = null;
    	   ArrayList<Twit> tList = new ArrayList<Twit>();
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver");

    	      //STEP 3: Open a connection
    	      System.out.println("Connecting to a selected database...");
    	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
    	      System.out.println("Connected database successfully...");
    	      
    	      //STEP 4: Execute a query
    	      System.out.println("Creating statement...");
    	      stmt = conn.createStatement();

    	      String sql = "SELECT id, latitude, longitude, keyword, screenName, time, sentiment, content FROM " + tableName;
    	      if(!filterKeyword.equalsIgnoreCase("All")) {
    	    	  sql = sql + " WHERE keyword" + " = '" + filterKeyword +"'";
    	      }
    	      //System.out.println(sql);
    	      ResultSet rs = stmt.executeQuery(sql);
 
    	      //STEP 5: Extract data from result set
    	      while(rs.next()){
    	         //Retrieve by column name
    	         long id  = rs.getLong("id");
    	         String keyword = rs.getString("keyword");
    	         String screenName = rs.getString("screenName");
    	         String content = rs.getString("content");
    	         double latitude = rs.getDouble("latitude");
    	         double longitude = rs.getDouble("longitude");
    	         String time = rs.getString("time");
    	         String sentiment = rs.getString("sentiment");
    	         Twit t = new Twit(screenName, content, latitude, longitude, keyword, id, time);
    	         t.sentiment = sentiment;
    	         tList.add(t);
    	         //Display values
    	         System.out.print("keyword: " + keyword);
    	         System.out.print(", screenName: " + screenName);
    	         System.out.print(", content: " + content);
    	         System.out.println(", latitude: " + latitude);
    	         System.out.print(", longitude: " + longitude);
    	         System.out.print(", time: " + longitude);
    	         System.out.println(", id: " + id);
    	         System.out.println(", sentiment: " + sentiment);
    	      }
    	      rs.close();
    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	      try{
    	         if(stmt!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	      }// do nothing
    	      try{
    	         if(conn!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	         se.printStackTrace();
    	      }//end finally try
    	   }//end try
    	   System.out.println("Goodbye!");
    	   return tList;
    	}//end main
    
    
    public static Map<String, String> getPositiveTweetsStat() {
    	   Connection conn = null;
    	   Statement stmt = null;
    	   Map<String,String> map = new HashMap<String,String>();
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver");

    	      //STEP 3: Open a connection
    	      System.out.println("Connecting to a selected database...");
    	      conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
    	      System.out.println("Connected database successfully...");
    	      
    	      //STEP 4: Execute a query
    	      System.out.println("Creating statement...");
    	      stmt = conn.createStatement();

    	      String sql = "select  sentiment, count(*)  as pos_cnt ,(select count(*) from TweetStreamTable) as total"+
    	    		  		" from TweetStreamTable  where sentiment in ('neutral','positive') ";
      	      //System.out.println(sql);
    	      ResultSet rs = stmt.executeQuery(sql);
 
    	      //STEP 5: Extract data from result set
    	      while(rs.next()){
    	         //Retrieve by column name
    	      
    	         String total = rs.getString("total");
    	         String pos_cnt = rs.getString("pos_cnt");
    	         map.put("positive",pos_cnt);
    	         map.put("total", total);
   
    	         //Display values
    	         System.out.print("total: " + total);
    	         System.out.print(", positive: " + pos_cnt);
    	      }
    	      rs.close();
    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	      try{
    	         if(stmt!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	      }// do nothing
    	      try{
    	         if(conn!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	         se.printStackTrace();
    	      }//end finally try
    	   }//end try
    	   System.out.println("Goodbye!");
    	   return map;
    	}//end main
    
    
    public static void deleteTable(String tableName) {
    	   Connection conn = null;
    	   Statement stmt = null;
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver");

    	      //STEP 3: Open a connection
    	      System.out.println("Connecting to a selected database...");
    	      conn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS);
    	      System.out.println("Connected database successfully...");
    	      
    	      //STEP 4: Execute a query
    	      System.out.println("Deleting table in given database...");
    	      stmt = conn.createStatement();
    	      
    	      String sql = "DROP TABLE " + tableName;
    	 
    	      stmt.executeUpdate(sql);
    	      System.out.println("Table  deleted in given database...");
    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	      try{
    	         if(stmt!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	      }// do nothing
    	      try{
    	         if(conn!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	         se.printStackTrace();
    	      }//end finally try
    	   }//end try
    	   System.out.println("Goodbye!");
    	}//end main
}
