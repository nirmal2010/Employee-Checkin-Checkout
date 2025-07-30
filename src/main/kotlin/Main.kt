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
)

data class DataAttendance(
    val employeeID: Int,
    val checkInDateTime: LocalDateTime,
    var checkOutDateTime: LocalDateTime? = null,
    var workingHours: LocalTime? = null
)

val employees = mutableListOf<DataEmployee>()
val attendanceList = mutableListOf<DataAttendance>()
var employeeID = 1

// Adds the employee into the database
fun addEmployee(firstName: String, lastName: String, role: String, contactNumber: String, reportingTo: Int)
{
    //adds the employee detail into the employee database using Employee data class
    employees.add(DataEmployee(employeeID, firstName, lastName, role, contactNumber, reportingTo))
    //increments the employee id for dynamic id generation
    employeeID++
}

// Prints the list of employees
fun employeeList() {
    for (employee in employees) {
        println("Employee ID: ${employee.employeeID}")
        println("Name: ${employee.firstName} ${employee.lastName}")
        println("Role: ${employee.role}")
        println("Contact Number: ${employee.contactNumber}")
        println("Reporting To: ${employee.reportingTo}")
        println("--------------------------------------")
    }
}

fun validateInputCheckIn(userDate: String?, userTime: String?): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val now = LocalDateTime.now().withSecond(0).withNano(0)

    return if (!userDate.isNullOrBlank() && !userTime.isNullOrBlank()) {
        try {
            val dateTimeStr = "$userDate $userTime"
            val inputDateTime = LocalDateTime.parse(dateTimeStr, formatter)

            return if (inputDateTime <= now) {
                inputDateTime
            }
            else
            {
                println("Input date/time is in the future. Using current date and time instead.")
                now
            }
        }
        catch (e: Exception)
        {
            println("Invalid date/time format. Using current date and time instead.")
            now
        }
    }
    else
    {
        println("Proceeding with current time and date as Check-In Date Time...")
        now
    }
}

fun inputCheckOut(userDate: String?, userTime: String?): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val now = LocalDateTime.now().withSecond(0).withNano(0)

    return if (!userDate.isNullOrBlank() && !userTime.isNullOrBlank()) {
        try {
            val dateTimeStr = "$userDate $userTime"
            val inputDateTime = LocalDateTime.parse(dateTimeStr, formatter)

            if (inputDateTime >= now) {
                println("Input date/time is in the future. Using current date and time instead.")
                return now
            }
            inputDateTime
        }
        catch (e: Exception)
        {
            println("Invalid date/time format. Using current date and time instead.")
            now
        }
    }
    else
    {
        println("Proceeding with current time and date as Check-Out Date Time...")
        now
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
        if (validateAttendance(empID, checkInDate))
        {
            attendanceList.add(DataAttendance(empID, checkInDate))
            return "true"
        }
        else
        {
            return "Employee $empID has checked-in already for the day ${checkInDate.toLocalDate()}"
        }
    }
    catch (e: Exception)
    {
        println("Error occurred: ${e.message}")
        return "e"
    }
}

fun createCheckOut(empID: Int, checkOutDate: LocalDateTime): String {
    try {
        val attendanceIndex = attendanceList.indexOfFirst {
            it.employeeID == empID && it.checkInDateTime.toLocalDate() == checkOutDate.toLocalDate()
        }

        if (attendanceIndex != -1) {
            val attendance = attendanceList[attendanceIndex]

            //validating whether checkOutDateTime is not entered already and the checkOutDate is not greater than the checkInDateTime
            if (attendance.checkOutDateTime == null && checkOutDate.isAfter(attendance.checkInDateTime)) {

                //java duration is used to find the difference of time between the check-in and check-out.
                //toMinute() helps to convert the obtained value(in seconds) to minutes.
                val workingMinutes = Duration.between(attendance.checkInDateTime, checkOutDate).toMinutes()

                //formatting the obtained minutes of working into hour format
                val formattedWorkingHours = LocalTime.of((workingMinutes / 60).toInt(), (workingMinutes % 60).toInt())

                //updates the attendanceList data class with the check-out time and working hours.
                attendanceList[attendanceIndex].checkOutDateTime = checkOutDate
                attendanceList[attendanceIndex].workingHours = formattedWorkingHours

                return "true"
            }
            else if(checkOutDate.isBefore(attendance.checkInDateTime) || checkOutDate.isAfter(LocalDateTime.now()))
            {
                return "timeError"
            }
            else
            {
                return "false"
            }
        }
        return "null"
    }
    catch (e: Exception)
    {
        println("Error occurred: ${e.message}")
        return e.toString()
    }
}

fun workingHoursList(attendanceList: List<DataAttendance>): String {
    if (attendanceList.isEmpty())
    {
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


fun main() {
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
                println("How many employees would you like to add?")
                val numEmployees = readln().toIntOrNull() ?: 0

                if (numEmployees <= 0)
                {
                    println("Invalid number. Returning to main menu.")
                }
                else {
                    for (i in 1..numEmployees)
                    {
                        println("Enter details for Employee #$i")

                        print("First Name of Employee $i: ")
                        val firstName = readln()

                        print("Last Name of Employee $i: ")
                        val lastName = readln()

                        val fullName = "$firstName $lastName"

                        print("Role of $fullName : ")
                        val role = readln()

                        print("Contact Number of $fullName: ")
                        val contactNumber = readln()

                        print("Reporting To (Manager ID): ")
                        val reportingTo = readln().toIntOrNull() ?: 0

                        addEmployee(firstName, lastName, role, contactNumber, reportingTo)
                        println("✅ Employee '$firstName $lastName' added successfully!\n")
                    }
                }

            }

            "2" -> employeeList()

            "3" -> {
                println("Enter Employee ID for Check-In:")
                val empID = readln().toIntOrNull() ?: 0
                if (!validateEmployeeID(empID))
                {
                    println("Employee ID $empID not found.")
                }
                else
                {
                    println("Enter Check-In Date (DD-MM-YYYY) or press Enter for today:")
                    val date = readln().ifBlank { null }
                    println("Enter Check-In Time (HH:MM) or press Enter for now:")
                    val time = readln().ifBlank { null }

                    val checkinDate = validateInputCheckIn(date, time)
                    if (validateAttendance(empID, checkinDate)) {
                        val flagCheckin = createCheckIn(empID, checkinDate)
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
                if (!validateEmployeeID(empID)) {
                    println("Employee ID $empID not found.")
                }
                else
                {
                    val checkin = attendanceList.find { it.employeeID == empID && it.checkOutDateTime == null }
                    if (checkin == null)
                    {
                        println("No active Check-In found for Employee ID $empID.")
                    }
                    else
                    {
                        println("Would you like to enter the Check-Out date and time? (Yes/No):")
                        val checkOutResponse = readlnOrNull()?.lowercase()

                        var userDate: String? = null
                        var userTime: String? = null

                        if (checkOutResponse == "yes" || checkOutResponse == "y") {
                            println("Enter Check-Out Date (DD-MM-YYYY):")
                            userDate = readln()

                            println("Enter Check-Out Time (HH:MM):")
                            userTime = readln()
                        }

                        // Use inputCheckOut() to validate user input and default if necessary
                        val checkOutDate = inputCheckOut(userDate, userTime)

                        val flagCheckout = createCheckOut(empID, checkOutDate)
                        println(flagCheckout)

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
                val workingHoursList = workingHoursList(attendanceList)
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
