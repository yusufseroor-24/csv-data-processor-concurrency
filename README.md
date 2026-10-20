Concurrent Employee CSV Data Processor
- 
As spring boot app that reads employees data from a CSV file, calculate salary increase concurrently using using a thread pool, and writes the results to a new CSV

## Salary Rules
1. **Completion below 60%:** no increase
2. **Role:** Director +5%, Manager +2%, Employee +1%
3. **Years worked:** +2% per full year, only after completing one year
The role and years raises are added together

How It works
- 
| Class | Responsibility |
|---|---|
| `CsvReader` | Reads the CSV into `Employee` records |
| `SalaryCalculator` | Applies the salary rules |
| `ConcurrentEmployeeProcessor` | Processes employees in parallel |
| `CsvWriter` | Writes old and new salaries to the output CSV |

## Workflow
CsvReader → ConcurrentEmployeeProcessor → CsvWriter

- **Input:** `src/main/resources/test_employees.csv`
- **Output:** `output/updated_employees.csv`

Concurrency
- 
- **Thread pool:** ExecutorService with 4 threads
- **Lock:** `ReentrantLock` protects the shared results list so no result is lost
- `Employee` is immutable and `SalaryCalculator` is stateless, so both are safe without locks

Sample Input Format 
-
Columns: `id,name,salary,joinedDate,role,projectCompletion`
```
1,Alice,52000.0,2019-05-12,Employee,0.8
```
`projectCompletion` is a fraction (0.8 = 80%). Invalid lines are skipped and logged.

Sample Output
- 
```
id,name,role,oldSalary,raisePercent,newSalary
1,Alice,EMPLOYEE,52000.00,15,59800.00
6,Frank,DIRECTOR,83000.00,0,83000.00
```
