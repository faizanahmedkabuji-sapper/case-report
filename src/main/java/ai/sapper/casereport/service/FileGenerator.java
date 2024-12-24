package ai.sapper.casereport.service;

import java.io.File;
import java.util.List;

public interface FileGenerator<T> {
    File generate(List<T> data, String name);
}
