

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
public class TweetDate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TweetDate() {
        super();
        // TODO Auto-generated constructor stub
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
		
			
				
		Map<String, Integer> mapPositive = new HashMap<String, Integer>();
		Map<String, Integer> mapNegative = new HashMap<String, Integer>();
		
		BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Samarth\\Desktop\\CloudComputing\\Tweet3\\sentiments_date_output.txt"));
        String line = "";
        while ((line = in.readLine()) != null) {
            String parts[] = line.split("\t");
            
            String date = parts[0];
           
            Integer year = Integer.valueOf(Character.toString(date.charAt(0)) +
            		Character.toString(date.charAt(1)) +
            		Character.toString(date.charAt(2)) +
            		Character.toString(date.charAt(3)) );
            		      
            
            Integer month = Integer.valueOf(Character.toString(date.charAt(4)) +
            		Character.toString(date.charAt(5)) );
            		
            
            Integer day = Integer.valueOf(Character.toString(date.charAt(6)) +
            		Character.toString(date.charAt(7)) );
            		
            
            Integer positive_count = Integer.valueOf();
            
            
              
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
