// ============================================================
// Bank.java
// Manages all accounts — creation, lookup, listing.
// CONCEPT: Same pattern as ResultManager and Library.
//          One class manages data for ONE entity (Account).
//          Bank manages ALL accounts.
// ============================================================

import java.util.ArrayList;

public class Bank {

    private String bankName;
    private ArrayList<Account> accounts;
    private int accountCounter;  // Used to generate unique account numbers

    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
        this.accountCounter = 1001;  // Account numbers start from ACC1001
    }

    // Create a new account and add it to the bank
    public Account createAccount(String holderName, String accountType, double initialDeposit, String pin) {

        // Validate initial deposit
        if (initialDeposit < 500) {
            System.out.println("  [Error] Minimum initial deposit is ₹500.");
            return null;
        }

        // Validate PIN — must be exactly 4 digits
        if (!pin.matches("\\d{4}")) {
            // .matches() checks against a regex pattern
            // "\\d{4}" means "exactly 4 digit characters"
            // CONCEPT: Regex (Regular Expressions) — a pattern language for strings
            System.out.println("  [Error] PIN must be exactly 4 digits.");
            return null;
        }

        String accountNumber = "ACC" + accountCounter++;  // e.g. ACC1001, ACC1002...
        Account newAccount = new Account(accountNumber, holderName, accountType, initialDeposit, pin);
        accounts.add(newAccount);

        System.out.println("  ✓ Account created successfully!");
        System.out.println("  Your Account Number: " + accountNumber + " (save this!)");
        return newAccount;
    }

    // Find an account by account number — returns null if not found
    public Account findAccount(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    // Authenticate: find account AND verify PIN
    // Returns the Account if credentials are valid, null otherwise
    // CONCEPT: Returning null on auth failure (not the reason for failure)
    //          is intentional — you don't tell an attacker which part was wrong.
    public Account authenticate(String accountNumber, String pin) {
        Account account = findAccount(accountNumber);
        if (account == null || !account.verifyPin(pin)) {
            System.out.println("  [Error] Invalid account number or PIN.");
            return null;
        }
        return account;
    }

    // Print summary of all accounts (admin view — no PINs shown)
    public void printAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("  No accounts created yet.");
            return;
        }
        System.out.println("\n  ╔══════════╦══════════════════════╦══════════╦════════════════╗");
        System.out.println("  ║ Acct No. ║ Holder               ║ Type     ║ Balance        ║");
        System.out.println("  ╠══════════╬══════════════════════╬══════════╬════════════════╣");
        for (Account a : accounts) {
            System.out.printf("  ║ %-8s ║ %-20s ║ %-8s ║ ₹%13.2f ║%n",
                a.getAccountNumber(),
                a.getHolderName().length() > 20
                    ? a.getHolderName().substring(0, 18) + ".."
                    : a.getHolderName(),
                a.getAccountType(),
                a.getBalance());
        }
        System.out.println("  ╚══════════╩══════════════════════╩══════════╩════════════════╝");
        System.out.println("  Total accounts: " + accounts.size());
    }

    public String getBankName()   { return bankName; }
    public int getAccountCount()  { return accounts.size(); }
}
