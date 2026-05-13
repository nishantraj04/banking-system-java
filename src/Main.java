// ============================================================
// Main.java — Banking System
// Entry point. Two-level menu: main menu + account menu.
// CONCEPT: Session-based design — user logs into an account,
//          does operations, then logs out. Like a real ATM.
// ============================================================

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Bank bank = new Bank("NishantBank");

        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║          BANKING MANAGEMENT SYSTEM       ║");
        System.out.println("  ║             Welcome to NishantBank       ║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("  Enter choice: ");

            switch (choice) {
                case 1 -> createAccount(bank);
                case 2 -> loginAndOperate(bank);
                case 3 -> bank.printAllAccounts();
                case 4 -> {
                    System.out.println("\n  Thank you for banking with NishantBank. Goodbye!");
                    running = false;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }

        sc.close();
    }

    // ── MAIN MENU ────────────────────────────────────────────

    static void printMainMenu() {
        System.out.println("\n  ── MAIN MENU ──────────────────────────────");
        System.out.println("  1. Create New Account");
        System.out.println("  2. Login to Account");
        System.out.println("  3. View All Accounts (Admin)");
        System.out.println("  4. Exit");
        System.out.println("  ───────────────────────────────────────────");
    }

    // Handle account creation
    static void createAccount(Bank bank) {
        System.out.println("\n  ── Create New Account ────────────────────");
        System.out.print("  Full Name     : ");  String name = sc.nextLine().trim();

        // Account type selection
        System.out.println("  Account Type  : 1. Savings   2. Current");
        int typeChoice = readInt("  Choose (1/2)  : ");
        String type = (typeChoice == 2) ? Account.CURRENT : Account.SAVINGS;

        double deposit = readDouble("  Initial Deposit (min ₹500): ₹");

        System.out.print("  Set 4-digit PIN: ");  String pin = sc.nextLine().trim();

        bank.createAccount(name, type, deposit, pin);
    }

    // ── ACCOUNT SESSION ──────────────────────────────────────

    // Login flow: authenticate, then enter account-level menu
    static void loginAndOperate(Bank bank) {
        System.out.println("\n  ── Login ──────────────────────────────────");
        System.out.print("  Account Number: ");
        String accNum = sc.nextLine().trim().toUpperCase();  // Normalize to uppercase

        System.out.print("  PIN           : ");
        String pin = sc.nextLine().trim();

        // Authenticate — returns null if credentials are wrong
        Account account = bank.authenticate(accNum, pin);

        if (account == null) return;  // Auth failed — message already printed in Bank

        System.out.println("\n  ✓ Login successful. Welcome, " + account.getHolderName() + "!");

        // Account session loop
        boolean loggedIn = true;
        while (loggedIn) {
            printAccountMenu();
            int choice = readInt("  Enter choice: ");

            switch (choice) {
                case 1 -> account.printSummary();
                case 2 -> {
                    double amount = readDouble("  Enter deposit amount: ₹");
                    account.deposit(amount);
                }
                case 3 -> {
                    double amount = readDouble("  Enter withdrawal amount: ₹");
                    account.withdraw(amount);
                }
                case 4 -> handleTransfer(bank, account);
                case 5 -> account.printMiniStatement(10);
                case 6 -> {
                    System.out.println("  Logged out. See you soon!");
                    loggedIn = false;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }
    }

    static void printAccountMenu() {
        System.out.println("\n  ── ACCOUNT MENU (" + "●●●● secured) ──────────────");
        System.out.println("  1. View Account Summary");
        System.out.println("  2. Deposit");
        System.out.println("  3. Withdraw");
        System.out.println("  4. Transfer to Another Account");
        System.out.println("  5. Mini Statement (last 10)");
        System.out.println("  6. Logout");
        System.out.println("  ───────────────────────────────────────────");
    }

    // Handle fund transfer between two accounts
    static void handleTransfer(Bank bank, Account sender) {
        System.out.println("\n  ── Fund Transfer ─────────────────────────");
        System.out.print("  Recipient Account Number: ");
        String recipientAccNum = sc.nextLine().trim().toUpperCase();

        Account recipient = bank.findAccount(recipientAccNum);
        if (recipient == null) {
            System.out.println("  [Error] Recipient account not found.");
            return;
        }

        System.out.println("  Recipient: " + recipient.getHolderName());
        System.out.print("  Confirm transfer to this account? (yes/no): ");
        String confirm = sc.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("  Transfer cancelled.");
            return;
        }

        double amount = readDouble("  Enter transfer amount: ₹");
        sender.transferTo(recipient, amount);
    }

    // --- UTILITY METHODS ---

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Please enter a valid number.");
            }
        }
    }

    // Read a double — used for monetary amounts
    // CONCEPT: double is Java's decimal number type.
    //          For real banking apps, use BigDecimal to avoid floating point errors.
    //          For this project, double is fine.
    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value <= 0) {
                    System.out.println("  [Error] Amount must be positive.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Please enter a valid amount.");
            }
        }
    }
}
