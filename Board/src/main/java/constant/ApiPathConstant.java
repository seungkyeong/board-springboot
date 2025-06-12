package constant;

import lombok.Getter;

/* API Path 관련 Constant */
@Getter
public class ApiPathConstant {
	public static final String API_ROOT = "/api/board";
	
	public static class USER {
        public static final String SIGNUP = "/signUp";
        public static final String LOGIN = "/login";
        public static final String FIND_ID_PW = "/findIdPw";
        public static final String GET_DETAIL = "/userDetail";
        public static final String UPDATE_DETAIL = "/updateUserDetail";
        public static final String UPDATE_PW = "/updateUserPw";
    }
	
	public static class BOARD {
        public static final String GET_LIST = "/list";
        public static final String UPLOAD_FILE = "/post/fileUpload";
        public static final String DELETE_FILE = "/post/fileDelete";
        public static final String POST_BOARD = "/post";
        public static final String GET_DETAIL = "/detail";
        public static final String UPDATE_VIEW = "/updateCount";
        public static final String UPLOAD_LIKE = "/updateLike";
        public static final String POST_COMMENT = "/comment";
        public static final String DELETE_BOARD = "/boardDelete";
        public static final String DELETE_LIKE = "/likeDelete";
        
    }

    public static class NOTI {
        public static final String GET_LIST = "/notiList";
        public static final String UPDATE_LIST = "/update/notiList";
    }

}

