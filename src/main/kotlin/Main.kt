import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Employee(
    val employeeID: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: String,
    val reportingTo: Int
)

data class EmployeeAttendance(
    val employeeID: Int,
    val checkInDateTime: LocalDateTime,
    val checkOutDateTime: LocalDateTime? = null,
    val workingHours: LocalTime?= null
)

val employees = mutableListOf<Employee>()

val numberOfEmployee = readln().toIntOrNull() ?: 0
var employeeID = 1
val attendanceList = mutableListOf<EmployeeAttendance>()


// Adds the employee into the database
fun addEmployee(firstName: String, lastName: String, role: String, contactNumber: String, reportingTo: Int)
{
    //adds the employee detail into the employee database using Employee data class
    employees.add(Employee(employeeID, firstName, lastName, role, contactNumber, reportingTo))
    //increments the employee id for dynamic id generation
    employeeID++
}



// Prints the employees list
fun employeeList()
{
    println("List of Employees")
    for(employee in employees)
    {
        println("Employee ID: ${employee.employeeID}")
        println("Name: ${employee.firstName} ${employee.lastName}")
        println("Role: ${employee.role}")
        println("Contact Number: ${employee.contactNumber}")
        println("Reporting To: ${employee.reportingTo}")
        println("--------------------------------------")
    }
}

fun inputCheckIn(userDate: String?, userTime: String?): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val now = LocalDateTime.now().withSecond(0).withNano(0)

    return if (!userDate.isNullOrBlank() && !userTime.isNullOrBlank()) {
        try {
            val dateTimeStr = "$userDate $userTime"
            val inputDateTime = LocalDateTime.parse(dateTimeStr, formatter)

            return if (inputDateTime <= now)
            {
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


// checks whether the list of employee has the input employee id
fun validateEmployeeID(empID: Int): Boolean{
    return employees.any{it.employeeID == empID}
}

// function that validates the existing check-in of the employee for the check-in date
fun validateAttendance(empID: Int, checkInDate: LocalDateTime): Boolean{
    return attendanceList.none { it.employeeID == empID && it.checkInDateTime.toLocalDate() == checkInDate.toLocalDate() }
}

// creating check-in for the check-in date
fun createCheckin(empID: Int, checkInDate: LocalDateTime)
{
    try {
        //checks the attendanceList, whether the system has attendance for the check-in date.
        // If not this adds the date and employee details into the .
        attendanceList.add(EmployeeAttendance(empID, checkInDate))
    }
    catch (e: Exception)
    {
        println("$e")
    }
}

fun main()
{
    for (i in 1..numberOfEmployee) {
        println("Enter the first name of employee #$i:")
        val firstName = readln()

        println("Enter the last name of $firstName:")
        val lastName = readln()

        val fullName = "$firstName $lastName"

        println("Enter the role of $fullName:")
        val role = readln()

        println("Enter the contact number of $fullName:")
        val contactNumber = readln()

        println("Enter the reporting person ID of $fullName:")
        val reportingTo = readln().toInt()

        // Call the modified addEmployee with parameters
        addEmployee(firstName, lastName, role, contactNumber, reportingTo)
    }
    if (numberOfEmployee != 0) {
        println("Employee list has been updated successfully !")

        //calling the function employeeList to print the list of employees in the database
        employeeList()

        println("Enter the Employee ID to check-in")

        //user enters the employee id for check-in
        val empID: Int = readln().toIntOrNull() ?: 0

        if (empID != 0) {
            //checks the employeeId has in the database
            if (!(validateEmployeeID(empID)))
            {
                println("Oops ! The employee ID $empID does not exist in the database.")
            }
            else
            {
                println("Would you like to enter the check-in date and time? (Yes/No):")
                val checkInResponse = readlnOrNull()?.lowercase()

                var userDate: String? = null
                var userTime: String? = null

                if (checkInResponse == "yes" || checkInResponse == "y") {
                    println("Please Enter the Date of Check-In (Format: DD-MM-YYYY):")
                    userDate = readln()

                    println("Please Enter the Time of Check-In (Format: HH:MM):")
                    userTime = readln()
                }

                // Now pass input to inputCheckIn()
                val checkinDate = inputCheckIn(userDate, userTime)

                if (validateAttendance(empID, checkinDate)) {
                    println("Creating Check-In for the employee ID $empID")

                    //creating check-in if the validAttendance function is passed
                    createCheckin(empID, checkinDate)
                }
                else
                {
                    println("Oops! The employee ID $empID has already checked-in for the day.")
                }
            }
        }

    }
    else
    {
        println("Oops! Number of employees has been entered incorrectly")
    }
}