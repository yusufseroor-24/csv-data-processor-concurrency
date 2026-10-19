package com.ga.CSVProcessor;

import com.ga.CSVProcessor.enums.Roles;
import com.ga.CSVProcessor.model.Employee;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CvsReader {

    private static final Logger log  = LoggerFactory.getLogger(CvsReader.class);
    private static final int EXPECTED_CVS_COLUMNS = 6;

    public List<Employee> read(Resource resource) throws IOException{
        List<Employee> employees = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))){
            String line;
            int lineNumber = 0;

            while((line = reader.readLine()) != null){
                lineNumber++;

                if(line.isBlank()){
                    continue;
                }
                try{
                    employees.add(parseLine(line));
                }catch (RuntimeException e){
                    log.warn("Skipping line {}: '{}' ({})", lineNumber, line, e.getMessage());
                }
            }
        }
        log.info("Read {} employees from {}", employees.size(), resource.getFilename());
        return employees;
    }

    Employee parseLine(String line){
        String[] p = line.split(",", -1);
        if(p.length !=EXPECTED_CVS_COLUMNS){
            throw new IllegalArgumentException("expected " + EXPECTED_CVS_COLUMNS + " columns but found " + p.length);
        }

        int id = Integer.parseInt(p[0].trim());
        String name = p[1].trim();
        BigDecimal salary = new BigDecimal(p[2].trim());
        LocalDate joinedDate = LocalDate.parse(p[3].trim());
        Roles role = Roles.fromString(p[4]);
        double projectCompletion = Double.parseDouble(p[5].trim());

        return new Employee(id, name, salary, joinedDate, role, projectCompletion);
    }
}
