package ai.sapper.casereport.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Log4j2
@Service
public class FileCleanupService {

    @Value("${generated-files.cleanup-enabled:true}")
    private boolean cleanupEnabled;

    @Value("${generated-files.path}")
    private String cleanupPath;

    /**
     * Deletes all files in the specified directory if cleanup is enabled.
     */
    public void clearTempDirectory() {
        if (!cleanupEnabled) {
            log.info("File cleanup is disabled by configuration.");
            return;
        }

        File tempDir = new File(cleanupPath);
        int deletedFilesCount = 0;
        if (tempDir.isDirectory()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.delete()) {
                            deletedFilesCount++;
                            log.info("Deleted temp file: {}", file.getName());
                        } else {
                            log.warn("Failed to delete temp file: {}", file.getName());
                        }
                    }
                }
            }
        }
        log.info("Total number of files deleted: {}", deletedFilesCount);
    }
}