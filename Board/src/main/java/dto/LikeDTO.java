package dto;

import entity.Likelog;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LikeDTO extends RequestDTO{
	private String boardSysNo;		//좋아요 누른 게시물 Sys no.
    private String action;          //좋아요 증가, 감소 액션
    
    /* LikeDTO -> Like Entity 변환 */
    public Likelog toEntity() {
    	Likelog like = Likelog.builder()
        		.sysNo(this.getSysNo())
        		.userId(this.getUserId())
        		.userSysNo(this.getUserSysNo())
        		.boardSysNo(boardSysNo)
//                .modifyDate(this.getModifyDate())
//                .createDate(this.getCreateDate())
                .build();
        return like;
    }
}


