class EmployeeList: Employee()
{
    fun addEmployee(firstName: String, lastName: String, role: String, contactNumber: Long, reportingTo: Int) {
        //adds the employee detail into the employee database using Employee data class
        // Adds the employee into the database
        employees.add(DataEmployee(employeeID, firstName, lastName, role, contactNumber, reportingTo))
        //increments the employee id for dynamic id generation
        employeeID++
    }

    // Prints the list of employees
    fun employeeList() {
        if (employees.isEmpty())
        {
            println("Oops! You haven't created any employee. Please create an employee and try viewing employee list.")
        }
        else{
            println("\n--- Employee List ---")
            for (employee in employees) {
                println(employee)
            }
        }
    }
}