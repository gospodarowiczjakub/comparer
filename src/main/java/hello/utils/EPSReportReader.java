package hello.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import hello.model.EPSClaim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EPSReportReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(EPSReportReader.class);

    public static List<EPSClaim> readFile(File csvFile) {
        try {
            MappingIterator<EPSClaim> epsClaimMappingIterator = new CsvMapper().readerWithTypedSchemaFor(EPSClaim.class).readValues(csvFile);
            return epsClaimMappingIterator.readAll();
        } catch (IOException e) {
            LOGGER.error("Error while loading {} file: {}", csvFile, e.toString());
            return Collections.emptyList();
        }
    }
}
