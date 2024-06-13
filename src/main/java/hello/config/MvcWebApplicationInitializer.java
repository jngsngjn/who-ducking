package hello.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String LOCATION = ""; // 업로드할 파일이 임시로 저장될 위치 (빈 문자열은 디폴트 위치)
    private static final long MAX_FILE_SIZE = 20848820; // 최대 업로드 파일 크기 (20MB)
    private static final long MAX_REQUEST_SIZE = 418018841; // 최대 요청 크기 (400MB)
    private static final int FILE_SIZE_THRESHOLD = 1048576; // 파일 임계값 (1MB)

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SecurityConfig.class, WebMvcConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement(
                LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
    }
}