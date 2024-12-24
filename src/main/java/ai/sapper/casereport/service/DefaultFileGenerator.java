package ai.sapper.casereport.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Log4j2
@Component
public class DefaultFileGenerator<T> implements FileGenerator<T> {
    @Override
    public File generate(List<T> data, String name) {
        log.info("Default file generator is used");
        return null;
    }
}
