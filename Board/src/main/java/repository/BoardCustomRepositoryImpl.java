package repository;

import java.util.ArrayList;
import java.util.List;
import dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dto.SearchDTO;
import entity.Board;
import entity.QBoard;
import entity.QLikelog;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {
	private final JPAQueryFactory queryFactory;
	QBoard board = QBoard.board;
	QLikelog likeLog = QLikelog.likelog;
	
	/* 게시물 조회 */
	/* allList: 게시물 조회 
	 * viewList: 조회수 Top 게시물 조회
	 * myBoardList: 내가 쓴 게시물 조회
	 * likeList: 좋아요 Top 게시물 조회 */
	@Override
	public Page<Board> getBoard(SearchDTO searchDto){    
        //페이징 정보 세팅
        Pageable pageable = PageRequest.of(searchDto.getPageIndex(), searchDto.getPageSize());
        
        //전체 게시물 Count 조회
        long totalCount = getTotalCount(searchDto);
        
        //게시물 조회 쿼리 실행
        List<Board> content = buildBaseQuery(searchDto)
        		.offset(pageable.getOffset())
        		.limit(pageable.getPageSize())
        	    .orderBy(orderSpecifier(searchDto.getType()).toArray(new OrderSpecifier[0]))
        	    .fetch();

        return new PageImpl<>(content, pageable, totalCount);
	}
	
	/* 게시물 상세 조회 */
	@Override
	public BoardDTO getBoardDetail(SearchDTO searchDto){ 
		//검색 조건 생성
        BooleanBuilder where = searchCondition(searchDto);      
        
        //게시물 상세 조회(사용자의 좋아요 로그 같이 조회)         
        BoardDTO content = queryFactory
        			.select(
        				Projections.bean(
        					BoardDTO.class,
        					board.sysNo.as("sysNo"),
        					board.title,
        					board.content,
        					board.userId.as("userId"),
        					board.userSysNo.as("userSysNo"),
        					board.createDate,
        					board.modifyDate,
        					board.view,
        					board.imgPath.as("strImgPath"),
        					new CaseBuilder()
        		            	.when(likeLog.sysNo.isNotNull())
        		            	.then("like")
        		            	.otherwise("unLike")
        		            	.as("likeFlag")
        				)
        			)
        			.from(board)
        			.leftJoin(likeLog)
        			.on(likeLog.boardSysNo.eq(searchDto.getSearchList().get("sysNo"))
        					.and(likeLog.userSysNo.eq(searchDto.getUserSysNo())))
        			.where(where)
        			.fetchOne();

        return content;
	}
	
	/* 검색 조건 생성 */
	private BooleanBuilder searchCondition(SearchDTO searchDto) {
        BooleanBuilder where = new BooleanBuilder();
        
        //검색 조건 생성(공통 조회 조건)
        if (searchDto.getSearchList().containsKey("title")) { //제목 검색
        	where.and(board.title.contains(searchDto.getSearchList().get("title")));
        }
        if (searchDto.getSearchList().containsKey("content")) { //내용 검색
        	where.and(board.content.contains(searchDto.getSearchList().get("content")));
        }
        if (searchDto.getSearchList().containsKey("userId")) { //작성자 검색
        	where.and(board.userId.contains(searchDto.getSearchList().get("userId")));
        }
        if (searchDto.getSearchList().containsKey("sysNo")) { //게시물 상세 조회 검색
        	where.and(board.sysNo.eq(searchDto.getSearchList().get("sysNo")));
        }  
        
        //type별 조회 조건 생성
        if(searchDto.getType().equals("myBoardList")) {
        	where.and(board.userSysNo.eq(searchDto.getUserSysNo()));
        }
        
        return where;
    }
	
	/* 정렬 조건 생성 */
	private List<OrderSpecifier<?>> orderSpecifier(String type) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();

		switch(type) {
			case "viewList":
				orders.add(board.view.desc());
				orders.add(board.createDate.desc());
				break;
			case "likeList":
				orders.add(likeLog.sysNo.count().desc());
				orders.add(board.createDate.desc());
				break;
			case "myLikeList":
				orders.add(likeLog.createDate.desc());
				break;
			case "allList":
			case "myBoardList":
			default:
				orders.add(board.createDate.desc());
				break;
		}
		return orders;
	}
	
	
	/* 게시글 Count Query 생성 */
	private long getTotalCount(SearchDTO searchDto) {
		if ("likeList".equals(searchDto.getType())) {
	        BooleanBuilder where = searchCondition(searchDto);
	        
	        return queryFactory
	            .select(board.sysNo.countDistinct())
	            .from(board)
	            .leftJoin(likeLog)
	            .on(likeLog.boardSysNo.eq(board.sysNo))
	            .where(where)
	            .fetchOne();
	    } else {
	        return buildBaseQuery(searchDto)
	            .select(board.count())
	            .fetchOne();
	    }
    }
	
	/* 게시글 Base Query 생성 */
	private JPAQuery<Board> buildBaseQuery(SearchDTO searchDto) {
		//검색 조건 생성
        BooleanBuilder where = searchCondition(searchDto);  
        
        if ("likeList".equals(searchDto.getType())) { //좋아요 TOP 게시물 목록인 경우, 좋아요 로그와 left outer join 
            return queryFactory
            		.select(
            			Projections.constructor(
        					Board.class,
        				    board.sysNo,
        				    board.title,
        				    board.content,
        				    board.userId,
        				    board.userSysNo,
        				    board.createDate,
        				    board.modifyDate,
        				    board.view,
        				    board.imgPath,
        				    likeLog.sysNo.count().as("like")
        				  )
            		)
                    .from(board)
                    .leftJoin(likeLog)
                    .on(likeLog.boardSysNo.eq(board.sysNo))
                    .where(where)
                    .groupBy(board.sysNo);
        } else {
        	//게시물 조회 쿼리 생성
            JPAQuery<Board> query = queryFactory
                    .selectFrom(board)
                    .where(where);
            
            if ("myLikeList".equals(searchDto.getType())) { //내 좋아요 게시물 목록인 경우, 좋아요 로그와 inner join
                query = query
                    .innerJoin(likeLog)
                    .on(likeLog.boardSysNo.eq(board.sysNo)
                    		.and(likeLog.userSysNo.eq(searchDto.getUserSysNo())));
            } 
            return query;
        }
    }
}
