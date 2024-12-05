package board;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
class BoardApplicationTests {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
    private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void testSqlSession() throws Exception {
		System.out.println(sqlSession.toString());
	}
	
	/* 게시판 목록 조회 */
	@Test
	public void getAllBoard() throws Exception {
		System.out.println("--------------------getAllBoard Start--------------------");	
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/board/list"))
                .andExpect(status().isOk()).andReturn();
		System.out.println(result.getResponse().getContentAsString());
        
		System.out.println("--------------------getAllBoard End--------------------");	
	}
	
	/* 게시물 생성 */
	@Test
	public void createBoard() throws Exception {
		System.out.println("--------------------createBoard Start--------------------");	
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/post")
				.param("title", "모루 인형")
				.param("content", "제가 만든 모루 인형이에요!")
				.param("userId", "smjeong")
				.param("userName", "정승민")
				.param("imgPath", ""))
                .andExpect(status().isOk()).andReturn();
		System.out.println(result.getResponse().getContentAsString());
        
		System.out.println("--------------------createBoard End--------------------");	
	}
	
	/* 게시물 수정 */
	@Test
	public void modifyBoard() throws Exception {
		System.out.println("--------------------modifyBoard Start--------------------");	
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/board/post/e0373a095c554ef88658e64716b7d4bd")
				.param("title", "수정")
				.param("content", "수정")
				.param("userId", "sgjeong")
				.param("userName", "정승경")
				.param("imgPath", "수정"))
                .andExpect(status().isOk()).andReturn();
		System.out.println(result.getResponse().getContentAsString());
        
		System.out.println("--------------------modifyBoard End--------------------");	
	}
	
	/* 게시물 상세 조회 */
	@Test
	public void getBoardDetail() throws Exception {
		System.out.println("--------------------getBoardDetail Start--------------------");	
		
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/board/detail").param("sysNo", "e0373a095c554ef88658e64716b7d4bd"))
                .andExpect(status().isOk()).andReturn();
		System.out.println(result.getResponse().getContentAsString());
        
		System.out.println("--------------------getBoardDetail End--------------------");	
	}

}
