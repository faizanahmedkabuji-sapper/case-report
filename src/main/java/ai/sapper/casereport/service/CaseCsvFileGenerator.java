package ai.sapper.casereport.service;

import ai.sapper.casereport.dto.CaseDto;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates CSV files for case data.
 */
@Log4j2
@Component
public class CaseCsvFileGenerator implements FileGenerator<CaseDto> {

    @Value("${case.csv.headers}")
    private List<String> headers;

    @Value("${generated-files.path}")
    private String generatedFilesPath;

    /**
     * Generates a CSV file from the provided case data.
     *
     * @param data the list of case data to be written to the CSV file
     * @param name the name of the file (without extension)
     * @return the generated CSV file
     */
    @Override
    public File generate(List<CaseDto> data, String name) {
        log.info("Starting CSV file generation for file: {}", name);
        File file = new File(generatedFilesPath + "/" + name + ".csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            List<String> formattedHeaders = headers.stream()
                    .map(this::convertCamelCaseToNormalCase)
                    .collect(Collectors.toList());
            log.debug("Writing header to CSV file: {}", formattedHeaders);
            writer.writeNext(formattedHeaders.toArray(new String[0]));

            // Write data
            for (CaseDto caseDto : data) {
                String[] record = headers.stream().map(header -> getFieldValue(caseDto, header)).toArray(String[]::new);
                log.debug("Writing record to CSV file: {}", (Object) record);
                writer.writeNext(record);
            }
            log.info("CSV file generation completed successfully for file: {}", name);
        } catch (IOException e) {
            log.error("Error occurred while writing to file: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error occurred while generating file: {}", e.getMessage(), e);
        }
        return file;
    }

    /**
     * Converts a camel case string to normal case with spaces and capitalization.
     *
     * @param camelCase the camel case string to convert
     * @return the converted string in normal case
     */
    private String convertCamelCaseToNormalCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1 $2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1 $2")
                .toUpperCase();
    }

    /**
     * Retrieves the value of a field from a CaseDto object using reflection.
     *
     * @param caseDto the CaseDto object
     * @param fieldName the name of the field to retrieve
     * @return the value of the field as a string
     */
    private String getFieldValue(CaseDto caseDto, String fieldName) {
        try {
            Field field = CaseDto.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(caseDto);
            return value != null ? value.toString() : "";
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Error accessing field: {}", fieldName, e);
            return "";
        }
    }
}