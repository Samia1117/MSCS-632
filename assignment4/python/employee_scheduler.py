# employee_scheduler.py
# Assignment 4 - Employee shift scheduling application (Python)
#
# Manages a 7-day weekly schedule across morning, afternoon, and evening shifts.
# Rules:
#   - No employee works more than one shift per day
#   - Max 5 days per employee per week
#   - At least 2 employees per shift per day (randomly filled if short)
#   - Max 3 employees per shift per day (triggers conflict resolution)
#   - Conflicts: if preferred shift is full, try alternate shift same day,
#     then fall back to preferred shift on the next available day

import random

DAYS   = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
SHIFTS = ["morning", "afternoon", "evening"]

MAX_DAYS_PER_WEEK = 5
MIN_PER_SHIFT     = 2
MAX_PER_SHIFT     = 3   # cap per slot so conflicts can arise

EMPLOYEE_PREFS = [
    ("Alice",  "morning"),
    ("Bob",    "afternoon"),
    ("Carol",  "evening"),
    ("David",  "morning"),
    ("Eve",    "afternoon"),
    ("Frank",  "evening"),
    ("Grace",  "morning"),
    ("Henry",  "afternoon"),
    ("Iris",   "evening"),
    ("James",  "morning"),
    ("Karen",  "afternoon"),
    ("Leo",    "evening"),
    ("Mia",    "morning"),
    ("Noah",   "afternoon"),
]


def build_schedule(seed=42):
    random.seed(seed)

    # schedule[day][shift] -> list of assigned employee names
    schedule = {d: {s: [] for s in SHIFTS} for d in DAYS}

    days_worked   = {name: 0        for name, _ in EMPLOYEE_PREFS}
    assigned_days = {name: set()    for name, _ in EMPLOYEE_PREFS}
    employees     = [{"name": n, "pref": p} for n, p in EMPLOYEE_PREFS]
    conflicts     = []

    # Phase 1: assign employees to their preferred shifts
    for day in DAYS:
        # randomize order so no employee always gets first pick
        daily_order = employees[:]
        random.shuffle(daily_order)

        for emp in daily_order:
            name, pref = emp["name"], emp["pref"]

            if day in assigned_days[name]:
                continue
            if days_worked[name] >= MAX_DAYS_PER_WEEK:
                continue

            if len(schedule[day][pref]) < MAX_PER_SHIFT:
                # preferred slot available
                schedule[day][pref].append(name)
                assigned_days[name].add(day)
                days_worked[name] += 1
            else:
                # conflict: preferred shift is full for this day
                resolved = False

                # try alternate shifts on the same day
                for alt in SHIFTS:
                    if alt == pref:
                        continue
                    if len(schedule[day][alt]) < MAX_PER_SHIFT:
                        conflicts.append(
                            f"{name}: preferred '{pref}' full on {day} -> moved to '{alt}'"
                        )
                        schedule[day][alt].append(name)
                        assigned_days[name].add(day)
                        days_worked[name] += 1
                        resolved = True
                        break

                if not resolved:
                    # all shifts full on this day: push to next available day
                    day_idx = DAYS.index(day)
                    for next_day in DAYS[day_idx + 1:]:
                        if next_day in assigned_days[name]:
                            continue
                        if days_worked[name] >= MAX_DAYS_PER_WEEK:
                            break
                        if len(schedule[next_day][pref]) < MAX_PER_SHIFT:
                            conflicts.append(
                                f"{name}: no shift available on {day} -> pushed to '{pref}' on {next_day}"
                            )
                            schedule[next_day][pref].append(name)
                            assigned_days[name].add(next_day)
                            days_worked[name] += 1
                            break

    # Phase 2: ensure every shift has at least MIN_PER_SHIFT employees
    for day in DAYS:
        for shift in SHIFTS:
            while len(schedule[day][shift]) < MIN_PER_SHIFT:
                available = [
                    e for e in employees
                    if day not in assigned_days[e["name"]]
                    and days_worked[e["name"]] < MAX_DAYS_PER_WEEK
                ]
                if not available:
                    print(f"[warning] cannot fill '{shift}' on {day} - no employees left")
                    break
                pick = random.choice(available)
                schedule[day][shift].append(pick["name"])
                assigned_days[pick["name"]].add(day)
                days_worked[pick["name"]] += 1

    return schedule, days_worked, conflicts


def print_schedule(schedule, days_worked, conflicts):
    width = 55
    print("=" * width)
    print("         WEEKLY EMPLOYEE SCHEDULE")
    print("=" * width)

    for day in DAYS:
        print(f"\n  {day}:")
        for shift in SHIFTS:
            names = schedule[day][shift]
            label = shift.capitalize()
            value = ", ".join(names) if names else "(unassigned)"
            print(f"    {label:<13} {value}")

    print("\n" + "-" * width)
    print("  Days worked per employee this week:")
    for name, count in sorted(days_worked.items()):
        bar = "#" * count + "." * (MAX_DAYS_PER_WEEK - count)
        print(f"    {name:<8}  {count}/{MAX_DAYS_PER_WEEK}  [{bar}]")

    if conflicts:
        print("\n" + "-" * width)
        print("  Conflict resolutions:")
        for c in conflicts:
            print(f"    * {c}")

    print("=" * width)


if __name__ == "__main__":
    schedule, days_worked, conflicts = build_schedule(seed=42)
    print_schedule(schedule, days_worked, conflicts)
