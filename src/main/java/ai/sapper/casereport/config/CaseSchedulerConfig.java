package ai.sapper.casereport.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "case-scheduler")
public class CaseSchedulerConfig {
    private String cron;
    private int hoursBack;
    private int minutesBack;
    private int maxRecords;
    private String timeZone;
}
