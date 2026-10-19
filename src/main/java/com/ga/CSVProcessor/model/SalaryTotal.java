package com.ga.CSVProcessor.model;

import java.math.BigDecimal;

/**
 * The result of calculating an employee raise
 * @param employee
 * @param raiseRate
 * @param newSalary
 */

public record SalaryTotal(
        Employee employee,
        BigDecimal raiseRate,
        BigDecimal newSalary
){
}
