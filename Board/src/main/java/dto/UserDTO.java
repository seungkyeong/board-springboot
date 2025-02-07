package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDTO {
    private String sysNo;				//사용자 System No
    private String id;				// 제목
    private String password;				// 내용
    private String name;				// 작성자 ID
    private String email;			//조회수
    private String phone;			//string으로 변환한 imgPath
    private LocalDateTime createDate;	// 작성일자
    private LocalDateTime modifyDate;	// 수정일자
    private String formattedCreateDate;	// 변환 작성일자
    private String formattedModifyDate;	// 변환 수정일자
    
    // 포맷 정의 (공통 사용)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserDTO() {
    	this.sysNo = "";
    	this.id = "";
    	this.password = "";
    	this.name = "";
    	this.email = "";
    	this.createDate = LocalDateTime.now();
    	this.modifyDate = LocalDateTime.now();
    	this.phone = "";
    	this.formattedCreateDate = "";
    	this.formattedModifyDate = "";
    }
    
    // Getter: 포맷된 날짜 반환
    public String getFormattedCreateDate() {
        return createDate != null ? createDate.format(FORMATTER) : null;
    }

    public void setFormattedCreateDate(String formattedCreateDate) {
		this.formattedCreateDate = formattedCreateDate;
	}

	public String getFormattedModifyDate() {
        return modifyDate != null ? modifyDate.format(FORMATTER) : null;
    }
	

	public void setFormattedModifyDate(String formattedModifyDate) {
		this.formattedModifyDate = formattedModifyDate;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	@Override
	public String toString() {
		return "UserDTO [sysNo=" + sysNo + ", userId=" + id + ", userPw=" + password + ", userName=" + name
				+ ", userEmail=" + email + ", userPhone=" + phone + ", createDate=" + createDate
				+ ", modifyDate=" + modifyDate + ", formattedCreateDate=" + formattedCreateDate
				+ ", formattedModifyDate=" + formattedModifyDate + "]";
	}
}


