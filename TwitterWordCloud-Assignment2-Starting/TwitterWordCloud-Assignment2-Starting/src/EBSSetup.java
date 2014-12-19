import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient;
import com.amazonaws.services.elasticbeanstalk.model.CheckDNSAvailabilityRequest;
import com.amazonaws.services.elasticbeanstalk.model.CheckDNSAvailabilityResult;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationResult;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionResult;
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentResult;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentsResult;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.amazonaws.services.elasticbeanstalk.model.ListAvailableSolutionStacksResult;
import com.amazonaws.services.elasticbeanstalk.model.S3Location;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
public class EBSSetup {

	// Defining constants below which won't change throughout the execution of the program
	private static final String CREDENTIALS_FILE = "AwsCredentials.properties";
	private static final String C_NAME_PREFIX = "ColumbiaTwitterApp13";
	private static final String APPLICATION_NAME = "TwitterAssignment1";
	private static final String APPLICATION_DESCRIPTION = "Analyzes and Displays tweets";
	private static final String APPLICATION_VERSION_LABEL = "TwitterAssignment1VersionLabel1";
	
	private static final String ENVIRONMENT_NAME = "TwitterAppEnv2";
	private static final String UPLOAD_FILE_NAME = "C:\\Users\\Vighnu\\workspace\\TwitterWebApp\\TwitterWebApp.war";
    private static final String SOLUTION_STACK = "64bit Amazon Linux 2014.09 v1.0.9 running Tomcat 7 Java 7";
	
	
	static AmazonEC2 ec2;
	static String instanceId;
	
	static AWSElasticBeanstalkClient EBSClient;
	
	static AmazonS3Client s3Client;


	public static void main(String[] args) throws Exception {
		
		
		AWSCredentials credentials = null;
		// Load the AWS credentials file
		try {
			credentials = new PropertiesCredentials(
					EBSSetup.class.getResourceAsStream(CREDENTIALS_FILE));
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that the location of the credentials file is correct ",
					e);
		}		
		
		try
		{
			// Create Amazon Elastic Bean Stalk Client
			System.out.println("Creating Elastic BeanStalk Client");
			EBSClient = new AWSElasticBeanstalkClient(credentials);
			
			// Create new S3 client
			System.out.println("Creating S3 Client");
			s3Client = new AmazonS3Client();
			
			// Check for DNS Name availability
			String cNamePrefix = C_NAME_PREFIX;
			System.out.println("Checking for DNS Name availability");
			CheckDNSAvailabilityRequest dnsAvailRequest  = new CheckDNSAvailabilityRequest(cNamePrefix);
			CheckDNSAvailabilityResult dnsAvailResult = EBSClient.checkDNSAvailability(dnsAvailRequest);
			Boolean isAvailable = dnsAvailResult.isAvailable();
			System.out.println(isAvailable);
			String fullyQualCName = dnsAvailResult.getFullyQualifiedCNAME();
			
			// Create Application Request
			System.out.println("Creating Application Request");
			CreateApplicationRequest appRequest = new CreateApplicationRequest(APPLICATION_NAME);
			appRequest.withDescription(APPLICATION_DESCRIPTION);
			CreateApplicationResult appResult = EBSClient.createApplication(appRequest);
			
			// Upload war file into S3 bucket
		    // Create a new bucket
			System.out.println("Uploading " + UPLOAD_FILE_NAME +" war file to S3\n");
			String bucketName = EBSClient.createStorageLocation().getS3Bucket();
			String uploadFileName = UPLOAD_FILE_NAME;
			String keyName = URLEncoder.encode(uploadFileName);
	        File file = new File(uploadFileName);
	        s3Client.putObject(new PutObjectRequest( bucketName, keyName, file));
	        System.out.println("Successfully uploaded war file to S3 bucket");
	        
	        
	        System.out.println("Creating application version request");
	        // Creating application request
	        CreateApplicationVersionRequest appVersionRequest = new CreateApplicationVersionRequest(APPLICATION_NAME, APPLICATION_VERSION_LABEL);
	        appVersionRequest.withAutoCreateApplication(true)
	                         .withSourceBundle(new S3Location(bucketName, keyName));
	        CreateApplicationVersionResult appVersionResult = EBSClient.createApplicationVersion(appVersionRequest); 
	        
	        
	        System.out.println("Creating Environment request");
	        CreateEnvironmentRequest envRequest = new CreateEnvironmentRequest(APPLICATION_NAME, ENVIRONMENT_NAME);
	        envRequest.withVersionLabel(APPLICATION_VERSION_LABEL).withSolutionStackName(SOLUTION_STACK);
	        
	        ListAvailableSolutionStacksResult solutionsStack = EBSClient.listAvailableSolutionStacks();
	        
	        List<String> solutions = solutionsStack.getSolutionStacks();
	        for (String solution : solutions)
	        {
	        	System.out.println(solution);
	        }
	       
	        CreateEnvironmentResult envResult = EBSClient.createEnvironment(envRequest);
	        System.out.println("Successfully created the environment");
	        
	        String endURL = envResult.getEndpointURL();
	        System.out.println(endURL);
	        
	       
	        DescribeEnvironmentsResult	environments = EBSClient.describeEnvironments();
	        List<EnvironmentDescription> environmentDescs = environments.getEnvironments();
	        
	        System.out.println("Environment sizes " + environmentDescs.size());
	        for (EnvironmentDescription env : environmentDescs)
	        {
	        	System.out.println(env.getSolutionStackName());
	        }
	        System.out.println("Application Created");
		}
        catch (AmazonServiceException ase) {
			System.out.println("Caught Exception: " + ase.getMessage());
			System.out.println("Reponse Status Code: " + ase.getStatusCode());
			System.out.println("Error Code: " + ase.getErrorCode());
			System.out.println("Request ID: " + ase.getRequestId());
		}

	}
}
