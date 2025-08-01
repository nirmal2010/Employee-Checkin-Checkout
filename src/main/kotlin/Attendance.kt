import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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