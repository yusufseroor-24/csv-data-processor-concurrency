package com.ga.CSVProcessor.model;

import com.ga.CSVProcessor.enums.Roles;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * One employee row read from the CSV
 * records are immutable and cannot be changed so it can be shared between threads
 * @param id
 * @param name
 * @param salary
 * @param joinedDate
 * @param role
 * @param projectCompletion
 */

public record Employee (int id,
                       String name,
                       BigDecimal salary,
                       LocalDate joinedDate,
                       Roles role,
                       double projectCompletion){
}
