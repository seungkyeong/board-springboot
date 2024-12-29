package aws;

import java.net.URL;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@Service
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3;
    private static final String BUCKET_NAME = "demofille";

    // 프리사인드 URL 생성
    public URL generatePresignedUrl(String objectKey) {
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);  // 1시간 후 만료

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, objectKey)
                .withMethod(com.amazonaws.HttpMethod.PUT)  //파일 업로드
                .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }
}