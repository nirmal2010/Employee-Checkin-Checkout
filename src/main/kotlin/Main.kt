import com.sun.net.httpserver.Authenticator
import javax.swing.LayoutStyle

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
    val checkInTime: String,
)

val employees = mutableListOf<Employee>()

val numberOfEmployee = readln().toIntOrNull() ?: 0
var employeeID = 1

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

// Adds the employee into the database
fun addEmployee()
{
    println("Enter the first name of ${employeeID}")
    val firstName = readln()
    println("Enter the last name of ${employeeID}")
    val lastName = readln()
    val fullName = firstName+" "+lastName

    println("Enter the role of ${fullName}")
    val role = readln()
    println("Enter the contact number of ${fullName}")
    val contactNumber = readln()
    println("Enter the reporting person ID of ${fullName}")
    val reportingTo = readln().toInt()

    //adds the employee detail into the employee database using Employee data class
    employees.add(Employee(employeeID, firstName, lastName, role, contactNumber, reportingTo))
    //increments the employee id for dynamic id generation
    employeeID++
}

fun main() {
    for (i in 1..numberOfEmployee) {
        //calling the function addEmployee to add the employee
        addEmployee()
    }
    println("Employee list has been updated successfully !")
    //calling the function employeeList to print the list of employees in the database
    employeeList()

    println("Enter the Employee ID to check-in")

}