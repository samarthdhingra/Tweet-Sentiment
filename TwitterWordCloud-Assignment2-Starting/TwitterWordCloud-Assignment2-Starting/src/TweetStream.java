

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
public class TweetStream extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TweetStream() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
    	PrintWriter writer = response.getWriter();
    	for (int i = 0; i < 1000; i++) {
    		Twit twit = TweetGet.getCurrentTwit();
    		while(twit == null) {
    			//start the sampling
    			System.out.println("Sampling");
    			TweetGet.TwitterExample();
    			try {
    				Thread.sleep(5000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			twit = TweetGet.getCurrentTwit();
    		}
    		System.out.println(twit.keyword);
    		String tweetString = twit.id + "^" + twit.screenName + "^" + twit.latitude + "^" + twit.longitude + "^" + twit.content;
    		writer.write("data: "+ tweetString +"\n\n");
    		writer.flush();
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	writer.close();
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
