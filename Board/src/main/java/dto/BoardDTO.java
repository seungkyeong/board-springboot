package dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDTO extends RequestDTO{
    private String title;				//제목
    private String content;				//내용
    private long view = 0; 				//조회수
    private long likeCount = 0;         //좋아요 수 
    private String likeFlag;            //사용자의 좋아요 누른 판별
    
    private List<String> imgPath = new ArrayList<>(); 		//이미지 경로 List<String>
    private String strImgPath;			//string으로 변환한 imgPath
    
//    public BoardDTO() {
//    	this.imgPath =  
//    }
}


