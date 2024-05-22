package entity;

public class Singleton {

    private static Singleton instance;

    private Singleton() {
        
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void menuTong() {
        System.out.println("1. Quản lý bán hàng\n2. Quản lý nhân viên\n3. Quản lý đơn hàng\n4. Exit\n5. Back");
    }
}