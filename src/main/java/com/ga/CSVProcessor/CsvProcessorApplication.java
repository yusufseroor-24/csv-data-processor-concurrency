package com.ga.CSVProcessor;

import com.ga.CSVProcessor.model.Employee;
import com.ga.CSVProcessor.model.SalaryTotal;
import com.ga.CSVProcessor.service.ConcurrentEmployeeProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CsvProcessorApplication {

	public static void main(String[] args) throws Exception{
		//holds all @services
		ConfigurableApplicationContext context = SpringApplication.run(CsvProcessorApplication.class, args);

		CsvReader reader = context.getBean(CsvReader.class);
		ConcurrentEmployeeProcessor processor = context.getBean(ConcurrentEmployeeProcessor.class);
		CsvWriter writer = context.getBean(CsvWriter.class);

		List<Employee> employees = reader.read(new ClassPathResource("test_employees.csv"));
		List<SalaryTotal> results = processor.process(employees, LocalDate.now());
		writer.write(results, Path.of("output/updated_employees.csv"));

		context.close();

	}

}
