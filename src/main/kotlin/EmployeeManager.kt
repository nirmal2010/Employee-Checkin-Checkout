class EmployeeManager(private val employeeService: Employee, private val employeeList: EmployeeList) {

    // Method to add employee using user input and validation
    fun addEmployee() {
        println("Enter details for new employee")

        // Get user input
        val firstName = promptInput("First Name: ")
        val lastName = promptInput("Last Name: ")
        val fullName = "$firstName $lastName"

        val role = promptInput("Role of $fullName: ")
        val contactNumber: Long = promptContactInput("Contact Number of $fullName: ")

        val reportingTo = promptIntInput("Reporting To (Manager ID): ")

        // Validate the input
        if (employeeService.validateEmployeeInput(firstName, lastName, role, contactNumber, reportingTo))
        {
            employeeList.addEmployee(firstName, lastName, role, contactNumber, reportingTo)
            println("Employee '$firstName $lastName' added successfully!\n")
        }
        else {
            println("Invalid input! Please try again.")
        }
    }

    // Method to prompt for string input
    private fun promptInput(prompt: String): String {
        print(prompt)
        return readln().trim()
    }

    // Method to prompt for contact number input
    private fun promptContactInput(prompt: String): Long {
        print(prompt)
        return readln().trim().toLongOrNull() ?: 0L  // Default to 0L if invalid input
    }

    // Method to prompt for integer input
    private fun promptIntInput(prompt: String): Int {
        print(prompt)
        return readln().toIntOrNull() ?: 0  // Default to 0 if invalid input
    }

    // Method to view the employee list
    fun viewEmployeeList() {
        //employeeService is the obj referring the Employee Class
        //it calls the employeeList() function in the Employee Class
        employeeList.employeeList()
    }
}
