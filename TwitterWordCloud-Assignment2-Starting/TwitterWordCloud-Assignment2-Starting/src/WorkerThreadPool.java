import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.services.sqs.model.Message;

public class WorkerThreadPool implements Runnable{
 
	private String queueUrl;
	WorkerThreadPool(String queUrl) {
		this.queueUrl = queUrl;
	}
	
    @Override
    public void run() {
    	//SimpleQueueServiceSample awssqsUtil =   SimpleQueueServiceSample.getInstance();
        boolean flag = true;
        while(flag){
        	ExecutorService executor = Executors.newFixedThreadPool(5);
            List<Message> messages =  SimpleQueueServiceSample.getMessagesFromQueue(this.queueUrl);
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
                    //System.out.println("photo to be processed : " + messageTweet);
                    Twit t = new Twit(messageTweet);
                    Runnable worker = new WorkerThread(t);
                    executor.execute(worker);
                    //StringTokenizer photoTokenizer = new StringTokenizer(messagePhoto,",");
                }
                //Delete message after processing
                for (Message message : messages) {
                	SimpleQueueServiceSample.deleteMessageFromQueue(this.queueUrl, message);
                }
                //
                executor.shutdown();
                while (!executor.isTerminated()) {
                }
                System.out.println("Finished all threads");
            }
        }
    }
}