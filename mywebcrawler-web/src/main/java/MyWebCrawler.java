import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan({"mywebcrawler_core","mywebcrawler_web","task_manager"})
@EntityScan({"mywebcrawler_core.model"})
@EnableJpaRepositories(basePackages={"mywebcrawler_core.repositories"})
@EnableTransactionManagement
public class MyWebCrawler {
    public static void main(String[] args) {
        SpringApplication.run(MyWebCrawler.class);
    }
}
