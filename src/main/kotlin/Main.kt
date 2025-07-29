import java.time.LocalDate

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
    val checkInDate: String,
)

val employees = mutableListOf<Employee>()

val numberOfEmployee = readln().toIntOrNull() ?: 0
var employeeID = 1
val attendanceList = mutableListOf<EmployeeAttendance>()


// Adds the employee into the database
fun addEmployee()
{
    println("Enter the first name of $employeeID")
    val firstName = readln()
    println("Enter the last name of $employeeID")
    val lastName = readln()
    val fullName = "$firstName $lastName"

    println("Enter the role of $fullName")
    val role = readln()
    println("Enter the contact number of $fullName")
    val contactNumber = readln()
    println("Enter the reporting person ID of $fullName")
    val reportingTo = readln().toInt()

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

// checks whether the list of employee has the input employee id
fun validateEmployeeID(empID: Int): Boolean{
    return employees.any{it.employeeID == empID}
}

// function that validates the existing check-in of the employee for the check-in date
fun validateAttendance(empID: Int, checkInDate: String): Boolean{
    return attendanceList.none { it.employeeID == empID && it.checkInDate == checkInDate }
}

// creating checkin for the check-in date
fun createCheckin(empID: Int, checkInDate: String)
{
    try {
        //checks the attendanceList, whether the system has attendance for the check-in date.
        // If not this add the date.
        attendanceList.add(EmployeeAttendance(empID, checkInDate))
        println("AttendanceList $attendanceList")
    }
    catch (e: Exception)
    {
        println("$e")
    }
}

fun main()
{
    for (i in 1..numberOfEmployee) {
        //calling the function addEmployee to add the employees
        addEmployee()
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
                val checkinDate: String = LocalDate.now().toString()

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