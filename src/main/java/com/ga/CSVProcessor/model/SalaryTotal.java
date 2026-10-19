package com.ga.CSVProcessor.model;

import java.math.BigDecimal;

public record SalaryTotal(
        Employee employee,
        BigDecimal raiseRate,
        BigDecimal newSalary
){
}
