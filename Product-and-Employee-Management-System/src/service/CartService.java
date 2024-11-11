package service;

import entity.CartItem;
import entity.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static service.ProductService.*;

public class CartService implements PrintServices {

    ProductService orderService = ProductService.getProductService();

    public static List<CartItem> cartItemList;

    public static CartService cartService = null;

    public static CartService getCartService() {
        if (cartService == null) {
            cartService = new CartService();
        }
        return cartService;
    }

    public CartService() {
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
        }
    }

    Scanner input = new Scanner(System.in);

    public void addOrder() {
        orderService.displayProductList();
        int chooseProduct;
        do {
            System.out.print("Nhập id của sản phẩm muốn mua: ");
            chooseProduct = input.nextInt();
            System.out.print("Enter quantity : ");
            int quantity = input.nextInt();
            if (quantity < 0) {
                throw new ArithmeticException("Invalid quantity");
            }
            boolean isProductInCart = false;
            Product product = null;
            for (Product element : docProductListTuFile()) {
                if (chooseProduct == element.getId()) {
                    if (cartItemList.isEmpty()) {
                        product = element;
                        int subToTal = quantity * product.getPrice();
                        cartItemList.add(new CartItem(product, product.getPrice(), quantity, subToTal));
                        notificationProductAddedToCart(product.getName());
                    } else {
                        for (CartItem cartItem : cartItemList) {
                            Product product1 = cartItem.getProduct();
                            if (element.getId() == (product1.getId())) {
                                product = element;
                                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                                cartItem.setSubTotalPrice(cartItem.getQuantity() * product1.getPrice());
                                notificationProductAddedToCart(product.getName());
                                isProductInCart = true;
                                break;
                            }
                        }
                        if (!isProductInCart) {
                            product = element;
                            int subToTal = quantity * product.getPrice();
                            cartItemList.add(new CartItem(product, product.getPrice(), quantity, subToTal));
                            notificationProductAddedToCart(product.getName());
                        }
                    }
                    break;
                }
            }
            if (product == null) {
                System.out.println("invalid");
            }
            System.out.println("1. Continue shopping   2. Exit");
            chooseProduct = input.nextInt();
        } while (chooseProduct != 2);
    }

    private void notificationProductAddedToCart(String name) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "text_to_speech_when_order.py",name);
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

    public void displayOrder() {
        if (!cartItemList.isEmpty()) {
            System.out.println("********************** The Order **********************");
            for (CartItem cartItem : cartItemList) {
                System.out.println(cartItem.getId()
                        + ". Name: " + cartItem.getProduct()
                        + "    SubTotal: " + cartItem.getSubTotalPrice()
                        + ".000 VNĐ   Quantity: " + cartItem.getQuantity());

            }
        } else System.out.println("No food on the list yet");
    }

    public void updateOrder() {
        System.out.println("Nhập id sản phẩm muốn sửa");
        int idCuaSanphamMuonSua = input.nextInt();
        CartItem cartItem;
        for (CartItem element : cartItemList) {
            if (idCuaSanphamMuonSua == element.getId()) {
                cartItem = element;
                System.out.println(cartItem);
                System.out.println("Nhập số lượng muốn đổi");
                int quantityMuonDoi = input.nextInt();
                cartItem.setQuantity(quantityMuonDoi);
                cartItem.setSubTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
                System.out.println("Đã đổi số lượng của " + cartItem.getProduct().getName() + " thành công");
            }
        }
    }

    public void cancelOrder() {
        System.out.print("ID of product to remove: ");
        int cancel = input.nextInt();
        String nameProduct = cartItemList.get(cancel - 1).getProduct().getName();
        cartItemList.remove(cancel - 1);
        System.out.println("Đã xoá " + nameProduct + " thành công");

        for (int i = 1; i <= cartItemList.size(); i++) {
            cartItemList.get(i-1).setId(i);
        }

    }

    @Override
    public int print(String username) {
        int choiceOrderService;
        do {
            System.out.println("[1]Add Order    [2]Display Order     [3]Cancel Order    [4]Back");
            System.out.print("Choice: ");
            choiceOrderService = input.nextInt();
            switch (choiceOrderService) {
                case phimSo1: {
                    addOrder();
                    break;
                }
                case phimSo2: {
                    displayOrder();
                    System.out.println("Change quantity");
                    System.out.println("1. Yes      2. No");
                    int updateQuantity = input.nextInt();
                    switch (updateQuantity) {
                        case phimSo1: {
                            updateOrder();
                            break;
                        }
                        case phimSo2: {
                        }
                    }
                    break;
                }
                case phimSo3: {
                    cancelOrder();
                    break;
                }
                case phimSo4: {
                    System.out.println("Back");
                    break;
                }
                default:
                    System.out.println("invalid");
            }
        } while (choiceOrderService != 4);
        return 0;
    }
}
