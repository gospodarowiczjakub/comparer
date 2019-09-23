package hello.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import hello.config.FileConfiguration;
import hello.model.ReportClaim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CSVUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtils.class);
    private static final char COLUMN_SEPARATOR = ';';

    public static boolean saveObjectList(List<ReportClaim> type, String filename) {
        Date date = Calendar.getInstance().getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String strDate = dateFormat.format(date);
        File csvOutputData = new File(filename + "_" + strDate + ".csv");

        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema bootstrapSchema = CsvSchema.builder()
                    .addColumn("claimNumber")
                    .addColumn("epsNumber")
                    .addColumn("attachmentNumber")
                    .addColumn("attachmentName")
                    .setColumnSeparator(COLUMN_SEPARATOR)
                    .setUseHeader(true)
                    .build();

            ObjectWriter writer = mapper
                    .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                    //.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE)
                    .writerFor(ReportClaim.class)
                    .with(bootstrapSchema);
            writer.writeValues(csvOutputData).writeAll(type);
            LOGGER.info("Lost attachments saved to {} file", csvOutputData.getName());
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
