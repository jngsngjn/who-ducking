package hello.config;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class MySqlCleanupListener implements ServletContextListener {

    private final ThreadPoolTaskScheduler taskScheduler;

    public MySqlCleanupListener(ThreadPoolTaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 초기화 작업이 필요 없는 경우 비워둡니다.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 스케줄러 종료
        if (taskScheduler != null) {
            taskScheduler.shutdown();
        }
        // MySQL 드라이버 정리
        AbandonedConnectionCleanupThread.checkedShutdown();
    }
}
