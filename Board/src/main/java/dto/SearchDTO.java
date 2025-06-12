package dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchDTO extends RequestDTO{
	private String type;         								//게시물 List 타입(좋아요, 조회수 등)
    private int pageIndex = 1;	 								//페이지 번호
    private int pageSize = 10;	 								//페이지 당 게시물 수 
    private int countFlag; 		 								//게시물 count 조회 플래그 
    private Map<String, String> searchList = new HashMap<>(); 	//검색
}


