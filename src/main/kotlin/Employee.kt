open class Employee() {

    val employees = mutableListOf<DataEmployee>()
    var employeeID = 1

    // Validate employee input
    fun validateEmployeeInput(
        firstName: String, lastName: String, role: String,
        contactNumber: Long, reportingTo: Int
    ): Boolean {
        if (firstName.isBlank()) {
            println("First Name should not be blank!")
            return false
        }
        if (lastName.isBlank()) {
            println("Last Name should not be blank!")
            return false
        }
        if (role.isBlank()) {
            println("Role should not be blank!")
            return false
        }
        if (contactNumber == 0L || ((contactNumber.toString()).length <=9)) {
            println("Contact number must be valid!")
            return false
        }

        if (reportingTo <= 0) {
            println("Invalid Reporting To ID.")
            return false
        }
        if (firstName.isBlank() || lastName.isBlank() || role.isBlank() || contactNumber == 0L) {
            println("All the fields are mandatory. Please enter the values!")
            return false
        }

        return true
    }

}