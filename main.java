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
        scan.nextLine();

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

    public void createNewAccount(String username, String password) {
        try {
            FileWriter writer = new FileWriter("accounts.txt", true);
            writer.write(username + "," + password + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR!");
            System.out.println("Try again later.");
        }
    }

    public boolean checkIfAccountExists(String username, String password) {
        try (Scanner fileScan = new Scanner(new File("accounts.txt"))) {
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String fileUsername = parts[0];
                    String filePassword = parts[1];

                    if (password.isEmpty()) {
                        if (fileUsername.equals(username)) {
                            return true;
                        }
                    }
                    else if (fileUsername.equals(username) && filePassword.equals(password)) {
                        return true;
                    }
                }
            }
            return false;

        } catch (IOException e) {
            System.out.println("ERROR!");
            System.out.println("Try again later.");
            return false;
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
        String username, password;
        boolean checker = false;
        
        scan.nextLine();
        while (!checker) {
            System.out.println("Enter username");
            username = scan.nextLine();
            System.out.println("Enter password");
            password = scan.nextLine();

            checker = reader.checkIfAccountExists(username, password);
            if (checker) {
                System.out.println("Login Successfully");
                break;
            } 

            System.out.println("Invalid username or password");
            System.out.println("Do you want to try again? (y/n)");
            String input = scan.nextLine().trim().toLowerCase();

            if (input.equals("y")) continue;
            else break;
        }
    }

    public void registerUser(Scanner scan) {
        String username, password;

        System.out.println("Enter username");
        username = scan.nextLine();

        if (reader.checkIfAccountExists(username, "")) {
            System.out.println("This username is already taken.");
            return;
        }

        System.out.println("Enter password");
        password = scan.nextLine(); 

        reader.createNewAccount(username, password);
        System.out.println("You're welcome, mate!");
    }
}
