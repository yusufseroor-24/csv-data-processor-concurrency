Concurrent Employee CSV Data Processor
- 
As spring boot app that reads employees data from a CSV file, calculate salary increase concurrently using using a thread pool, and writes the results to a new CSV

- **Input:** `src/main/resources/test_employees.csv`
- **Output:** `output/updated_employees.csv`

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

Concurrency
- 
- **Thread pool:** ExecutorService with 4 threads
- **Lock:** `ReentrantLock` protects the shared results list so no result is lost
- `Employee` is immutable and `SalaryCalculator` is stateless, so both are safe without locks

