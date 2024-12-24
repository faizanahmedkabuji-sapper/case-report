package ai.sapper.casereport.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class CaseDto {
    private String id;
    private String coaId;
    private String coaName;
    private String assignee;
    private String status;
    private String type;
    private String channel;
    private String fileName;
    private Map<String, ?> attributes;
    private String submitDate;
    private String rejectReason;
    private String filePath;
    private String createdDate;
}
