package com.ga.CSVProcessor.service;

import com.ga.CSVProcessor.model.Employee;
import com.ga.CSVProcessor.model.SalaryTotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConcurrentEmployeeProcessor {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentEmployeeProcessor.class);
    private static final int THREAD_COUNT = 4;

    private final SalaryCalculator salaryCalculator;

    public ConcurrentEmployeeProcessor(SalaryCalculator salaryCalculator) {
        this.salaryCalculator = salaryCalculator;
    }

    public List<SalaryTotal> process(List<Employee> employees, LocalDate today) throws InterruptedException{
        List<SalaryTotal> results = new ArrayList<>(); //shared list for all 4 threads
        Lock resultsLock = new ReentrantLock();

        //creates 4 threads that are reused
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        try{
            for(Employee employee : employees){
                //adds a task to the pool queue
                executor.execute(() -> {
                    SalaryTotal result = salaryCalculator.calculate(employee, today);
                    resultsLock.lock();
                    try{
                        results.add(result);
                    }finally {
                        resultsLock.unlock();
                    }
                    log.info("[{}] processed {}: {} -> {}", Thread.currentThread().getName(), employee.name(),
                            employee.salary(), result.newSalary());
                });
            }
        }finally {
            executor.shutdown();
        }
        //pauses until ever task is done
        if(!executor.awaitTermination(1, TimeUnit.MINUTES)){
            executor.shutdownNow();
            throw new IllegalStateException("Processing didn't finish within 1 minute");
        }
        //put the result back in CSV order
        results.sort(Comparator.comparing(result -> result.employee().id()));

        //counts the results that raise rate is more than 0
        long raisedCount = results.stream().filter(result -> result.raiseRate().signum() > 0).count();

        log.info("Done: {} employees processed, {} got a raise, {} did not", results.size(), raisedCount, results.size() - raisedCount);

        return results;
    }

}
