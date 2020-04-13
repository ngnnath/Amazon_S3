package S3Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * S3Properties.
 */
@Component
@PropertySource("classpath:application.properties")
public class S3Properties{

    @Value("${minio.bucket.name}")
    private String bucketname;
    @Value("${minio.access.key}")
    private String accessKey;
    @Value("${minio.access.secret}")
    private String accessSecret;
    @Value("${minio.url}")
    private String url;
    @Value("${key.name}")
    private static String keyName ;
    @Value("${upload.file.name}")
    private static String uploadFileName;

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static String getKeyName() {
        return keyName;
    }

    public static void setKeyName(String keyName) {
        S3Properties.keyName = keyName;
    }

    public static String getUploadFileName() {
        return uploadFileName;
    }

    public static void setUploadFileName(String uploadFileName) {
        S3Properties.uploadFileName = uploadFileName;
    }

}
