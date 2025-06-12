package dto;

import entity.Likelog;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LikeDTO extends RequestDTO{
	private String boardSysNo;		//좋아요 누른 게시물 System no.
    private String action;          //좋아요 누름 / 취소 액션
    
    /* LikeDTO -> Like Entity 변환 */
    public Likelog toEntity() {
    	Likelog like = Likelog.builder()
        		.sysNo(this.getSysNo())
        		.userId(this.getUserId())
        		.userSysNo(this.getUserSysNo())
        		.boardSysNo(boardSysNo)
                .build();
        return like;
    }
}


