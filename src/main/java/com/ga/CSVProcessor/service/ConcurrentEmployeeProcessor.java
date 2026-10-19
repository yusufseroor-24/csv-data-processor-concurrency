package com.ga.CSVProcessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConcurrentEmployeeProcessor {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentEmployeeProcessor.class);
    private static final int THREAD_COUNT = 4;

    private final SalaryCalculator salaryCalculator;

    public ConcurrentEmployeeProcessor(SalaryCalculator salaryCalculator) {
        this.salaryCalculator = salaryCalculator;
    }

}
