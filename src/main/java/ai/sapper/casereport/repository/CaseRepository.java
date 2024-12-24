package ai.sapper.casereport.repository;

import ai.sapper.casereport.dto.CaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * Repository class for accessing case data from MongoDB.
 */
@Repository
@RequiredArgsConstructor
public class CaseRepository {
    private final MongoTemplate mongoTemplate;

    /**
     * Finds cases created after the specified date.
     *
     * @param date       the date to compare against the created date of cases
     * @param maxRecords the maximum number of records to return
     * @return a list of cases created after the specified date
     */
    public List<CaseDto> findCasesByCreatedDateAfter(ZonedDateTime date, int maxRecords) {
        Date convertedDate = Date.from(date.toInstant());

        Query query = new Query();
        query.addCriteria(Criteria.where("createdDate").gte(convertedDate));
        query.limit(maxRecords);

        return mongoTemplate.find(query, CaseDto.class, "case");
    }
}