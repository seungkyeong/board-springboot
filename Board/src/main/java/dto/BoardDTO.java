package dto;

import java.time.LocalDateTime;

public class BoardDTO {
    private String sysNo;//게시물 System No
    private String title;	// 제목
    private String content;	// 내용
    private String userId;	// 작성자 ID
    private String userName;	// 작성자
    private LocalDateTime createDate;	// 작성일자
    private LocalDateTime modifyDate;	// 수정일자
    private long view; //조회수
    private String imgPath; //이미지 경로
    
    public BoardDTO(String sysNo, String title, String content, String userId, String userName, 
    		LocalDateTime createDate, LocalDateTime modifyDate, long view, String imgPath) {
    	this.sysNo = sysNo;
    	this.title = title;
    	this.content = content;
    	this.userId = userId;
    	this.userName = userName;
    	this.createDate = createDate;
    	this.modifyDate = modifyDate;
    	this.view = view;
    	this.imgPath = imgPath;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return "BoardDTO [sysNo=" + sysNo + ", title=" + title + ", content=" + content
				+ ", userId=" + userId + ", userName=" + userName + ", createDate=" + createDate + ", modifyDate="
				+ modifyDate + ", view=" + view + ", imgPath=" + imgPath + "]";
	}
}


