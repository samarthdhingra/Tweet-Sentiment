import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class WorkerThread implements Runnable {

    private Twit twit;
    
    public WorkerThread(Twit t){
        this.twit = t;
    }

    @Override
    public void run() {
    	AlchemyAPIWrapper wrapper = new AlchemyAPIWrapper();
  
        	try {
			Document doc = wrapper.TextGetTextSentiment(this.twit.content);
			
			NodeList nList = doc.getElementsByTagName("docSentiment");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				 
				Node nNode = nList.item(temp);
		 
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					//System.out.println("Type : " + eElement.getElementsByTagName("type").item(0).getTextContent());
					this.twit.sentiment = eElement.getElementsByTagName("type").item(0).getTextContent();
					/*
					if(eElement.getElementsByTagName("score") != null && eElement.getElementsByTagName("score").item(0) != null) {
						System.out.println("Score : " + eElement.getElementsByTagName("score").item(0).getTextContent());
					}
					*/
				}
			}
			}
			catch(Exception e){
				System.out.println("Daily Limit for Alchemy exceeded :");
				this.twit.sentiment = "Positive";
			}
        	try{
				System.out.println("Constructed tweet = " + this.twit.toString());
				//---remove this in last minite
				//DatabaseWrapper.insertTweet(TweetGet.TweetStreamTable, this.twit);
				//publish to an SNS topic
				String msg = twit.toString();
				PublishRequest publishRequest = new PublishRequest(SimpleQueueServiceSample.topicARN, msg);
				PublishResult publishResult = SimpleQueueServiceSample.snsClient.publish(publishRequest);
				//print MessageId of message published to SNS topic
				//System.out.println("MessageId - " + publishResult.getMessageId());
        	} 
        	catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
        	}
    }

    /*
    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */

    @Override
    public String toString(){
        return this.twit.toString();
    }
}