package entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Employee extends User implements Serializable {

    private String checkInTime;

    private String checkOutTime;

    private String name;

    public Employee(String checkInTime, String checkOutTime, String name) {
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.name = name;
    }

    public Employee(String username, String password) {
        super();
    }

    public Employee() {

    }

    public String getName() {
        return name;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public void checkIn() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.checkInTime = time.format(myFormatTime);
    }

    public void checkOut() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.checkOutTime = myFormatTime.format(time);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getWorkingHours() {
        if (checkInTime == null || checkOutTime == null) {
            return null;
        }
        String strCheckInTime = checkInTime;
        DateTimeFormatter formatterCheckInTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime checkInTime = LocalDateTime.parse(strCheckInTime, formatterCheckInTime);
        String strCheckOutTime = checkOutTime;
        DateTimeFormatter formatterCheckOutTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime checkOutTime = LocalDateTime.parse(strCheckOutTime, formatterCheckOutTime);
        return Duration.between(checkInTime, checkOutTime);
    }

    @Override
    public String toString() {
        return checkInTime + "," + checkOutTime + "," + name;
    }
}
