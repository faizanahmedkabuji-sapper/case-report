package ai.sapper.casereport.factory;

import ai.sapper.casereport.dto.CaseDto;
import ai.sapper.casereport.service.CaseCsvFileGenerator;
import ai.sapper.casereport.service.DefaultFileGenerator;
import ai.sapper.casereport.service.FileGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class FileGeneratorFactory {
    private final ApplicationContext applicationContext;
    private final Map<Class<?>, FileGenerator<?>> fileGenerators = new HashMap<>();

    @PostConstruct
    public void init() {
        fileGenerators.put(CaseDto.class, applicationContext.getBean(CaseCsvFileGenerator.class));
    }

    public <T> FileGenerator<T> getGenerator(Class<T> clazz) {
        return (FileGenerator<T>) fileGenerators.getOrDefault(clazz, applicationContext.getBean(DefaultFileGenerator.class));
    }
}
