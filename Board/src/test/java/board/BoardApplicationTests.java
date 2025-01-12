//package board;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import java.util.HashMap;
//import java.util.Map;
//import org.junit.jupiter.api.Test;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dto.SearchDTO;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class BoardApplicationTests {
//
//	@Autowired
//	private SqlSessionTemplate sqlSession;
//	
//	@Autowired
//    private MockMvc mockMvc;
//
//	@Test
//	void contextLoads() {
//	}
//	
//	@Test
//	public void testSqlSession() throws Exception {
//		System.out.println(sqlSession.toString());
//	}
//	
//	/* 게시판 목록 조회 */
//	@Test
//	public void getAllBoard() throws Exception {
//System.out.println("--------------------login Start--------------------");	
//		
//		//검색 조건 생성
//		Map<String, String> mapSg = new HashMap<>();
//		mapSg.put("id", "sgjeong");  
//		mapSg.put("password", "sgjeong");  
//		System.out.println(mapSg);
//		
//		// Map을 JSON 문자열로 변환
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = objectMapper.writeValueAsString(mapSg);
//		
//			    
//	    //검색
//		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/login")
//						.contentType("application/json") // 요청 본문 타입 지정
//						.content(json.getBytes())) // JSON 데이터 전달
//		                .andExpect(status().isOk())
//		                .andReturn();
//		
//		 // 로그인 응답에서 JWT 토큰 추출 (응답 본문에서 추출)
//	    String responseContent = result.getResponse().getContentAsString();
//	    ObjectMapper responseMapper = new ObjectMapper();
//	    JsonNode jsonResponse = responseMapper.readTree(responseContent);
//	    System.out.println("jsonResponse");
//	    
//	    
//	    String jwtToken = jsonResponse.get("data").asText(); // 'data'가 JWT 토큰이 포함된 부분
//	    System.out.println(jwtToken);
//        
//		System.out.println("--------------------login End--------------------");	
//		
//		System.out.println("--------------------getAllBoard Start--------------------");	
//		//검색 조건 생성
//		Map<String, String> requestParam = new HashMap<>();
//		requestParam.put("title", "인형");  
//		requestParam.put("content", "인형");
//		
//		SearchDTO search = new SearchDTO();
//		search.setPageIndex(0);
//	    search.setPageSize(10);
//	    search.setSearchList(requestParam);
//	    
//	    System.out.println(requestParam);
//	    System.out.println(search);
//	    
//	    // JSON으로 변환
//	    objectMapper = new ObjectMapper();
//	    String boardJson = objectMapper.writeValueAsString(search);
//	    
//	    //검색
//		result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/list")
//				.header("Authorization", "Bearer " + jwtToken)
//				.contentType("application/json") // 요청 본문 타입 지정
//                .content(boardJson)) // JSON 데이터 전달
//                .andExpect(status().isOk())
//                .andReturn();
//		System.out.println(result.getResponse().getContentAsString());
//		System.out.println("--------------------getAllBoard End--------------------");	
//	}
////	
////	/* 게시물 생성 */
////	@Test
////	public void createBoard() throws Exception {
////		System.out.println("--------------------createBoard Start--------------------");	
////		//이미지 배열 만들기
////		List<String> imgPath = new ArrayList<>();
////		imgPath.add("img1");
////		imgPath.add("img2");
////		
////		//저장 BoardDTO 만들기
////		BoardDTO board = new BoardDTO();
////	    board.setTitle("모루 인형");
////	    board.setContent("제가 만든 모루 인형이에요!");
////	    board.setUserId("smjeong");
////	    board.setUserName("정승민");
////	    board.setImgPath(imgPath);
////	    board.setView(0);
////	    board.setCreateDate(null);
////	    board.setModifyDate(null);
////
////	    // JSON으로 변환
////	    ObjectMapper objectMapper = new ObjectMapper();
////	    String boardJson = objectMapper.writeValueAsString(board);
////	    
////	    //저장
////		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/post")
////				.contentType("application/json") // 요청 본문 타입 지정
////                .content(boardJson)) // JSON 데이터 전달
////                .andExpect(status().isOk())
////                .andReturn();
////		System.out.println(result.getResponse().getContentAsString());
////		System.out.println("--------------------createBoard End--------------------");	
////	}
////	
////	/* 파일 업로드 */
////	@Test
////	public void uploadFile() throws Exception {
////		System.out.println("--------------------uploadFile Start--------------------");	
////		//이미지 formData 만들기
////		MockMultipartFile file1 = new MockMultipartFile("files", "test-image1.jpg", "image/jpeg", "test image content".getBytes());
////		MockMultipartFile file2 = new MockMultipartFile("files", "test-image2.jpg", "image/jpeg", "test image content".getBytes());
////		
////	    //s3 presignedurl
////		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/board/post/file")
////				.file(file1).file(file2)
////				.contentType("multipart/form-data"))
////                .andExpect(status().isOk())
////                .andReturn();
////		System.out.println(result.getResponse().getContentAsString());
////		System.out.println("--------------------uploadFile End--------------------");	
////	}
//	
//	/* 게시물 수정 */
////	@Test
////	public void modifyBoard() throws Exception {
////		System.out.println("--------------------modifyBoard Start--------------------");	
////		
////		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/post/e0373a095c554ef88658e64716b7d4bd")
////				.param("title", "수정")
////				.param("content", "수정")
////				.param("userId", "sgjeong")
////				.param("userName", "정승경")
////				.param("imgPath", "수정"))
////                .andExpect(status().isOk()).andReturn();
////		System.out.println(result.getResponse().getContentAsString());
////        
////		System.out.println("--------------------modifyBoard End--------------------");	
////	}
//	
//	/* 게시물 상세 조회 */
////	@Test
////	public void getBoardDetail() throws Exception {
////		System.out.println("--------------------getBoardDetail Start--------------------");	
////		
////		//검색 조건 생성
////		Map<String, String> mapSg = new HashMap<>();
////		mapSg.put("sysNo", "57891255b52711ef9b2402508bdeccbf");  
////				
////		SearchDTO search = new SearchDTO();
////		search.setPageIndex(0);
////		search.setPageSize(10);
////		search.setSearchList(mapSg);
////			    
////		System.out.println(mapSg);
////		System.out.println(search);
////			    
////		// JSON으로 변환
////		ObjectMapper objectMapper = new ObjectMapper();
////	    String boardJson = objectMapper.writeValueAsString(search);
////			    
////	    //검색
////		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/detail")
////						.contentType("application/json") // 요청 본문 타입 지정
////		                .content(boardJson)) // JSON 데이터 전달
////		                .andExpect(status().isOk())
////		                .andReturn();
////		
////		System.out.println(result.getResponse().getContentAsString());
////        
////		System.out.println("--------------------getBoardDetail End--------------------");	
////	}
//	
//	/* 로그인 */
////	@Test
////	public void login() throws Exception {
////		System.out.println("--------------------login Start--------------------");	
////		
////		//검색 조건 생성
////		Map<String, String> mapSg = new HashMap<>();
////		mapSg.put("id", "sgjeong");  
////		mapSg.put("password", "sgjeong");  
////		System.out.println(mapSg);
////		
////		// Map을 JSON 문자열로 변환
////		ObjectMapper objectMapper = new ObjectMapper();
////		String json = objectMapper.writeValueAsString(mapSg);
////		
////			    
////	    //검색
////		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/login")
////						.contentType("application/json") // 요청 본문 타입 지정
////						.content(json.getBytes())) // JSON 데이터 전달
////		                .andExpect(status().isOk())
////		                .andReturn();
////		
////		System.out.println(result.getResponse().getContentAsString());
////        
////		System.out.println("--------------------login End--------------------");	
////	}
//	
//	/* 아이디 찾기 */
////	@Test
////	public void findid() throws Exception {
////		System.out.println("--------------------find ID Start--------------------");	
////		
////		//검색 조건 생성
////		Map<String, String> mapSg = new HashMap<>();
////		mapSg.put("email", "sgjeong@naver.com");  
////		System.out.println(mapSg);
////		
////		// Map을 JSON 문자열로 변환
////		ObjectMapper objectMapper = new ObjectMapper();
////		String json = objectMapper.writeValueAsString(mapSg);
////		
////			    
////	    //검색
////		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/findIdPw")
////						.contentType("application/json") // 요청 본문 타입 지정
////						.content(json.getBytes())) // JSON 데이터 전달
////		                .andExpect(status().isOk())
////		                .andReturn();
////		
////		System.out.println(result.getResponse().getContentAsString());
////        
////		System.out.println("--------------------find ID End--------------------");	
////	}
//}
