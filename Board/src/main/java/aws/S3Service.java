package aws;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;

import constant.ExceptionConstant;
import dto.ResponseDTO;
import exceptionHandle.GeneralException;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.MultiObjectDeleteException.DeleteError;

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
    
    // 프리사인드 URL 생성
    public ResponseDTO<Object> deleteFiles(List<String> fileKeys) {
    	// URL에서 S3 Key (파일명)만 추출
        List<KeyVersion> keys = fileKeys.stream()
                .map(url -> new KeyVersion(url.replace("https://demofille.s3.ap-northeast-2.amazonaws.com/", ""))) 
                .collect(Collectors.toList());
        
    	// S3에서 다중 삭제 요청 생성
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(BUCKET_NAME).withKeys(keys);

        try {
        	// 삭제 실행
        	amazonS3.deleteObjects(deleteObjectsRequest);
        }catch(Exception e) {
        	throw new GeneralException(ExceptionConstant.OPERATION.getCode(), e.getMessage());
        }
        return new ResponseDTO<>();
    }
}