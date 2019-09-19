package hello.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import hello.model.ReportClaim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EPSReportReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(EPSReportReader.class);

    public static List<ReportClaim> readFile(File csvFile) {
        try {
            MappingIterator<ReportClaim> epsClaimMappingIterator = new CsvMapper().readerWithTypedSchemaFor(ReportClaim.class).readValues(csvFile);
            return epsClaimMappingIterator.readAll();
        } catch (IOException e) {
            LOGGER.error("Error while loading {} file: {}", csvFile, e.toString());
            return Collections.emptyList();
        }
    }
}
