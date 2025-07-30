# Employee Check-In Check-Out

> OVERVIEW

A Command Line Interface (CLI) based Employee Check-In System using Kotlin. The system is designed to track employee check-ins per day and ensure each employee checks in only once per day.
Here number of employees will be added. Based on the input, employee records will be created based on user input.

>  DATA CLASSES

**1.Employee**
  - employeeID :Int
  - firstName :String 
  - lastName :String
  - role :String
  - contactNumber :Long
  - reportingTo: Int
    
**2.EmployeeAttendance**
  - employeeId :Int
  - checkInDateTime :LocalDateTime
  - checkOutDateTime :LocalDateTime
  - workingHours: LocalTime

> FUNCTIONS
  
**1.addEmployee()**
  - gets firstName,lastName,role,contactNumber and reportingTo from user and creates an id automatically.
  - stores in employees using Employee data class.

**2.employeeList()**
  - details of all employees will be returned.
    
**3.createCheckIn()**
  - gets user id and takes dateTime and validates id using validateID() and validateAttendance().
  - stores data in checkedInDetails map using CheckIn(Data class).

**4.validateEmployeeID()**
  - checks whether id is present in employeeDetails map.

**5.validateAttendance()**
  - checks whether id is present in checkedInDetails map.

