// EmployeeScheduler.java
// Assignment 4 - Employee shift scheduling application (Java)
//
// Manages a 7-day weekly schedule across morning, afternoon, and evening shifts.
// Rules:
//   - No employee works more than one shift per day
//   - Max 5 days per employee per week
//   - At least 2 employees per shift per day (randomly filled if short)
//   - Max 3 employees per shift per day (triggers conflict resolution)
//   - Conflicts: if preferred shift is full, try alternate shift same day,
//     then fall back to preferred shift on the next available day
//
// Compile: javac EmployeeScheduler.java
// Run:     java EmployeeScheduler

import java.util.*;

public class EmployeeScheduler {

    static final String[] DAYS   = {"Monday", "Tuesday", "Wednesday", "Thursday",
                                     "Friday", "Saturday", "Sunday"};
    static final String[] SHIFTS = {"morning", "afternoon", "evening"};

    static final int MAX_DAYS_PER_WEEK = 5;
    static final int MIN_PER_SHIFT     = 2;
    static final int MAX_PER_SHIFT     = 3; // cap per slot so conflicts can arise

    static class Employee {
        String name;
        String preference;
        int    daysWorked;
        Set<String> assignedDays;

        Employee(String name, String preference) {
            this.name        = name;
            this.preference  = preference;
            this.daysWorked  = 0;
            this.assignedDays = new HashSet<>();
        }
    }

    // schedule.get(day).get(shift) -> list of assigned employee names
    static Map<String, Map<String, List<String>>> schedule = new LinkedHashMap<>();
    static List<String> conflicts = new ArrayList<>();

    static List<Employee> employees = new ArrayList<>(Arrays.asList(
        new Employee("Alice",  "morning"),
        new Employee("Bob",    "afternoon"),
        new Employee("Carol",  "evening"),
        new Employee("David",  "morning"),
        new Employee("Eve",    "afternoon"),
        new Employee("Frank",  "evening"),
        new Employee("Grace",  "morning"),
        new Employee("Henry",  "afternoon"),
        new Employee("Iris",   "evening"),
        new Employee("James",  "morning"),
        new Employee("Karen",  "afternoon"),
        new Employee("Leo",    "evening"),
        new Employee("Mia",    "morning"),
        new Employee("Noah",   "afternoon")
    ));

    static void initSchedule() {
        for (String day : DAYS) {
            Map<String, List<String>> dayMap = new LinkedHashMap<>();
            for (String shift : SHIFTS) {
                dayMap.put(shift, new ArrayList<>());
            }
            schedule.put(day, dayMap);
        }
    }

    static void buildSchedule(Random rand) {
        List<String> dayList = Arrays.asList(DAYS);

        // Phase 1: assign employees to their preferred shifts
        for (String day : DAYS) {
            // randomize order so no employee always gets first pick
            List<Employee> dailyOrder = new ArrayList<>(employees);
            Collections.shuffle(dailyOrder, rand);

            for (Employee emp : dailyOrder) {
                if (emp.assignedDays.contains(day)) continue;
                if (emp.daysWorked >= MAX_DAYS_PER_WEEK)  continue;

                List<String> prefSlot = schedule.get(day).get(emp.preference);

                if (prefSlot.size() < MAX_PER_SHIFT) {
                    // preferred slot available
                    prefSlot.add(emp.name);
                    emp.assignedDays.add(day);
                    emp.daysWorked++;
                } else {
                    // conflict: preferred shift is full for this day
                    boolean resolved = false;

                    // try alternate shifts on the same day
                    for (String alt : SHIFTS) {
                        if (alt.equals(emp.preference)) continue;
                        List<String> altSlot = schedule.get(day).get(alt);
                        if (altSlot.size() < MAX_PER_SHIFT) {
                            conflicts.add(emp.name + ": preferred '" + emp.preference
                                + "' full on " + day + " -> moved to '" + alt + "'");
                            altSlot.add(emp.name);
                            emp.assignedDays.add(day);
                            emp.daysWorked++;
                            resolved = true;
                            break;
                        }
                    }

                    if (!resolved) {
                        // all shifts full on this day: push to next available day
                        int dayIdx = dayList.indexOf(day);
                        for (int i = dayIdx + 1; i < DAYS.length; i++) {
                            String nextDay = DAYS[i];
                            if (emp.assignedDays.contains(nextDay)) continue;
                            if (emp.daysWorked >= MAX_DAYS_PER_WEEK)   break;

                            List<String> nextSlot = schedule.get(nextDay).get(emp.preference);
                            if (nextSlot.size() < MAX_PER_SHIFT) {
                                conflicts.add(emp.name + ": no shift available on " + day
                                    + " -> pushed to '" + emp.preference + "' on " + nextDay);
                                nextSlot.add(emp.name);
                                emp.assignedDays.add(nextDay);
                                emp.daysWorked++;
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Phase 2: ensure every shift has at least MIN_PER_SHIFT employees
        for (String day : DAYS) {
            for (String shift : SHIFTS) {
                List<String> slot = schedule.get(day).get(shift);
                while (slot.size() < MIN_PER_SHIFT) {
                    List<Employee> available = new ArrayList<>();
                    for (Employee e : employees) {
                        if (!e.assignedDays.contains(day) && e.daysWorked < MAX_DAYS_PER_WEEK) {
                            available.add(e);
                        }
                    }
                    if (available.isEmpty()) {
                        System.out.println("[warning] cannot fill '" + shift + "' on " + day
                            + " - no employees left");
                        break;
                    }
                    Employee pick = available.get(rand.nextInt(available.size()));
                    slot.add(pick.name);
                    pick.assignedDays.add(day);
                    pick.daysWorked++;
                }
            }
        }
    }

    static void printSchedule() {
        int width = 55;
        String sep  = "=".repeat(width);
        String dash = "-".repeat(width);

        System.out.println(sep);
        System.out.println("         WEEKLY EMPLOYEE SCHEDULE");
        System.out.println(sep);

        for (String day : DAYS) {
            System.out.println("\n  " + day + ":");
            for (String shift : SHIFTS) {
                List<String> names = schedule.get(day).get(shift);
                String label = capitalize(shift);
                String value = names.isEmpty() ? "(unassigned)" : String.join(", ", names);
                System.out.printf("    %-13s %s%n", label, value);
            }
        }

        System.out.println("\n" + dash);
        System.out.println("  Days worked per employee this week:");
        for (Employee emp : employees) {
            String bar = "#".repeat(emp.daysWorked) + ".".repeat(MAX_DAYS_PER_WEEK - emp.daysWorked);
            System.out.printf("    %-8s  %d/%d  [%s]%n",
                emp.name, emp.daysWorked, MAX_DAYS_PER_WEEK, bar);
        }

        if (!conflicts.isEmpty()) {
            System.out.println("\n" + dash);
            System.out.println("  Conflict resolutions:");
            for (String c : conflicts) {
                System.out.println("    * " + c);
            }
        }

        System.out.println(sep);
    }

    static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void main(String[] args) {
        initSchedule();
        buildSchedule(new Random(42));
        printSchedule();
    }
}
