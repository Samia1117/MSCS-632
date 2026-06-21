# Assignment 4: Employee Shift Scheduler

A weekly employee scheduling application implemented in both Python and Java.
The program assigns employees to morning, afternoon, and evening shifts across a 7-day week,
enforces scheduling rules, detects conflicts, and prints the final schedule.

## Scheduling Rules

- No employee works more than one shift per day
- Maximum 5 days per employee per week
- Maximum 3 employees per shift per day (triggers conflict resolution)
- Minimum 2 employees per shift per day (randomly filled from available employees if short)
- If an employee's preferred shift is full, they are moved to an alternate shift the same day,
  or pushed to their preferred shift on the next available day

## Project Structure

```
assignment4/
├── python/
│   └── employee_scheduler.py
└── java/
    └── EmployeeScheduler.java
```

## Running the Python Version

Requires Python 3.6+. No external dependencies.

```bash
cd python
python3 employee_scheduler.py
```

## Running the Java Version

Requires Java 11+.

```bash
cd java
javac EmployeeScheduler.java
java EmployeeScheduler
```

## Sample Output

Both versions produce a readable weekly schedule, a per-employee days-worked summary,
and a log of any conflicts that were resolved during scheduling.

```
=======================================================
         WEEKLY EMPLOYEE SCHEDULE
=======================================================

  Monday:
    Morning       Alice, David, Grace
    Afternoon     Bob, Eve, Henry
    Evening       Carol, Frank, Iris

  ...

-------------------------------------------------------
  Days worked per employee this week:
    Alice     5/5  [#####]
    Bob       4/5  [####.]
    ...

-------------------------------------------------------
  Conflict resolutions:
    * James: preferred 'morning' full on Monday -> moved to 'afternoon'
    ...
=======================================================
```
