import java.util.*;
import java.io.*;


class Main {
    public static void main(String[] args) {
        AccountManager manager = new AccountManager();
        Scanner scan = new Scanner(System.in);
        Reader reader = new Reader();

        reader.createTxt();
        printWelcome();
        int operation = checkValidity(scan);

        switch (operation) {
            case 1: manager.loginUser(scan); break;
            case 2: manager.registerUser(scan); break;
            case 3: return;
        }
    }

    public static void printWelcome() { 
        System.out.println("WELCOME");
        System.out.println("If you want to login write 1");
        System.out.println("If you want to register write 2");
        System.out.println("If you want to close write 3");
    }

    public static int checkValidity(Scanner scan) {
        int operation;
        while (true) { 
            try {
                operation = scan.nextInt(); 
                if (operation < 1 || operation > 3) {
                    throw new InputMismatchException();
                } 
                break; 
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid integer");
                scan.nextLine();
            }
        }

        return operation;
    }
}

class Reader {
    public void createTxt() {
        File file = new File("accounts.txt");
        try {
            if (file.createNewFile()) {
                System.out.println("File was created successfully");
            }
        } catch (IOException e) {
            System.out.println("ERROR!");
            System.out.println("Try again later.");
        }
    }

    public void createNewAccount(String username, String password, int balance) {
        try {
            FileWriter writer = new FileWriter("accounts.txt", true);
            writer.write(username + " " + password + " " + String.valueOf(balance) + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR!");
            System.out.println("Try again later.");
        }
    }
}

class User { 
    private String username, password;
    private int balance;

    public User(String username, String password, int balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public double getBalance() {
        return balance;
    }

    public void addToBalance(int amount) {
        this.balance += amount;
    }

    public boolean getMoney(int amount) {
        if (amount <= this.balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
}

class AccountManager {
    private Reader reader;
    
    public AccountManager() {
        this.reader = new Reader();
    }

    public void loginUser(Scanner scan) {
        return;
    }

    public void registerUser(Scanner scan) {
        return;
    }
}
