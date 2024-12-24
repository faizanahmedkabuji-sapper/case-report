package ai.sapper.casereport.scheduler;

import ai.sapper.casereport.config.CaseSchedulerConfig;
import ai.sapper.casereport.dto.CaseDto;
import ai.sapper.casereport.factory.FileGeneratorFactory;
import ai.sapper.casereport.service.CaseService;
import ai.sapper.casereport.service.FileCleanupService;
import ai.sapper.casereport.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Scheduler class to handle case report generation and upload to S3.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class CaseScheduler {
    private final CaseSchedulerConfig caseSchedulerConfig;
    private final FileGeneratorFactory fileGeneratorFactory;
    private final CaseService caseService;
    private final S3Service s3Service;
    private final FileCleanupService fileCleanupService;

    /**
     * Scheduled method to generate case reports and upload them to S3.
     * This method runs based on the cron expression defined in the configuration.
     */
    @Scheduled(cron = "#{caseSchedulerConfig.cron}")
    public void schedule() {
        log.info("Starting case scheduler");
        ZonedDateTime date = ZonedDateTime.now(ZoneId.of(caseSchedulerConfig.getTimeZone()))
                .minusHours(caseSchedulerConfig.getHoursBack())
                .minusMinutes(caseSchedulerConfig.getMinutesBack());

        List<CaseDto> cases = caseService.findCasesByCreatedDateAfter(date, caseSchedulerConfig.getMaxRecords());
        log.info("Found {} cases", cases.size());
        String fileName = generateFileName(date, cases);
        File file = fileGeneratorFactory.getGenerator(CaseDto.class).generate(cases, fileName);
        log.info("Generated file: {}", file.getName());
        s3Service.uploadFile(file);
        log.info("Finished case scheduler");

        // Clear the temp directory after the file is uploaded
        fileCleanupService.clearTempDirectory();
    }

    /**
     * Generates a file name based on the given date and list of cases.
     *
     * @param date  the start date for the file name
     * @param cases the list of cases
     * @return the generated file name
     */
    private String generateFileName(ZonedDateTime date, List<CaseDto> cases) {
        String startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss").format(date);
        String endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss").format(ZonedDateTime.now(ZoneId.of(caseSchedulerConfig.getTimeZone())));
        return startDate + "_to_" + endDate + (cases.isEmpty() ? "_(no_records)" : "");
    }
}