

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
	
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedFilter = request.getParameter("filterByKeyword");
		String  displayMode= request.getParameter("displayMode");
		ArrayList<Twit> tweetList;
		if("Markers_Clustered".equalsIgnoreCase(displayMode)){
				int zoomLevel=1;
				tweetList = DatabaseWrapper.selectClusteredRecords("TweetStreamTable", selectedFilter,zoomLevel);
		}
		else{
				tweetList = DatabaseWrapper.selectRecord("TweetStreamTable", selectedFilter);
		}
		for(int i = 0; i<tweetList.size(); i++ ) {
			map.put(String.valueOf(i), tweetList.get(i));
		}
		write(response, map);
	}
	private void write(HttpServletResponse response, Map<String, Object> map) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(map));
	}
}
