package service;

import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ProductService implements ManagerService, PrintServices {

    public final static int phimSo1 = 1;
    public final static int phimSo2 = 2;
    final static int phimSo3 = 3;
    final static int phimSo4 = 4;
    final int phimSo5 = 5;
    final int phimSo6 = 6;

    Scanner scanner = new Scanner(System.in);

    public static List<Product> productsList;

    public static ProductService productService = null;

    static Scanner input = new Scanner(System.in);

    public static ProductService getProductService() {
        if (productService == null) {
            productService = new ProductService();
        }
        return productService;
    }

    public static List<Product> docProductListTuFile() {
        productsList = new ArrayList<>();
        File file = new File("products.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] productAttributes = line.split("_");
                Product product = new Product();
                product.setId(Integer.parseInt(productAttributes[0]));
                product.setName(productAttributes[1]);
                product.setPrice(Integer.parseInt(productAttributes[2]));
                productsList.add(product);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return productsList;
    }

    public static void ghiproductsVaoFile(List<Product> products) {
        FileWriter writer;
        try {
            writer = new FileWriter("products.txt");
            for (Product s : products) {
                writer.write(s.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("bug");
        }
    }

    public void addNewProductToList() {
        List<Product> productsDataFromFile = docProductListTuFile();
        System.out.println("Product Id: ");
        int productId = input.nextInt();
        input.nextLine();
        System.out.println("Product Name: ");
        String productName = input.nextLine();
        System.out.println("Product Price: ");
        int productPrice = input.nextInt();
        productsDataFromFile.add(new Product(productId, productName, productPrice));
        ghiproductsVaoFile(productsDataFromFile);
        System.out.println("Add New Product To List Successful");
    }

    public void updateProductInList() {
        List<Product> productsDataFromFile = docProductListTuFile();
        System.out.println("Product Id Wan To Update: ");
        int productId = input.nextInt();
        Product product = null;
        for (Product element : productsDataFromFile) {
            if (productId == element.getId()) {
                product = element;
                System.out.println(product);
            }
        }
        System.out.println("Product Price Wan To Update");
        int productPrice = input.nextInt();
        assert product != null;
        product.setPrice(productPrice);
        ghiproductsVaoFile(productsDataFromFile);
        System.out.println("Update Product In List Successful");
    }

    public void removeProductInList() {
        List<Product> productsDataFromFile = docProductListTuFile();
        System.out.println("Product Id Wan To Remove");
        int productId = input.nextInt();
        productsDataFromFile.removeIf(product -> productId == product.getId());
        ghiproductsVaoFile(productsDataFromFile);
        System.out.println("Remove Product In List Successful");
    }

    public void displayProductList() {
        List<Product> productsDataFromFile = docProductListTuFile();
        System.out.println("********* Menu *********");
        System.out.println("ID    NAME      PRICE");
        for (Product type : productsDataFromFile) {
            System.out.println(type.getId() + "     " + type.getName() + "     " + type.getPrice());
        }
    }

    @Override
    public boolean manage() {
        int choice;
        do {
            System.out.println("[1]View Product List\n[2]Back");
            System.out.print("Choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    displayProductList();
                    System.out.println("1. Add New Product To List\n2. Update Product In List\n3. Remove Product In List\n4. Back");
                    int choice1 = scanner.nextInt();
                    switch (choice1) {
                        case 1: {
                            addNewProductToList();
                            break;
                        }
                        case 2: {
                            updateProductInList();
                            break;
                        }
                        case 3: {
                            removeProductInList();
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.println("Back");
                    return true;
                }
            }
        } while (true);
    }

    public static String searchBySpeech() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "speech_recognition.py");
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

    public void search() {
        List<Product> products = docProductListTuFile();
        System.out.println("Product name wan to find");
        String productName = searchBySpeech();
        boolean findToProduct = false;
        for (Product product: products) {
            String nameProduct = product.getName();
            if (nameProduct.equals(productName)) {
                System.out.println(product);
                findToProduct = true;
            }
        }
        if (!findToProduct) {
            System.out.println("invalid");
        }
    }

    @Override
    public int print(String username) {
        displayProductList();
        boolean isInvalid = false;
        do {
            System.out.println("[2]Back\n[3]See the most cheap product\n[4]See the most expensive product\n[5]Sắp xếp theo giá giảm dần\n[6]Sắp xếp theo giá tăng dần\n[7]Search");
            System.out.print("Choice: ");
            int viewProduct = input.nextInt();
            switch (viewProduct) {
                case phimSo2: {
                    System.out.println("Back");
                    return 0;
                }
                case phimSo3: {
                    Collections.sort(productsList);
                    System.out.println(productsList.get(0));
                    break;
                }
                case phimSo4: {
                    productsList.sort(Collections.reverseOrder());
                    System.out.println(productsList.get(0));
                    break;
                }
                case phimSo5: {
                    Collections.sort(productsList);
                    System.out.println(productsList);
                    break;
                }
                case phimSo6: {
                    productsList.sort(Collections.reverseOrder());
                    System.out.println(productsList);
                    break;
                }
                case 7: {
                    search();
                    break;
                }
                default:
                    isInvalid = true;
                    System.out.println("invalid");
            }
        } while (isInvalid);
        return 0;
    }
}
