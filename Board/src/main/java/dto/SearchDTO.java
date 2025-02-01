package dto;

import java.util.HashMap;
import java.util.Map;

public class SearchDTO {
	private String type;                //게시물 List 타입
    private int pageIndex;
    private int pageSize;	
    private Map<String, String> searchList;	
    private int countFlag; //count flag
    
    public SearchDTO() {
    	this.pageIndex = 0;
    	this.pageSize = 10;
    	this.searchList = new HashMap<>();
    }
    
    public SearchDTO(int pageIndex, int pageSize, Map<String, String> searchList, String type) {
    	this.pageIndex = pageIndex;
    	this.pageSize = pageSize;
    	this.searchList = searchList;
    	this.type = type;
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
}


