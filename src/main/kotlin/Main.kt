import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Duration

data class DataEmployee(
    val employeeID: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: String,
    val reportingTo: Int
) {
    override fun toString(): String {
        return "Employee ID: $employeeID, Name: $firstName $lastName, Role: $role, Contact: $contactNumber, Reporting Manager ID: $reportingTo"
    }
}

data class DataAttendance(
    val employeeID: Int,
    val checkInDateTime: LocalDateTime,
    var checkOutDateTime: LocalDateTime? = null,
    var workingHours: LocalTime? = null
)

open class Employee() {

    val employees = mutableListOf<DataEmployee>()
    var employeeID = 1

    // Adds the employee into the database
    fun addEmployee(firstName: String, lastName: String, role: String, contactNumber: String, reportingTo: Int) {
        //adds the employee detail into the employee database using Employee data class
        employees.add(DataEmployee(employeeID, firstName, lastName, role, contactNumber, reportingTo))
        //increments the employee id for dynamic id generation
        employeeID++
    }

    // Prints the list of employees
    fun employeeList() {
        for (employee in employees) {
            println(employee)
        }
    }
}

class Attendance():Employee() {

    val attendanceList = mutableListOf<DataAttendance>()
    fun validateInputCheckIn(userDate: String?, userTime: String?): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        return try {
            if (!userDate.isNullOrBlank() && !userTime.isNullOrBlank()) {
                val dateTimeStr = "$userDate $userTime"
                val inputDateTime = LocalDateTime.parse(dateTimeStr, formatter)
                val now = LocalDateTime.now().withSecond(0).withNano(0)

                if (inputDateTime.isAfter(now)) {
                    println("Date/time is in the future.")
                    null
                } else {
                    inputDateTime
                }
            } else {
                println("Both Check-In date and time must be entered.")
                null
            }
        } catch (e: Exception) {
            println("Invalid format. Use DD-MM-YYYY and HH:MM.")
            null
        }
    }

    fun inputCheckOut(userDate: String?, userTime: String?): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        return try {
            if (!userDate.isNullOrBlank() && !userTime.isNullOrBlank()) {
                val dateTimeStr = "$userDate $userTime"
                val inputDateTime = LocalDateTime.parse(dateTimeStr, formatter)
                val now = LocalDateTime.now().withSecond(0).withNano(0)

                if (inputDateTime.isAfter(now)) {
                    println("Date/time is in the future.")
                    null
                } else {
                    inputDateTime
                }
            } else {
                println("Both Check-Out date and time must be entered.")
                null
            }
        } catch (e: Exception) {
            println("Invalid format. Use DD-MM-YYYY and HH:MM.")
            null
        }
    }


    // checks whether the list of employee has the input employee id
    fun validateEmployeeID(empID: Int): Boolean {
        return employees.any { it.employeeID == empID }
    }

    // function that validates the existing check-in of the employee for the check-in date
    fun validateAttendance(empID: Int, checkInDate: LocalDateTime): Boolean {
        return attendanceList.none { it.employeeID == empID && it.checkInDateTime.toLocalDate() == checkInDate.toLocalDate() }
    }

    fun createCheckIn(empID: Int, checkInDate: LocalDateTime): String {
        try {
            if (validateAttendance(empID, checkInDate)) {
                attendanceList.add(DataAttendance(empID, checkInDate))
                return "true"
            } else {
                return "Employee $empID has checked-in already for the day ${checkInDate.toLocalDate()}"
            }
        } catch (e: Exception) {
            println("Error occurred: ${e.message}")
            return "e"
        }
    }

    fun createCheckOut(empID: Int, checkOutDate: LocalDateTime?): String {
        try {
            val attendanceIndex = attendanceList.indexOfFirst {
                it.employeeID == empID && it.checkInDateTime.toLocalDate() == checkOutDate?.toLocalDate()
            }

            if (attendanceIndex != -1) {
                val attendance = attendanceList[attendanceIndex]

                //validating whether checkOutDateTime is not entered already and the checkOutDate is not greater than the checkInDateTime
                if (checkOutDate != null) {
                    if (attendance.checkOutDateTime == null && checkOutDate.isAfter(attendance.checkInDateTime)) {
                        //java duration is used to find the difference of time between the check-in and check-out.
                        //toMinute() helps to convert the obtained value(in seconds) to minutes.
                        val workingMinutes = Duration.between(attendance.checkInDateTime, checkOutDate).toMinutes()

                        //formatting the obtained minutes of working into hour format
                        val formattedWorkingHours =
                            LocalTime.of((workingMinutes / 60).toInt(), (workingMinutes % 60).toInt())

                        //updates the attendanceList data class with the check-out time and working hours.
                        attendanceList[attendanceIndex].checkOutDateTime = checkOutDate
                        attendanceList[attendanceIndex].workingHours = formattedWorkingHours

                        return "true"
                    } else if (checkOutDate.isBefore(attendance.checkInDateTime) || checkOutDate.isAfter(LocalDateTime.now())) {
                        return "timeError"
                    } else {
                        return "false"
                    }
                }
            }
            return "null"
        } catch (e: Exception) {
            println("Error occurred: ${e.message}")
            return e.toString()
        }
    }

    fun workingHoursList(attendanceList: List<DataAttendance>): String {
        if (attendanceList.isEmpty()) {
            return "No attendance records found.\nPlease make sure you have checked in to generate attendance report."
        }
        return attendanceList.joinToString(separator = "\n") { recordAttendance ->
            val employee = employees.find { it.employeeID == recordAttendance.employeeID }
            val employeeName = if (employee != null) "${employee.firstName} ${employee.lastName}" else "Unknown"

            "Employee ID: ${recordAttendance.employeeID}, Name: $employeeName, " +
                    "Check-In: ${recordAttendance.checkInDateTime}, " +
                    "Check-Out: ${recordAttendance.checkOutDateTime ?: "Yet to Check Out"}, " +
                    "Working Hours: ${recordAttendance.workingHours ?: "N/A"}"
        }
    }
}


