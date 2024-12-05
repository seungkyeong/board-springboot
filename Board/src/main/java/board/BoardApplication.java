package board;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication 
@MapperScan(basePackages = "dao") // BoardDAO의 패키지 경로 설정 
@ComponentScan(basePackages = {"board", "controller", "service", "dao", "dto", "configuration", "exceptionHandle", "constant"}) 
public class BoardApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
