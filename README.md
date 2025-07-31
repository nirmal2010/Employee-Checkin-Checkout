# Employee Check-In/Check-Out System


A **Kotlin-based Command Line Interface (CLI)** application to manage employee check-ins and check-outs. It ensures that each employee checks in once per day and calculates total working hours.

---

## Features

- Add new employees
- View employee list
- Check-in with optional manual time
- Check-out with optional manual time and auto-calculate working hours
- Prevent multiple check-ins and check-outs per day
- Display working hours report

---

## Data Models

### 1. `DataEmployee`
```
employeeID: Int
firstName: String
lastName: String
role: String
contactNumber: String
reportingTo: Int
```

### 2. `DataEmployee`
```
employeeID: Int,
checkInDateTime: LocalDateTime,
checkOutDateTime: LocalDateTime? = null,
workingHours: LocalTime? = null
```

---

## Funtions

### 1. `addEmployee()`
> Takes employee details as input.

> Generates a unique employee ID (numeric).

> Stores employee in the list.

### 2. `employeeList()`
> Prints details of all registered employees.

### 3. `createCheckIn()`
> Validates employee ID and duplicate check-ins.

> Adds check-in time for the current or specified date time.

### 4. `createCheckOut()`
> Allows check-out only if checked-in is present and not already checked-out.

> Calculates total working hours based on check-in/out.

### 5. `validateEmployeeID()`
> Checks if employee ID exists in the list.

### 6. `validateAttendance()`
> Checks if check-in already exists for the same date.

### 7. `workingHoursList()`
> Displays all check-in/out times and total hours worked.