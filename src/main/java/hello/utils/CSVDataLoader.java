package hello.utils;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class CSVDataLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVDataLoader.class);

    public <T> List<T> loadObjectList(Class<T> type, String filename){
        try{
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();

            File file = new ClassPathResource(filename).getFile();
            MappingIterator<T> readValues = mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();

        } catch(Exception e){
            LOGGER.error("Error while loading {} file: {}", filename, e.toString());
            return Collections.emptyList();
        }
    }
}
