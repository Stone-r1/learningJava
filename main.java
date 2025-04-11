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

        User currentUser = null;

        switch (operation) {
            case 1: currentUser = manager.loginUser(scan); break;
            case 2: manager.registerUser(scan); break;
            case 3: System.exit(0);
        }

        if (currentUser != null) {
            System.out.println("Welcome " + currentUser.getUsername() + " your current balance is " + currentUser.getBalance());
            sessionLoop(scan, currentUser, reader);
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

    public static void sessionLoop(Scanner scan, User currentUser, Reader reader) {
        String command = "";
        System.out.println("You're logged in. Type 'help' to get additional information or 'quit' to exit");
        while (!command.equalsIgnoreCase("quit")) {
            command = scan.nextLine().trim().toLowerCase();
            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "deposit":
                    deposit(scan, currentUser, reader);
                    break;

                case "withdraw":
                    withdraw(scan, currentUser, reader);
                    break;

                case "balance":
                    System.out.println(currentUser.getUsername() + " has " + currentUser.getBalance() + "$");
                    break;

                case "quit":
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Unknown command");
            }
        }
    }

    public static void deposit(Scanner scan, User currentUser, Reader reader) {
        System.out.println("Enter deposit amount:");
        try {
            int amount = Integer.parseInt(scan.nextLine().trim());
            currentUser.addToBalance(amount);
            reader.updateUser(currentUser.getUsername(), currentUser);
            System.out.println("New balance: " + currentUser.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid integer.");
        }
    }

    public static void withdraw(Scanner scan, User currentUser, Reader reader) {
        System.out.println("Enter withdrawal amount:");
        try {
            int amount = Integer.parseInt(scan.nextLine().trim());
            if (currentUser.getMoney(amount)) {
                System.out.println("New balance: " + currentUser.getBalance());
                reader.updateUser(currentUser.getUsername(), currentUser);
            } else {
                System.out.println("Current user doesn't have " + amount + "$");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid integer.");
        }
    }

    public static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  deposit  - Add money to your account.");
        System.out.println("  withdraw - Remove money from your account.");
        System.out.println("  balance  - Check your current balance.");
        System.out.println("  quit     - Log out.");
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
        try (FileWriter writer = new FileWriter("accounts.txt", true)) {
            writer.write(username + "," + password + ",0\n");
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
                if (parts.length >= 3) {
                    String fileUsername = parts[0];
                    String filePassword = parts[1];
                    
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
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

    public boolean checkIfUsernameExists(String username) {
        try (Scanner fileScan = new Scanner(new File("accounts.txt"))) {
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String fileUsername = parts[0];
                    
                    if (fileUsername.equals(username)) {
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

    public User getUser(String username, String password) {
        try (Scanner fileScan = new Scanner(new File("accounts.txt"))) {
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileUsername = parts[0];
                    String filePassword = parts[1];
                    int balance = Integer.parseInt(parts[2]);

                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        return new User(fileUsername, filePassword, balance);
                    }
                } 
            }

        } catch (IOException e) {
            System.out.println("ERROR!");
            System.out.println("Try again later.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing balance.");
            System.out.println("Try again later.");
        }
        return null;
    }
    
    public void updateUser(String username, User updatedUser) {
        List<String> lines = new ArrayList<>();
        try (Scanner fileScan = new Scanner(new File("accounts.txt"))) {
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String parts[] = line.split(",");
                if (parts.length >= 3) {
                    String fileUsername = parts[0];
                    if (fileUsername.equals(username)) {
                        line = updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getBalance();
                    }
                }

                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error!");
        }

        try (FileWriter writer = new FileWriter(new File("accounts.txt"))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error!");
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

    public String getPassword() {
        return password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public int getBalance() {
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

    public User loginUser(Scanner scan) {
        String username, password, input;
        User loggedInUser = null;
        
        while (true) {
            System.out.println("Enter username");
            username = scan.nextLine();
            System.out.println("Enter password");
            password = scan.nextLine();

            loggedInUser = reader.getUser(username, password);
            if (loggedInUser != null) {
                System.out.println("Login Successful!");
                break;
            } 

            System.out.println("Invalid username or password");
            System.out.println("Do you want to try again? (y/n)");

            input = scan.nextLine().trim().toLowerCase();
            if (!input.equals("y")) {
                break;
            }
        }

        return loggedInUser;
    }

    public void registerUser(Scanner scan) {
        String username, password;

        System.out.println("Enter username");
        username = scan.nextLine();

        if (reader.checkIfUsernameExists(username)) {
            System.out.println("This username is already taken.");
            return;
        }

        System.out.println("Enter password");
        password = scan.nextLine(); 

        reader.createNewAccount(username, password);
        System.out.println("You're welcome, mate!");
    }
}
