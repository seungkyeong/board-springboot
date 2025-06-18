package dto;

import java.util.ArrayList;
import java.util.List;
import entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CommentDTO extends RequestDTO {
    private String parSysNo = "";		    //상위 댓글 System No
    private String boardSysNo;			//게시물 System No
    private String title;				//게시물 제목
    private String boardCreater;		//게시물 작성자 id
    private String boardCreaterSysNo;	//게시물 작성자 sysNo
    private String comment;				//댓글
    private List<CommentDTO> replies =  new ArrayList<>(); 	//대댓글 리스트
    
    public CommentDTO(Comment comment) {
    	super.setSysNo(comment.getSysNo());
    	this.comment = comment.getComment();
    	super.setUserId(comment.getUserId());
    	super.setUserSysNo(comment.getUserSysNo());
    	this.boardSysNo = comment.getBoardSysNo();
    	this.parSysNo = comment.getParSysNo();
    }
    
    /* CommentDTO -> Comment Entity 변환 */
    public Comment toEntity() {
        Comment comment = Comment.builder()
        		.sysNo(super.getSysNo())
        		.parSysNo(parSysNo)
        		.boardSysNo(boardSysNo)
        		.comment(this.comment)
        		.userId(super.getUserId())
        		.userSysNo(super.getUserSysNo())
                .build();
        return comment;
    }
    
    /* Comment Entity -> CommentDTO 변환 */
    public static CommentDTO fromEntity(Comment comment) {
        return new CommentDTO(comment);
    }
}


