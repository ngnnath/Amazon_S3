package S3Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller S3
 */
@RestController
public class S3Controller {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    /**
     * Properties of S3.
     */
    private S3Properties app;

    /**
     * credentials.
     */
    private AwsSessionCredentials awsCreds;

    @Autowired
    public void setApp(S3Properties app) {
        this.app = app;
        this.awsCreds = AwsSessionCredentials.create(
                app.getAccessKey(),
                app.getAccessSecret(),
                "");
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

    @RequestMapping("/createbucket")
    public String createbucket(@RequestParam("name") String bucketname) throws URISyntaxException {
        String message = "";
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        // Create bucket
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder()
                .bucket(bucketname)
                .createBucketConfiguration(CreateBucketConfiguration.builder()
                        .locationConstraint(region.id())
                        .build())

                .build();
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();
        CreateBucketResponse result = s3.createBucket(createBucketRequest);
        if (result.sdkHttpResponse().statusCode() == HttpStatus.OK.value()) {
            message = "bucket crée";
        }
        return message;
    }

    /**
     * Put file on the bucket
     *
     * @param filepath file path
     * @return a string
     * @throws IOException exception
     */
    @RequestMapping(value = "/put/file", method = GET)
    public String putFile(@RequestParam("path") String filepath) throws IOException, URISyntaxException {
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();
        String keyName = "newFile.txt";
        String uploadFileName = filepath;
        String result = "";

        System.out.println("Uploading a new object to S3 from a file\n");
        File file = new File(uploadFileName);
        byte[] content = "content".getBytes();
        RequestBody requestBody = RequestBody.fromBytes(content);
        // Upload file
        s3.putObject(PutObjectRequest.builder().bucket(app.getBucketname()).key(keyName)
                .build(), RequestBody.fromFile(file));

        // Download file
        GetObjectResponse reponse = s3.getObject(GetObjectRequest.builder().bucket(app.getBucketname()).key(keyName).build(),
                ResponseTransformer.toOutputStream(new ByteArrayOutputStream()));
        if (reponse.sdkHttpResponse().statusCode() == HttpStatus.OK.value()) {
            result = "Fichier ajouté";
        }
        return result;
    }

    /**
     * Get the file
     * @param key clé de l'objet à récupérer
     * @throws IOException error in DisplayTextStream
     * @return
     */
    @RequestMapping(value = "/get/file", method = GET)
    public Response getFile(@RequestParam("key") String key) throws URISyntaxException, IOException {
        String result = "";
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();


        ResponseInputStream<GetObjectResponse> s3objectResponse = s3.getObject(GetObjectRequest.builder().bucket(app.getBucketname()).key(key).build());
        displayTextInputStream(s3objectResponse);

        String message = "{\"hello\": \"This is a JSON response\"}";

        return Response
                .status(Response.Status.OK)
                .entity(message)
                .type(String.valueOf(MediaType.APPLICATION_JSON))
                .build();
        //return result;
    }

    @RequestMapping(value = "/delete/file", method = GET)
    public String deleteFile(@RequestParam("key") String key) throws URISyntaxException {
        String result = "";
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(app.getBucketname()).key(key).build();

        DeleteObjectResponse response = s3.deleteObject(deleteObjectRequest);
        if (response.sdkHttpResponse().statusCode() == HttpStatus.NO_CONTENT.value()) {
            result = "Delete Done";
        } else {
            result = response.sdkHttpResponse().statusCode() +" "+response.sdkHttpResponse().isSuccessful();
        }
        return result;
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