fun main() {
    val employeeService = Employee()
    val attendanceService = Attendance()
    var isRunning = true

    while (isRunning) {
        println("\n--- Employee Attendance System ---")
        println("1. Add Employee")
        println("2. View Employee List")
        println("3. Add Check-In")
        println("4. Add Check-Out")
        println("5. View Working Hours")
        println("6. Exit")
        print("Enter your choice: ")

        when (readlnOrNull()?.trim()) {
            "1" -> {
                var addingEmployee = true

                while (addingEmployee) {
                    println("Enter details for new employee")

                    print("First Name: ")
                    val firstName = readln()

                    print("Last Name: ")
                    val lastName = readln()

                    val fullName = "$firstName $lastName"

                    print("Role of $fullName: ")
                    val role = readln()

                    print("Contact Number of $fullName: ")
                    val contactNumber = readln()

                    print("Reporting To (Manager ID): ")
                    val reportingTo = readln().toIntOrNull() ?: 0

                    employeeService.addEmployee(firstName, lastName, role, contactNumber, reportingTo)
                    println("Employee '$firstName $lastName' added successfully!\n")

                    println("Do you want to add another employee? (Yes/No): ")
                    val response = readlnOrNull()?.trim()?.lowercase()
                    if (response != "yes" && response != "y") {
                        addingEmployee = false
                    }
                }

            }

            "2" -> employeeService.employeeList()

            "3" -> {
                println("Enter Employee ID for Check-In:")
                val empID = readln().toIntOrNull() ?: 0
                if (!attendanceService.validateEmployeeID(empID))
                {
                    println("Employee ID $empID not found.")
                }
                else
                {
                    var checkinDate: LocalDateTime? = null
                    while (checkinDate == null)
                    {
                        println("Would you like to enter the Check-In date and time? (Yes/No):")
                        val response = readlnOrNull()?.trim()?.lowercase()

                        if (response == "no" || response == "n")
                        {
                            checkinDate = LocalDateTime.now().withSecond(0).withNano(0)
                            println("Proceeding with current date/time: $checkinDate")
                        }
                        else
                        {
                            println("Enter Check-In Date (DD-MM-YYYY):")
                            val date = readlnOrNull()?.trim()

                            println("Enter Check-In Time (HH:MM):")
                            val time = readlnOrNull()?.trim()

                            checkinDate = attendanceService.validateInputCheckIn(date, time)
                        }
                    }

                    if (attendanceService.validateAttendance(empID, checkinDate)) {
                        val flagCheckin = attendanceService.createCheckIn(empID, checkinDate)
                        if (flagCheckin=="true") {
                            println("Check-In created for Employee ID $empID.")
                        }
                        else
                        {
                            println(flagCheckin)
                        }
                    }
                    else
                    {
                        println("Already checked-in for the date ${checkinDate.toLocalDate()}.")
                    }
                }
            }

            "4" -> {
                println("Enter Employee ID for Check-Out:")
                val empID = readln().toIntOrNull() ?: 0
                if (!attendanceService.validateEmployeeID(empID)) {
                    println("Employee ID $empID not found.")
                }
                else
                {
                    val checkin = attendanceService.attendanceList.find { it.employeeID == empID && it.checkOutDateTime == null }
                    if (checkin == null)
                    {
                        println("No active Check-In found for Employee ID $empID.")
                    }
                    else
                    {
                        // Use inputCheckOut() to validate user input and default if necessary
                        var checkOutDate: LocalDateTime? = null
                        while (checkOutDate == null) {
                            println("Would you like to enter the Check-Out date and time? (Yes/No):")
                            val response = readlnOrNull()?.trim()?.lowercase()

                            if (response == "no" || response == "n") {
                                checkOutDate = LocalDateTime.now().withSecond(0).withNano(0)
                                println("Proceeding with current date/time: $checkOutDate")
                            } else {
                                println("Enter Check-Out Date (DD-MM-YYYY):")
                                val date = readlnOrNull()?.trim()

                                println("Enter Check-Out Time (HH:MM):")
                                val time = readlnOrNull()?.trim()

                                checkOutDate = attendanceService.inputCheckOut(date, time)
                            }
                        }


                        val flagCheckout = attendanceService.createCheckOut(empID, checkOutDate)

                        when (flagCheckout) {
                            "true" -> {
                                println("Checkout for the Employee ID $empID has been created successfully")
                            }
                            "timeError" -> {
                                println("Oops! The Check-Out time $checkOutDate is behind the Check-In date time")
                            }
                            "null" -> {
                                println("Oops! The Employee has not checked-in for the day ${checkOutDate.toLocalDate()}")
                            }
                            "false" ->{
                                println("Employee have already checked-out for the day ${checkOutDate.toLocalDate()}")
                            }
                            else -> {
                                {
                                    println("Oops! $flagCheckout")
                                }
                            }
                        }
                    }
                }
            }

            "5" -> {
                println("Employee Working Hours Report: ")
                val workingHoursList = attendanceService.workingHoursList(attendanceService.attendanceList)
                println(workingHoursList)
            }

            "6" -> {
                println("Exiting system. Goodbye!")
                isRunning = false
            }

            else -> println("Invalid option. Please try again.")
        }
    }
}
