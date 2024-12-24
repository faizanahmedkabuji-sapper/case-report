package ai.sapper.casereport.service;

import ai.sapper.casereport.dto.CaseDto;
import ai.sapper.casereport.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling case-related operations.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CaseService {
    private final CaseRepository caseRepository;

    /**
     * Finds cases created after the specified date.
     *
     * @param date       the date to compare against the created date of cases
     * @param maxRecords the maximum number of records to return
     * @return a list of cases created after the specified date
     */
    public List<CaseDto> findCasesByCreatedDateAfter(ZonedDateTime date, int maxRecords) {
        log.info("Finding cases created after {}, max records: {}", date, maxRecords);
        List<CaseDto> cases = new ArrayList<>();
        try {
            cases = caseRepository.findCasesByCreatedDateAfter(date, maxRecords);
        } catch (Exception e) {
            log.error("Error occurred while fetching cases: {}", e.getMessage());
        }
        return cases;
    }
}