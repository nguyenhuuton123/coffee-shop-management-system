package service;

import entity.Employee;
import entity.User;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static service.BillService.statisticsAtTheEndOf1Shift;

public class EmployeeService implements ManagerService, PrintServices {

    static Scanner scanner = new Scanner(System.in);

    public static List<Employee> attendanceEmployeeList;

    public static List<User> usersList;

    public static Map<String, Employee> employees = new HashMap<>();

    public static List<User> docUserListTuFile(File path) {
        if (path.canRead()) {
            usersList = new ArrayList<>();
            Scanner sc;
            try {
                sc = new Scanner(path);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] userAttributes = line.split(",");
                    User user = new User();
                    user.setUsername(userAttributes[0]);
                    user.setPassword(userAttributes[1]);
                    usersList.add(user);
                }
            } catch (Exception ex) {
                System.out.println("An error occurred.");
            }
        } else {
            return usersList;
        }
        return usersList;
    }

    public static List<Employee> docAttendanceListTuFile(File path) {
        if (path.canRead()) {
            if (attendanceEmployeeList == null) {
                attendanceEmployeeList = new ArrayList<>();
            }
            Scanner sc;
            try {
                sc = new Scanner(path);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    Employee employee = getEmployee(line);
                    attendanceEmployeeList.add(employee);
                }
            } catch (Exception ex) {
                System.out.println("An error occurred.");
            }
        } else {
            return attendanceEmployeeList;
        }
        return attendanceEmployeeList;
    }

    private static Employee getEmployee(String line) {
        String[] employeeAttributes = line.split(",");
        Employee employee = new Employee();
        employee.setCheckInTime(employeeAttributes[0]);
        employee.setCheckOutTime(employeeAttributes[1]);
//        String[] productAttributes = employeeAttributes[2].split("_");
//        User user = new User();
//        user.setUsername(productAttributes[0]);
//        user.setPassword(productAttributes[1]);
        employee.setName(employeeAttributes[2]);
        return employee;
    }

    public static void ghiAttendanceListVaoFile(List<Employee> attendance) {
        try {
            // Mở file Excel hiện có
            File inp = new File("attendance.xls");
            Workbook existingWorkbook = Workbook.getWorkbook(inp);

            // Tạo một bản sao có thể ghi được từ Workbook hiện có
            File out = new File("attendance.xls");
            WritableWorkbook workbook = Workbook.createWorkbook(out, existingWorkbook);

            // Lấy sheet đầu tiên
            WritableSheet sheet = workbook.getSheet(0);

            // Ghi dữ liệu
            for (int i = 0; i < attendance.size(); i++) {
                Employee employee = attendance.get(i);
                Label name = new Label(0, i + 1, employee.getName());
                Label checkIn = new Label(1, i + 1, String.valueOf(employee.getCheckInTime()));
                Label checkOut = new Label(2, i + 1, String.valueOf(employee.getCheckOutTime()));
                sheet.addCell(name);
                sheet.addCell(checkIn);
                sheet.addCell(checkOut);
            }

            // Đóng Workbook hiện có trước khi ghi bản sao
            existingWorkbook.close();

            // Ghi workbook vào file và đóng nó
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            System.out.println("bug");
        }
        FileWriter writer;
        try {
            writer = new FileWriter("attendance.txt");
            for (Employee user : attendance) {
                writer.write(user.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("bug");
        }
    }

    public static void ghiUserListVaoFile(List<User> users) {
        FileWriter writer;
        try {
            writer = new FileWriter("users.txt");
            for (User user : users) {
                writer.write(user.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("bug");
        }
    }

    public void dangKy(String username, String password) {
        List<User> userListInFile = docUserListTuFile(new File("users.txt"));
        User user = new User(username, password);
        userListInFile.add(user);
        ghiUserListVaoFile(userListInFile);

    }

    public static boolean isExistingUser(String username) {
        List<User> usersDataFromFile = docUserListTuFile(new File("users.txt"));
        for (User employee : usersDataFromFile) {
            if (employee.getUsername().equals(username)) {
                System.out.println("username: " + username + " đã tồn tại vui lòng nhập username check");
                return true;
            }
        }
        System.out.println("username: " + username + " hợp lệ");
        return false;
    }

    public static void dangNhap(String username, String password) {
        if (kiemTraPassword(username, password)) {
            System.out.println("Đăng nhập thành công!");
            Employee employee = new Employee(null, null, username);
            employees.put(username, employee);
        } else {
            System.out.println("Tên người dùng hoặc mật khẩu không chính xác!");
        }
    }

    public static String getPasswordByUsername(String username) {
        List<User> dataUserFromFile = docUserListTuFile(new File("users.txt"));
        if (dataUserFromFile != null) {
            for (User type : dataUserFromFile) {
                if (type.getUsername().equals(username)) {
                    return type.getPassword();
                }
            }
        }
        return null;
    }

    public static boolean kiemTraPassword(String username, String password) {
        String passwordInFile = getPasswordByUsername(username);
        if (passwordInFile == null) {
            System.out.println("username không hợp lệ");
            return false;
        }
        if (passwordInFile.equals(password)) {
            return true;
        } else {
            System.out.println("Sai mật khẩu vui lòng nhập lại");
        }
        return false;
    }

    public static void checkIn(String username) {
        Employee employee = employees.get(username);
        if (employee == null) {
            return;
        }
        employee.checkIn();
    }

    public void checkOut(String username) {
        Employee employee = employees.get(username);
        if (employee == null) {
            return;
        }
        employee.checkOut();
    }

    public void attendance(String username) {
        List<Employee> listAttendance = docAttendanceListTuFile(new File("attendance.txt"));
        Employee employee = employees.get(username);
        listAttendance.add(employee);
        ghiAttendanceListVaoFile(listAttendance);
    }

    public Duration getWorkingHours(String username) {
        Employee employee = employees.get(username);
        if (employee == null) {
            return null;
        }

        return employee.getWorkingHours();
    }

    public void displayEmployeeList() {
        List<User> employeeList = docUserListTuFile(new File("users.txt"));
        for (User user : employeeList) {
            System.out.println(user);
        }
    }

    public void displayAttendanceList() {
        List<Employee> employeeList = docAttendanceListTuFile(new File("attendance.txt"));
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    public static void faceTraining() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "02facetraining.py");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.out.println("bug");
        }
    }

    public static void faceDataSet() {
        Scanner scanner1 = new Scanner(System.in);
        try {
            System.out.println("\n enter user id end press <return> ==>  ");
            String faceId = scanner1.nextLine();
            System.out.println("\n enter user name end press <return> ==> ");
            String nameId = scanner1.nextLine();
            ProcessBuilder processBuilder = new ProcessBuilder("python", "01facedataset.py", faceId, nameId);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.out.println("bug");
        }
    }

    @Override
    public boolean manage() {
        do {
            System.out.println("[1]View Employee List\n[2]View Attendance List\n[3]Register New Employee\n[4]Back\n[5]Train Data");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    displayEmployeeList();
                    break;
                }
                case 2: {
                    displayAttendanceList();
                    break;
                }
                case 3: {
                    System.out.println("1. Register with webcam\n2. Register with username, password");
                    int choice1 = scanner.nextInt();
                    switch (choice1) {
                        case 1: {
                            faceDataSet();
                            break;
                        }
                        case 2: {
                            Scanner scanner = new Scanner(System.in);
                            String username;
                            String password;
                            do {
                                System.out.print("Nhập username: ");
                                username = scanner.nextLine();
                                System.out.print("Nhập password: ");
                                password = scanner.nextLine();
                            } while (isExistingUser(username));
                            dangKy(username, password);
                            break;
                        }
                    }
                    break;
                }
                case 4: {
                    System.out.println("Back");
                    return true;
                }
                case 5: {
                    faceTraining();
                    break;
                }
            }
        }
        while (true);
    }

    @Override
    public int print(String username) {
        checkOut(username);
        attendance(username);
        Duration workingHours = getWorkingHours(username);
        assert workingHours != null;
        System.out.println("Số giờ làm việc: " + workingHours.toMinutes());
        statisticsAtTheEndOf1Shift();
        return 0;
    }
}

