package dto;

import java.util.HashMap;
import java.util.Map;

public class SearchDTO {
	private String type;                //게시물 List 타입
    private int pageIndex;
    private int pageSize;	
    private Map<String, String> searchList;	
    private int countFlag; //count flag
    private String userId;
    private String userSysNo;
    
    public SearchDTO() {
    	this.pageIndex = 0;
    	this.pageSize = 10;
    	this.searchList = new HashMap<>();
    	this.userId = "";
    	this.userSysNo = "";
    }
    
    public SearchDTO(int pageIndex, int pageSize, Map<String, String> searchList, String type, String userId, String userSysNo) {
    	this.pageIndex = pageIndex;
    	this.pageSize = pageSize;
    	this.searchList = searchList;
    	this.type = type;
    	this.userId = userId;
    	this.userSysNo = userSysNo;
    }

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, String> getSearchList() {
		return searchList;
	}

	public void setSearchList(Map<String, String> searchList) {
		this.searchList = searchList;
	}
	
	public int getCountFlag() {
		return countFlag;
	}

	public void setCountFlag(int countFlag) {
		this.countFlag = countFlag;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}


