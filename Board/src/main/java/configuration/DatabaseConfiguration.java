package configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {
	
	@Autowired	//여기
    private ApplicationContext applicationContext;
	
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://mydb.cpygi2cq2n6v.ap-northeast-2.rds.amazonaws.com:3306/board?serverTimezone=Asia/Seoul&characterEncoding=UTF-8")
                .username("root")
                .password("dmd950112")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    // SqlSessionFactory 설정
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    // SqlSessionTemplate 설정
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
        		.withEndpointConfiguration(new EndpointConfiguration("s3.ap-northeast-2.amazonaws.com", "ap-northeast-2")) // 엔드포인트 추가
        		.build();
    }
}



