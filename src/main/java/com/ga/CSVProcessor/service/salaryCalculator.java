package com.ga.CSVProcessor.service;

import com.ga.CSVProcessor.model.Employee;
import com.ga.CSVProcessor.model.SalaryTotal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class salaryCalculator {
    private static final double MIN_COMPLETION = 0.6;
    private static final BigDecimal RATE_PER_YEAR = new BigDecimal("0.02");


    public SalaryTotal calculate(Employee employee, LocalDate today){
        if(employee.projectCompletion() < MIN_COMPLETION){
            BigDecimal sameSalary = employee.salary().setScale(2, RoundingMode.HALF_UP); //2 decimal places
            return new SalaryTotal(employee, BigDecimal.ZERO, sameSalary);
        }

        BigDecimal raiseRate = employee.role().getRaiseRate();

        int yearsWorked = Period.between(employee.joinedDate(), today).getYears();
        if(yearsWorked >= 1){
            BigDecimal yearsRaise = RATE_PER_YEAR.multiply(BigDecimal.valueOf(yearsWorked));
            raiseRate = raiseRate.add(yearsRaise);
        }

    }
}
