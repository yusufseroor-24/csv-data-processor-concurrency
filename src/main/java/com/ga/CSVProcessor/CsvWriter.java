package com.ga.CSVProcessor;

import com.ga.CSVProcessor.model.Employee;
import com.ga.CSVProcessor.model.SalaryTotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * writes new salary result to CSV file with a header in output/
 * Columns: id,name,role,oldSalary,raisePercent,newSalary
 */

@Service
public class CsvWriter {

    private static final Logger log = LoggerFactory.getLogger(CsvWriter.class);
    private static final String HEADER = "id,name,role,oldSalary,raisePercent,newSalary";

    public void write(List<SalaryTotal> results, Path outputFile) throws IOException {
        Files.createDirectories(outputFile.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write(HEADER); //add the header
            writer.newLine(); //add a line after header

            //for each employee --> write
            for (SalaryTotal result : results) {
                writer.write(toCsvLine(result));
                writer.newLine();
            }
        }
        log.info("Wrote {} rows to {}", results.size(), outputFile.toAbsolutePath());
    }

    //turning a result into Csv line
    private String toCsvLine(SalaryTotal result) {
        Employee employee = result.employee();

        BigDecimal oldSalary = employee.salary().setScale(2, RoundingMode.HALF_UP); //2 decimals
        //calculates %
        String raisePercent = result.raiseRate().multiply(BigDecimal.valueOf(100)).stripTrailingZeros()
                .toPlainString();

        return employee.id() + "," + employee.name() + ","
                + employee.role() + "," + oldSalary + ","
                + raisePercent + "," + result.newSalary();
    }
}
