package hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import({ JpaConfig.class, RestTemplateConfig.class, ValidationConfig.class, SchedulingConfig.class })
@Configuration
@PropertySource("classpath:application.properties")
public class RootConfig {

}