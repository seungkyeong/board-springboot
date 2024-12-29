package dto;

import java.util.HashMap;
import java.util.Map;

public class SearchDTO {
    private int pageIndex;
    private int pageSize;	
    private Map<String, String> searchList;	
    
    public SearchDTO() {
    	this.pageIndex = 0;
    	this.pageSize = 10;
    	this.searchList = new HashMap<>();
    }
    
    public SearchDTO(int pageIndex, int pageSize, Map<String, String> searchList) {
    	this.pageIndex = pageIndex;
    	this.pageSize = pageSize;
    	this.searchList = searchList;
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
}


