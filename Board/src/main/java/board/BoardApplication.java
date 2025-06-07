package board;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication 
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
@MapperScan(basePackages = "dao") // BoardDAO의 패키지 경로 설정 
@ComponentScan(basePackages = {"board", "controller", "service", "dao", "dto", "configuration", "exceptionHandle", "constant", "aws", "util", "redisHandle", "webSocketHandle", "repository"}) 
public class BoardApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
