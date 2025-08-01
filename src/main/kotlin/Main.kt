import java.time.LocalDateTime
import java.time.LocalTime

data class DataEmployee(
    val employeeID: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: Long,
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

fun main() {
    val employeeService = Employee()
    val employeeListService = EmployeeList()
    val employeeManager = EmployeeManager(employeeService, employeeListService)
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
                //calls the employeeManager object and the object has the EmployeeManager class reference
                // then the addEmployee function in the class EmployeeManager will be called
                employeeManager.addEmployee()
            }

            //calls the employeeManager object and the object has the EmployeeManager class reference
            // then the viewEmployeeList function in the class EmployeeManager will be called
            "2" -> employeeManager.viewEmployeeList()

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
