package service;

import entity.Bill;
import entity.BillItem;
import entity.CartItem;
import entity.Product;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BillService implements ManagerService, PrintServices {

    Scanner input = new Scanner(System.in);

    public static List<Bill> billList;

    public static List<BillItem> billItemList;

    public static List<Bill> billListAtTheOf1Shift = new ArrayList<>();

    public static BillService billService = null;

    public static CartService cartService = null;

    public BillService() {
        billList = new ArrayList<>();
        cartService = CartService.getCartService();
    }

    public static BillService getBillService() {
        if (billService == null) {
            billService = new BillService();
        }
        return billService;
    }

    public void printBill(String username) {
        List<Bill> billsDataFromFile = docBillsTuFile();
        List<BillItem> billItems = convertCartLineToOrderLine();
        int totalPrice = calcSubPrice(billItems);
        System.out.println("*********************** Bill **************************");
        System.out.println("Total Price: " + totalPrice + ".000 VNĐ");
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = time.format(myFormatTime);
        System.out.println("Time: " + formattedDate);
        cartService.displayOrder();
        moneyReturned();
        Bill bill = new Bill(username, totalPrice, formattedDate, billItems);
        BillItem.setCount(0);
        billsDataFromFile.add(bill);
        billListAtTheOf1Shift.add(bill);
        ghiBillsVaoFile(billsDataFromFile);
        CartService.cartItemList.clear();
    }

    public static List<BillItem> convertCartLineToOrderLine() {
        billItemList = new ArrayList<>();
        for (CartItem cartline : CartService.cartItemList) {
            BillItem billItem = new BillItem(cartline.getProduct(), cartline.getUnitPrice(), cartline.getQuantity(), cartline.getSubTotalPrice());
            billItemList.add(billItem);
        }
        return billItemList;
    }

    public static void ghiBillsVaoFile(List<Bill> bills) {
        FileWriter writer;
        try {
            writer = new FileWriter("bills.txt");
            for (Bill bill : bills) {
                writer.write(bill.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("bug");
        }
    }

    public static List<Bill> docBillsTuFile() {
        List<Bill> bills = new ArrayList<>();
        File file = new File("bills.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] billAttributes = line.split(",");
                Bill bill = new Bill();
                bill.setEmployee(billAttributes[0]);
                bill.setTotalPrice(Integer.parseInt(billAttributes[1]));
                bill.setCreatedDay(billAttributes[2]);
                String[] billItemAttributes = billAttributes[3].split(";");
                billItemList = new ArrayList<>();
                for (String element : billItemAttributes) {
                    String[] billItemListInFile = element.split("-");
                    BillItem billItem = new BillItem();
                    String[] productAttributes = billItemListInFile[0].split("_");
                    Product product = new Product();
                    product.setId(Integer.parseInt(productAttributes[0]));
                    product.setName(productAttributes[1]);
                    product.setPrice(Integer.parseInt(productAttributes[2]));
                    billItem.setProduct(product);
                    billItem.setUnitPrice(Integer.parseInt(billItemListInFile[1]));
                    billItem.setQuantity(Integer.parseInt(billItemListInFile[2]));
                    billItem.setSubTotalPrice(Integer.parseInt(billItemListInFile[3]));
                    billItemList.add(billItem);
                    bill.setBillItemList(billItemList);
                }
                bills.add(bill);
            }
        } catch (FileNotFoundException e) {
            System.out.println("bug");
        }
        return bills;
    }

    public static int totalBills() {
        int sum = 0;
        for (Bill bill : billListAtTheOf1Shift) {
            sum = sum + bill.getTotalPrice();
        }
        return sum;
    }

    public static void statisticsAtTheEndOf1Shift() {
        for (Bill bill : billListAtTheOf1Shift) {
            System.out.println(bill);
        }
        System.out.println("Thong ke tong tien toan bo tat ca cac bill trong 1 ca cua ban la: " + totalBills() + ".000 VNĐ");
        System.out.println("So luong toan bo tat ca cac bill trong 1 ca cua ban la: " + billListAtTheOf1Shift.size());
    }

    public static void statisticalAllShifts() {
        List<Bill> billsDataFromFile = docBillsTuFile();
        for (Bill bill : billsDataFromFile) {
            System.out.println(bill);
        }
    }

    public int calcSubPrice(List<BillItem> billItemList) {
        int price;
        int totalPrice = 0;
        for (BillItem order : billItemList) {
            price = (order.getUnitPrice() * order.getQuantity());
            totalPrice += price;
        }
        return totalPrice;
    }

    private void notificationMoneyReturned(int name) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "text_to_speech_when_payment.py", String.valueOf(name));
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

    public void moneyReturned() {
        List<BillItem> billItems = convertCartLineToOrderLine();
        System.out.print("money given by customer: ");
        int moneyGivenByCustomer = input.nextInt();
        int moneyBackToCustomer = moneyGivenByCustomer - calcSubPrice(billItems);
        System.out.println("money back to customer: " + moneyBackToCustomer + ".000 VNĐ");
        notificationMoneyReturned(moneyBackToCustomer);
    }

    @Override
    public boolean manage() {
        do {
            System.out.println("[1]Statistical\n[2]Exit");
            System.out.print("Choice: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1: {
                    statisticalAllShifts();
                    break;
                }
                case 2: {
                    System.out.println("Back");
                    return true;
                }
            }
        } while (true);
    }

    @Override
    public int print(String username) {
        printBill(username);
        return 0;
    }
}
