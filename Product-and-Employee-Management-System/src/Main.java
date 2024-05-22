import entity.*;
import service.BillService;
import service.CartService;
import service.EmployeeService;
import service.ManagerService;
import service.PrintServices;
import service.ProductService;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static service.BillService.statisticsAtTheEndOf1Shift;
import static service.EmployeeService.*;
import static service.ProductService.phimSo1;

public class Main {

    static String username, password;

    public static String faceRecognition() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "03facerecognition.py");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                return line;
            }
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.out.println("bug");
        }
        return null;
    }

    public static void loginSuccesful(String name) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "d.py",name);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.out.println("bug");
        }
    }

    static List<Product> products;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("************************************* Java Cafe ***********************************");
            System.out.println("[1]Login with username, password\n[2]Login with webcam\n[3]Exit");
            choice = input.nextInt();
            switch (choice) {
                case phimSo1: {
                    Scanner scanner = new Scanner(System.in);
                    int count = 0;
                    boolean checkUsernamePassword = false;
                    int channel = 0;
                    do {
                        if (count == 2) {
                            System.out.println("Còn 1 lần nhập nữa, lần này mà sai thì chương trình sẽ đóng");
                        }
                        if (count == 3) {
                            throw new ArithmeticException("Access denied - bạn đã nhập sai quá số lần quy định.");
                        }
                        System.out.print("Nhập username: ");
                        username = scanner.nextLine();
                        System.out.print("Nhập password: ");
                        password = scanner.nextLine();
                        count++;
                        if (username.equals(Manager.getManager().getUsername()) && password.equals(Manager.getManager().getPassword())) {
                            boolean isBack;
                            ManagerService factoryMethodOfManagerService = null;
                            do {
                                Singleton.getInstance().menuTong();
                                System.out.print("Choice: ");
                                channel = input.nextInt();
                                if (channel == 5) {
                                    break;
                                }
                                factoryMethodOfManagerService = factoryMethodOfManager(channel);
                                if (factoryMethodOfManagerService == null) {
                                    break;
                                }
                                isBack = factoryMethodOfManagerService.manage();
                            } while (isBack);
                            if (factoryMethodOfManagerService == null) {
                                break;
                            }
                        } else {
                            checkUsernamePassword = kiemTraPassword(username, password);
                            if (checkUsernamePassword) {
                                EmployeeService.dangNhap(username, password);
                                EmployeeService.checkIn(username);
                                memuEmployee();
                            }
                        }
                        if (channel ==5) {
                            break;
                        }
                    }
                    while (!checkUsernamePassword);
                    break;
                }
                case 2: {
                    username = faceRecognition();
                    System.out.println("Đăng nhập thành công!");
                    loginSuccesful(username);
                    Employee employee = new Employee(null, null, username);
                    employees.put(username, employee);
                    EmployeeService.checkIn(username);
                    memuEmployee();
                    break;
                }
            }
            if (choice == 3) {
                break;
            }
        } while (true);
    }
    public static void Products() //Method to create and add object in ArrayList (Products Information)
    {
        products = new ArrayList<>();
        Product coffee1 = new Coffee(1, "Mocha", 15);  //Polymorphism
        Product coffee2 = new Coffee(2, "Latte", 18); //Polymorphism
        Product coffee3 = new Coffee(3, "Black", 5); //Polymorphism

        Product bakery1 = new Bakery(4, "Bagel", 19); //Polymorphism
        Product bakery2 = new Bakery(5, "Bread", 6); //Polymorphism
        Product bakery3 = new Bakery(6, "Donut", 13); //Polymorphism

        products.add(coffee1); //Adding element
        products.add(coffee2); //Adding element
        products.add(coffee3); //Adding element

        products.add(bakery1); //Adding element
        products.add(bakery2); //Adding element
        products.add(bakery3); //Adding element
    }

    public static ManagerService factoryMethodOfManager(int channel) {
        if (channel == 3) {
            return new BillService();
        } else if (channel == 1) {
            return new ProductService();
        } else if (channel == 2) {
            return new EmployeeService();
        } else {
            return null;
        }
    }

    public static PrintServices factoryMethodOfEmployee(int channel) {
        if (channel == 2) {
            return new CartService();
        } else if (channel == 3) {
            return new BillService();
        } else if (channel == 1) {
            return new ProductService();
        } else if (channel == 4) {
            return new EmployeeService();
        } else {
            return null;
        }
    }

    public static void memuEmployee() {
        Scanner input = new Scanner(System.in);
        int choice1 = 0;
        int choice2;
        do {
            System.out.println("[1]View Menu\n[2]Order Services\n[3]Print Bill\n[4]Check Out\n[5]Exit");
            System.out.print("Choice: ");
            int channel = input.nextInt();
            if (channel == 5) {
                break;
            } else {
                PrintServices factoryMethodOfEmployee = factoryMethodOfEmployee(channel);
                if (factoryMethodOfEmployee != null) {
                    choice2 = factoryMethodOfEmployee.print(username);
                } else {
                    choice2 = 0;
                    System.out.println("invalid");
                }
            }
        } while (choice1 == choice2);
    }
}


