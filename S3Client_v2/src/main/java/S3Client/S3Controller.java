package S3Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import javax.json.Json;
import javax.json.JsonObject;
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
    public ResponseEntity<String> putFile(@RequestParam("path") String filepath) throws IOException, URISyntaxException {
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();
        String keyName = "newFile.txt";
        String uploadFileName = filepath;

        System.out.println("Uploading a new object to S3 from a file\n");
        File file = new File(uploadFileName);
        byte[] content = "content".getBytes();
        // Upload file
        s3.putObject(PutObjectRequest.builder().bucket(app.getBucketname()).key(keyName)
                .build(), RequestBody.fromFile(file));


        // Download file
        GetObjectResponse reponse = s3.getObject(GetObjectRequest.builder().bucket(app.getBucketname()).key(keyName).build(),
                ResponseTransformer.toOutputStream(new ByteArrayOutputStream()));
        if (reponse.sdkHttpResponse().statusCode() == HttpStatus.OK.value()) {
            JsonObject object = Json.createObjectBuilder().add("status", HttpStatus.OK.value()).add("body","Fichier ajouté").build();
            return ResponseEntity.status(HttpStatus.OK).body(object.toString());
        }else{
            JsonObject object = Json.createObjectBuilder().add("status", HttpStatus.OK.value()).add("body", String.valueOf(reponse.sdkHttpResponse().statusText())).build();
            reponse.sdkHttpResponse().statusCode();
            return ResponseEntity.status(HttpStatus.OK).body(object.toString());
        }
    }

    /**
     * Get the file
     * @param key clé de l'objet à récupérer
     * @throws IOException error in DisplayTextStream
     * @return
     */
    @RequestMapping(value = "/get/file", method = GET, produces = "application/json")
    public ResponseEntity<String> getFile(@RequestParam("key") String key) throws URISyntaxException, IOException {
        String result = "";
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();
        ResponseInputStream<GetObjectResponse> s3objectResponse = s3.getObject(GetObjectRequest.builder().bucket(app.getBucketname()).key(key).build());
        displayTextInputStream(s3objectResponse);
        JsonObject object = Json.createObjectBuilder().add("status", HttpStatus.OK.value()).add("body","get it").build();

        return ResponseEntity.status(HttpStatus.OK).body(object.toString());
    }

    @RequestMapping(value = "/delete/file", method = GET)
    public ResponseEntity<String> deleteFile(@RequestParam("key") String key) throws URISyntaxException {
        JsonObject object;
        Region region = Region.US_EAST_1;
        URI uri = new URI(app.getUrl());
        S3Client s3 = S3Client.builder().endpointOverride(uri).region(region).credentialsProvider(StaticCredentialsProvider.create(this.awsCreds)).build();
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(app.getBucketname()).key(key).build();

        DeleteObjectResponse response = s3.deleteObject(deleteObjectRequest);
        if (response.sdkHttpResponse().statusCode() == HttpStatus.NO_CONTENT.value()) {
            object = Json.createObjectBuilder().add("status", HttpStatus.OK.value()).add("body","Fichier supprimé").build();
        } else {
            object = Json.createObjectBuilder().add("status", HttpStatus.NO_CONTENT.value()).add("body","Aucun document").build();

        }
        return ResponseEntity.status(HttpStatus.OK).body(object.toString());
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
