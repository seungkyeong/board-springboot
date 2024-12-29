package service;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import aws.S3Service;
import dao.BoardDAO;
import dto.BoardDTO;
import dto.SearchDTO;

@Service
public class BoardService {
	@Autowired                     
	private final BoardDAO Boarddao;
	@Autowired                     
	private final S3Service S3service;
	

    public BoardService(BoardDAO Boarddao, S3Service S3service) { 
        this.Boarddao = Boarddao;
        this.S3service = S3service;
    }

    /* 게시판 목록 조회 */ 
    public List<BoardDTO> getAllBoardList(SearchDTO search) throws Exception{
        return Boarddao.getAllBoardList(search);
    }
    
    /* 파일 업로드 */ 
    public List<URL> uploadFile(MultipartFile[] files) throws Exception{
    	List<URL> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            // 파일 이름 생성(uuid_file)
        	String decodeFileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
            String fileName = UUID.randomUUID().toString().replace("-", "") + decodeFileName;

            // S3 임시 업로드 url 발급
            URL url = S3service.generatePresignedUrl(fileName);
            fileUrls.add(url); // 파일 URL 리스트에 추가
        }
        return fileUrls;
    }
    
    /* 게시물 생성 & 수정 */ 
    public int postBoard(BoardDTO requestParam) throws Exception{
    	//strImgPath 넣기
    	requestParam.setStrImgPath(String.join(",", requestParam.getImgPath()));
    	
        if(requestParam.getSysNo() == "") { //신규 생성인 경우 
        	return Boarddao.createBoard(requestParam);
        }else { //수정인 경우 
        	System.out.println(requestParam.toString());
        	return Boarddao.updateBoard(requestParam);
        }
        
    }
    
    /* 게시물 상세 조회 */ 
    public BoardDTO getBoardDetail(SearchDTO search) throws Exception{  
    	BoardDTO boardDto = Boarddao.getBoardDetail(search);
    	
    	System.out.printf("getStrImgPath: %s", boardDto.getStrImgPath());
    	List<String> imgPath = new ArrayList<>(Arrays.asList(boardDto.getStrImgPath().split(",")));
    	boardDto.setImgPath(imgPath);
        return boardDto;
    }
}

