package com.ga.CSVProcessor.service;

import com.ga.CSVProcessor.model.Employee;
import com.ga.CSVProcessor.model.SalaryTotal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

/**
 * applies salary increase to an employee
 * role raise and tenure raise are added
 * completion below 60% has no increase
 * role raise are Director 5%, Manager 2%, Employee 1%
 * Tenure raise: 2% for full year worked
 */

@Service
public class SalaryCalculator {
    private static final double MIN_COMPLETION = 0.6;
    private static final BigDecimal RATE_PER_YEAR = new BigDecimal("0.02");


    public SalaryTotal calculate(Employee employee, LocalDate today){
        if(employee.projectCompletion() < MIN_COMPLETION){
            BigDecimal sameSalary = employee.salary().setScale(2, RoundingMode.HALF_UP); //2 decimal places
            return new SalaryTotal(employee, BigDecimal.ZERO, sameSalary);
        }

        BigDecimal raiseRate = employee.role().getRaiseRate();

        //calculate the years worked by finding the gap
        int yearsWorked = Period.between(employee.joinedDate(), today).getYears();
        //if more than a year, calculate yearsRaise
        if(yearsWorked >= 1){
            BigDecimal yearsRaise = RATE_PER_YEAR.multiply(BigDecimal.valueOf(yearsWorked));
            raiseRate = raiseRate.add(yearsRaise);
        }

        BigDecimal newSalary = employee.salary().multiply(BigDecimal.ONE.add(raiseRate)).setScale(2, RoundingMode.HALF_UP);
        return new SalaryTotal(employee, raiseRate, newSalary);
    }
}
