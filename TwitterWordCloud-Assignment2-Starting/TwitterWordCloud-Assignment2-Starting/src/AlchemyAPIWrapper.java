
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class AlchemyAPIWrapper {
	
			private String _apiKey = "286e33caaf4f9088db14757da69b952a002ab100";
			//private String _apiKey = "c880627aae204cd0b783f043ad2bc08742636047";
			private String _requestUri = "http://access.alchemyapi.com/calls/";

			AlchemyAPIWrapper () {
				
			}
    		public Document TextGetTextSentiment(String text)
    				throws IOException, SAXException,
    				ParserConfigurationException, XPathExpressionException
    		{
    			return TextGetTextSentiment(text, new AlchemyAPI_Params());
    		}
        
    		public Document TextGetTextSentiment(String text, AlchemyAPI_Params params)
    				throws IOException, SAXException, ParserConfigurationException, XPathExpressionException 
    		{
    			//CheckText(text); 		
    			params.setText(text);
    			String[] customParameters = {"sentiment","1","showSourceText","1"};
    			params.setCustomParameters(customParameters);
    			//System.out.println("text = " + text);
    			//params.setOutputMode("json");
    			return POST("TextGetTextSentiment", "text", params);
    			//return POST("TextGetTextSentiment", "text", params);
    		}
    
    		
    		private void CheckText(String text) {
    				if (null == text || text.length() < 5)
    					throw new IllegalArgumentException("Enter some text to analyze.");
    		}
    
    		private Document GET(String callName, String callPrefix, AlchemyAPI_Params params)
    	    throws IOException, SAXException,
    	           ParserConfigurationException, XPathExpressionException
    		{
    		    StringBuilder uri = new StringBuilder();
    		    uri.append(_requestUri).append(callPrefix).append('/').append(callName)
    		       .append('?').append("apikey=").append(this._apiKey);
    		    uri.append(params.getParameterString());
    		
    		    URL url = new URL(uri.toString());
    		    //System.out.println("URL = " + url);
    		    HttpURLConnection handle = (HttpURLConnection) url.openConnection();
    		    handle.setDoOutput(true);
    		    //System.out.println("Handle = " + handle.toString());
    		    return doRequest(handle, params.getOutputMode());
    		}
    		

    	    private Document POST(String callName, String callPrefix, AlchemyAPI_Params params)
    	        throws IOException, SAXException,
    	               ParserConfigurationException, XPathExpressionException
    	    {
    	        URL url = new URL(_requestUri + callPrefix + "/" + callName);
    	        //System.out.println("URL = " + url);
    	        HttpURLConnection handle = (HttpURLConnection) url.openConnection();
    	        handle.setDoOutput(true);
    	        
    	        StringBuilder data = new StringBuilder();

    	        data.append("apikey=").append(this._apiKey);
    	        data.append(params.getParameterString());
    	        //System.out.println("Data = " + data.toString());
    	        handle.addRequestProperty("Content-Length", Integer.toString(data.length()));

    	        DataOutputStream ostream = new DataOutputStream(handle.getOutputStream());
    	        ostream.write(data.toString().getBytes());
    	        ostream.close();
    	  
    	        
    	        return doRequest(handle, params.getOutputMode());
    	    }
    	    
    	    /*
    	    static String convertStreamToString(java.io.InputStream is) {
    	        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    	        return s.hasNext() ? s.next() : "";
    	    }
    	    */

    	    private Document doRequest(HttpURLConnection handle, String outputMode)
    	        throws IOException, SAXException,
    	               ParserConfigurationException, XPathExpressionException
    	    {
    	        DataInputStream istream = new DataInputStream(handle.getInputStream());
    	        //System.out.println("Stream = " + convertStreamToString(istream));
    	        //System.out.println("Response code = " + handle.getResponseCode());
    	        //Document doc = null;
    	        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(istream);

    	        istream.close();
    	        handle.disconnect();
    	        
    	        XPathFactory factory = XPathFactory.newInstance();

    	        if(AlchemyAPI_Params.OUTPUT_XML.equals(outputMode)) {
    	        	String statusStr = getNodeValue(factory, doc, "/results/status/text()");
    	        	if (null == statusStr || !statusStr.equals("OK")) {
    	        		String statusInfoStr = getNodeValue(factory, doc, "/results/statusInfo/text()");
    	        		if (null != statusInfoStr && statusInfoStr.length() > 0)
    	        			throw new IOException("Error making API call: " + statusInfoStr + '.');

    	        		throw new IOException("Error making API call: " + statusStr + '.');
    	        	}
    	        }
    	        else if(AlchemyAPI_Params.OUTPUT_RDF.equals(outputMode)) {
    	        	String statusStr = getNodeValue(factory, doc, "//RDF/Description/ResultStatus/text()");
    	        	if (null == statusStr || !statusStr.equals("OK")) {
    	        		String statusInfoStr = getNodeValue(factory, doc, "//RDF/Description/ResultStatus/text()");
    	        		if (null != statusInfoStr && statusInfoStr.length() > 0)
    	        			throw new IOException("Error making API call: " + statusInfoStr + '.');

    	        		throw new IOException("Error making API call: " + statusStr + '.');
    	        	}
    	        }
    	        
    	        return doc;
    	    }

    	    private String getNodeValue(XPathFactory factory, Document doc, String xpathStr)
    	        throws XPathExpressionException
    	    {
    	        XPath xpath = factory.newXPath();
    	        XPathExpression expr = xpath.compile(xpathStr);
    	        Object result = expr.evaluate(doc, XPathConstants.NODESET);
    	        NodeList results = (NodeList) result;

    	        if (results.getLength() > 0 && null != results.item(0))
    	            return results.item(0).getNodeValue();

    	        return null;
    	    }
}
