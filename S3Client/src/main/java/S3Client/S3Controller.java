package S3Client;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller S3
 */
@Controller
public class S3Controller {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    /**
     * Properties of S3.
     */
    private S3Properties app;

    @Autowired
    public void setApp(S3Properties app) {
        this.app = app;
    }


    /**
     * show configuration
     *
     * @return return the configuration of S3
     */
    @RequestMapping("/configuration")
    public String configuration() {

        String appProperties = app.toString();
        logger.debug("Configuration {}", app);
        return app.toString();
    }

    /**
     * Put file on the bucket
     *
     * @param filepath file path
     * @return a string
     * @throws IOException exception
     */
    @RequestMapping(value = "/put/file", method = GET)
    public String putFile(@RequestParam("path") String filepath) throws IOException {
        String keyName = "newFile.txt";
        String uploadFileName = filepath;
        AWSCredentials credentials = new BasicAWSCredentials(app.getAccessKey(), app.getAccessSecret());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(app.getUrl(), Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            // Upload file
            s3Client.putObject(new PutObjectRequest(app.getBucketname(), keyName, file));

            // Download file
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(app.getBucketname(), keyName);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            System.out.println("Printing bytes retrieved:");
            displayTextInputStream(objectPortion.getObjectContent());
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response" + " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " + "means the client encountered " + "an internal error while trying to "
                    + "communicate with S3, " + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());

        }
        return "done";
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

}
