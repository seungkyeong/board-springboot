package configuration;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import constant.AppConstant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {
//    private ApplicationContext applicationContext;
	
	/* DataSource 설정 */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(AppConstant.DataSourceConfig.DB_URL)
                .username(AppConstant.DataSourceConfig.DB_USER)
                .password(AppConstant.DataSourceConfig.DB_PW)
                .driverClassName(AppConstant.DataSourceConfig.DRIVER_CLASS_NAME)
                .build();
    }

    /* SqlSessionFactory 설정 */
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*Mapper.xml"));
//        return sqlSessionFactoryBean.getObject();
//    }

    /* SqlSessionTemplate 설정 */
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
    
    /* S3 */
    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
        		.withEndpointConfiguration(new EndpointConfiguration(AppConstant.DataSourceConfig.S3_POINT, AppConstant.DataSourceConfig.REGION)) 
        		.build();
    }
}



