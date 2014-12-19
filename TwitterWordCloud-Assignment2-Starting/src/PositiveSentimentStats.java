

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TweetStream
 */
public class PositiveSentimentStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PositiveSentimentStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	String TOKENIZER = "^^*^^";
		//response.setContentType("text/plain");
    	response.setContentType("text/event-stream");
    	response.setCharacterEncoding("UTF-8");
    	PrintWriter writer = response.getWriter();
 
    		Map<String, String> map=DatabaseWrapper.getPositiveTweetsStat();
    		
    		String tweetString = map.get("positive") + TOKENIZER + map.get("total") ;
    		writer.write("data: "+ tweetString +"\n\n");
    		writer.flush();
    	 
    	writer.close();
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String TOKENIZER = "^^*^^";
		//response.setContentType("text/plain");
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
    	PrintWriter writer = response.getWriter();
 
    		Map<String, String> map=DatabaseWrapper.getPositiveTweetsStat();
    		
    		String tweetString = map.get("positive") + TOKENIZER + map.get("total") ;
    		writer.write("data: "+ tweetString +"\n\n");
    		writer.flush();
    	 
    	writer.close();
	}

}
