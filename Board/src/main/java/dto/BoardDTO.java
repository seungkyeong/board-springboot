package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BoardDTO {
    private String sysNo;				//게시물 System No
    private String title;				// 제목
    private String content;				// 내용
    private String userId;				// 작성자 ID
    private String userSysNo;			// 작성자
    private long view; 					//조회수
    private List<String> imgPath; 		//이미지 경로 List<String>
    private String strImgPath;			//string으로 변환한 imgPath
    private LocalDateTime createDate;	// 작성일자
    private LocalDateTime modifyDate;	// 수정일자
    private String formattedCreateDate;	// 변환 작성일자
    private String formattedModifyDate;	// 변환 수정일자
    private long likeCount;             //좋아요 수 
    
    // 포맷 정의 (공통 사용)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    
    public BoardDTO() {
    	this.sysNo = "";
    	this.title = "";
    	this.content = "";
    	this.userId = "";
    	this.userSysNo = "";
    	this.createDate = LocalDateTime.now();
    	this.modifyDate = LocalDateTime.now();
    	this.view = 0;
    	this.imgPath = new ArrayList<>(); //new ArrayList<>();
    	this.strImgPath = "";
    	this.formattedCreateDate = "";
    	this.formattedModifyDate = "";
    	this.likeCount = 0;
    }
    
    // Getter: 포맷된 날짜 반환
    public String getFormattedCreateDate() {
        return createDate != null ? createDate.plusHours(9).format(FORMATTER) : null;
    }

    public void setFormattedCreateDate(String formattedCreateDate) {
		this.formattedCreateDate = formattedCreateDate;
	}

	public String getFormattedModifyDate() {
        return modifyDate != null ? modifyDate.plusHours(9).format(FORMATTER) : null;
    }
	

	public void setFormattedModifyDate(String formattedModifyDate) {
		this.formattedModifyDate = formattedModifyDate;
	}
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserSysNo() {
		return userSysNo;
	}
	public void setUserSysNo(String userSysNo) {
		this.userSysNo = userSysNo;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	public LocalDateTime getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	public long getView() {
		return view;
	}
	public void setView(long view) {
		this.view = view;
	}
	public List<String> getImgPath() {
		return imgPath;
	}
	public void setImgPath(List<String> imgPath) {
		this.imgPath = imgPath;
	}
	public String getStrImgPath() {
		return strImgPath;
	}
	public void setStrImgPath(String strImgPath) {
		this.strImgPath = strImgPath;
	}
	public Long getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	@Override
	public String toString() {
		return "BoardDTO [sysNo=" + sysNo + ", title=" + title + ", content=" + content
				+ ", userId=" + userId + ", userSysNo=" + userSysNo + ", createDate=" + createDate + ", modifyDate="
				+ modifyDate + ", view=" + view + ", strImgPath=" + strImgPath + ", imgPath=" + imgPath + "]";
	}
}


