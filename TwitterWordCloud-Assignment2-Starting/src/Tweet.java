

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * Servlet implementation class Tweet
 */
public class Tweet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tweet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
    	PrintWriter writer = response.getWriter();
    	for (int i = 0; i < 1000; i++) {
    		Twit twit = TweetGet.getCurrentTwit();
    		while(twit == null) { //code required to start the sampling
    			//start the sampling
    			TweetGet.TwitterExample();
    			try {
    				Thread.sleep(2000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			twit = TweetGet.getCurrentTwit();
    		}
    		/*
    		System.out.println(twit.keyword);
    		String tweetString = twit.id + "^" + twit.screenName + "^" + twit.latitude + "^" + twit.longitude + "^" + twit.content;
    		writer.write("data: "+ tweetString +"\n\n");
    		writer.flush();
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
    		//Assignment 2 code
    		List<Message> messages =  SimpleQueueServiceSample.getMessagesFromQueue(SimpleQueueServiceSample.processedQueueURL);
            if(messages == null || messages.size() == 0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }else{
                //flag = false;
            	//System.out.println("Messages = " + messages.size());
                for (Message message : messages) {
                    String messageTweet = message.getBody();
                    SNSMessage snsMessage = readMessageFromJson(messageTweet);
                    String concatenatedTweet = snsMessage.getMessage();
                    System.out.println("Tweet = " + concatenatedTweet);
                    //Map<String, String> map = DatabaseWrapper.getPositiveTweetsStat();
            	    //concatenatedTweet = concatenatedTweet +"^^*^^" + map.get("positive") + "^^*^^" + map.get("total") ;
            		
                    writer.write("data: "+ concatenatedTweet +"\n\n");
            		writer.flush();
            		try {
        				Thread.sleep(1000);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}
                    //System.out.println("photo to be processed : " + messageTweet);
                    //Twit t = new Twit(messageTweet);
                    //Runnable worker = new WorkerThread(t);
                    //executor.execute(worker);
                    //StringTokenizer photoTokenizer = new StringTokenizer(messagePhoto,",");
                }
                //Delete message after processing
                for (Message message : messages) {
                	SimpleQueueServiceSample.deleteMessageFromQueue(SimpleQueueServiceSample.processedQueueURL, message);
                }
                //
                //executor.shutdown();
                //while (!executor.isTerminated()) {
                //}
                //System.out.println("Finished all threads");
            }
    	}
    	writer.close();
    	
	}
    
    private SNSMessage readMessageFromJson(String string) {
		ObjectMapper mapper = new ObjectMapper(); 
		SNSMessage message = null;
		try {
			message = mapper.readValue(string, SNSMessage.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Map<String, Object> mapOutput = new HashMap<String, Object>();
		
		ArrayList<TweetSentiment> tweetList = new ArrayList<>();
		
		/*tweetList.add(new TweetSentiment("spectacular",2,"date"));
		tweetList.add(new TweetSentiment("attack",-10,"date"));
		tweetList.add(new TweetSentiment("inspiring",15,"date"));
		tweetList.add(new TweetSentiment("condemn",-50,"date")); */
		
		String[] keywords = new String[]{"aamaadmi", "modi", "russia", "healthcare", "india", "elections", "obama", "politics", "democracy", "liberal", "participation", "illegal", "devolution", "trustee", "mandate", "manifesto", "microcosm", "proportional", "tactical", "parliament", "reform", "partisanship", "landslide", "capitalist", "paternalism", "consensus", "economics", "propaganda", "welfare", "sectional", "sanctions"};	
				
		Map<String, Integer> mapPositive = new HashMap<String, Integer>();
		Map<String, Integer> mapNegative = new HashMap<String, Integer>();
		
		BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Samarth\\Desktop\\CloudComputing\\Tweet3\\output_3"));
        String line = "";
        while ((line = in.readLine()) != null) {
            String parts[] = line.split("\t");
            Integer parts1Integer = Integer.valueOf(parts[1]);
            for (String keyword : keywords)
            {
            	if ((parts[0].toLowerCase()).contains(keyword))
            	{
            		 if (parts1Integer.intValue() >= 0)
                     {
            			if (mapNegative.get(keyword) == null)
            			{
            				if (mapPositive.get(keyword) == null)
                			{
                				mapPositive.put(keyword, parts1Integer);
                			}
                			else
                			{
                				mapPositive.put(keyword, mapPositive.get(keyword) + parts1Integer);
                			}
            			}
                     }
                     else 
                     {
                    	if (mapPositive.get(keyword) == null)
             			{
                    		if (mapNegative.get(keyword) == null)
                 			{
                        		mapNegative.put(keyword, parts1Integer);
                 			}
                 			else
                 			{
                 				mapNegative.put(keyword, mapNegative.get(keyword) + parts1Integer);
                 			}
             			}
                     }
            	}
            }
              
        }
        in.close();
        
        // Sort the hashmap .
        Map<String, Integer> sortedMapPositive = sortByValue( mapPositive );
        
        // Sort the hashmap .
        Map<String, Integer> sortedMapNegative = sortByValue( mapNegative );
        
        // Get top 10 positive and top 10 negative
        int positiveCount = 0;
        int negativeCount = 0;
        for (String mapKey : sortedMapNegative.keySet())
        {
        	negativeCount++;
        	if (negativeCount > 15)
        	{
        		break;
        	}
        	Integer countInteger = sortedMapNegative.get(mapKey);
        	int i = countInteger.intValue();
        	tweetList.add(new TweetSentiment(mapKey, i, "date"));
        }
        
        
        ArrayList<String> positiveKeys = new ArrayList<String>(sortedMapPositive.keySet());
        for(int i=positiveKeys.size()-1; i>=0;i--){
            //System.out.println(sortedMapPositive.get(positiveKeys));
            positiveCount++;
        	if (positiveCount > 20)
        	{
        		break;
        	}
        	Integer countInteger = sortedMapPositive.get(positiveKeys.get(i));
        	int intPrimitive = countInteger.intValue();
        	tweetList.add(new TweetSentiment(positiveKeys.get(i), intPrimitive, "date"));
        }
        

		for(int i = 0; i<tweetList.size(); i++ ) {
			mapOutput.put(String.valueOf(i), tweetList.get(i));
		}
		write(response, mapOutput);
	}
	
	
	
	
	private void write(HttpServletResponse response, Map<String, Object> map) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(map));
	}
	
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        @Override
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
}
