package dto;

import java.util.ArrayList;
import java.util.List;
import entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BoardDTO extends RequestDTO{
    private String title;								//제목
    private String content;								//내용
    private long view = 0; 								//조회수
    private long likeCount = 0;         				//좋아요 수 
    private String likeFlag;            				//사용자의 좋아요 누른 판별
    private List<String> imgPath = new ArrayList<>(); 	//이미지 경로 List<String>
    private String strImgPath;							//string으로 변환한 imgPath
    
    public BoardDTO(Board board, List<String> imgPath) {
    	super.setSysNo(board.getSysNo());
    	this.title = board.getTitle();
    	this.content = board.getContent();
    }
    
    public BoardDTO(Board board) {
    	super.setSysNo(board.getSysNo());
    	super.setUserId(board.getUserId());
    	super.setUserSysNo(board.getUserSysNo());
    	this.title = board.getTitle();
    	this.content = board.getContent();
    	this.view = board.getView();
    	this.likeCount = board.getLike();
    }
    
    /* BoardDTO -> Board Entity 변환 */
    public Board toEntity() {
        Board board = Board.builder()
        		.sysNo(this.getSysNo())
                .title(title)
                .content(content)
                .imgPath(strImgPath)
                .userId(this.getUserId())
                .userSysNo(this.getUserSysNo())
                .build();
        return board;
    }
    
    public static BoardDTO fromEntity(Board board, List<String> imgPath) {
        return new BoardDTO(board, imgPath);
    }
    
    public static BoardDTO fromEntity(Board board) {
        return new BoardDTO(board);
    }
}


