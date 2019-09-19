package hello.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import hello.model.ReportUniqueClaim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class CSVUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtils.class);
    private static final char COLUMN_SEPARATOR = ';';
    private static final String RESULT_FILENAME = "output.csv";

    public static boolean saveObjectList(List<ReportUniqueClaim> type) {
        File csvOutputData = new File(RESULT_FILENAME);

        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema bootstrapSchema = CsvSchema.builder()
                    .addColumn("claimNumber")
                    .addColumn("EPSNumber")
                    .addColumn("attachments.attachmentNumber")
                    .addColumn("attachments.filename")
                    .setColumnSeparator(COLUMN_SEPARATOR)
                    .setUseHeader(true)
                    .build();

            ObjectWriter writer = mapper
                    //.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                    //.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE)
                    .writerFor(ReportUniqueClaim.class)
                    .with(bootstrapSchema);
            writer.writeValues(csvOutputData).writeAll(type);

            return true;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return false;
        }
    }

    public static <T> List<T> loadObjectList(Class<T> type, String filename) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.builder()
                    .addColumn("claimNumber")
                    .addColumn("epsNumber")
                    .addColumn("attachmentNumber")
                    .addColumn("attachmentName")
                    .addColumn("sent")
                    .setColumnSeparator(COLUMN_SEPARATOR)
                    .setUseHeader(true)
                    .build();
            CsvMapper mapper = new CsvMapper();

            File file = new ClassPathResource(filename).getFile();
            MappingIterator<T> readValues = mapper.readerFor(type).with(bootstrapSchema).readValues(file);

            return readValues.readAll();

        } catch (Exception e) {
            LOGGER.error("Error while loading {} file: {}", filename, e.toString());
            return Collections.emptyList();
        }
    }
}
