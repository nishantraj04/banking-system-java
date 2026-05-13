// ============================================================
// Account.java
// Represents a single bank account.
// CONCEPT: Encapsulation is critical here — balance is private.
//          Nobody can directly set balance = -99999.
//          All changes go through deposit() and withdraw(),
//          which enforce rules. This is real-world OOP.
// ============================================================

import java.util.ArrayList;

public class Account {

    // Account types as constants — cleaner than using raw strings
    public static final String SAVINGS  = "Savings";
    public static final String CURRENT  = "Current";

    private String accountNumber;   // e.g. "ACC1001"
    private String holderName;
    private String accountType;     // Savings or Current
    private double balance;
    private String pin;             // Simple 4-digit PIN for security simulation

    // Transaction history — each entry is a formatted string
    // CONCEPT: In a real app, you'd have a Transaction object.
    //          Here, strings keep things simple for learning.
    private ArrayList<String> transactions;

    private static final double MIN_BALANCE = 500.0;  // Minimum balance rule

    // Constructor
    public Account(String accountNumber, String holderName, String accountType,
                   double initialDeposit, String pin) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.pin = pin;
        this.transactions = new ArrayList<>();

        // Log the opening transaction
        transactions.add("[OPEN] Account opened with balance ₹" + String.format("%.2f", initialDeposit));
    }

    // Verify PIN — returns true if correct
    public boolean verifyPin(String inputPin) {
        return this.pin.equals(inputPin);  // .equals() for string comparison, NOT ==
        // IMPORTANT: Never use == to compare strings in Java.
        //            == checks if they're the same object in memory.
        //            .equals() checks if they have the same characters. Always use .equals().
    }

    // Deposit money into the account
    // Returns true if successful
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  [Error] Deposit amount must be positive.");
            return false;
        }
        balance += amount;
        transactions.add(String.format("[CREDIT] +₹%.2f  |  Balance: ₹%.2f", amount, balance));
        System.out.printf("  ✓ ₹%.2f deposited successfully. New balance: ₹%.2f%n", amount, balance);
        return true;
    }

    // Withdraw money from the account
    // Returns true if successful, false if insufficient funds or below min balance
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  [Error] Withdrawal amount must be positive.");
            return false;
        }
        // Check if withdrawal would breach minimum balance
        if (balance - amount < MIN_BALANCE) {
            System.out.printf("  [Error] Insufficient funds. Min balance ₹%.2f must be maintained.%n", MIN_BALANCE);
            System.out.printf("  Available to withdraw: ₹%.2f%n", balance - MIN_BALANCE);
            return false;
        }
        balance -= amount;
        transactions.add(String.format("[DEBIT]  -₹%.2f  |  Balance: ₹%.2f", amount, balance));
        System.out.printf("  ✓ ₹%.2f withdrawn. New balance: ₹%.2f%n", amount, balance);
        return true;
    }

    // Transfer money to another account
    // CONCEPT: This method takes another Account object as a parameter —
    //          objects can interact with each other this way.
    public boolean transferTo(Account recipient, double amount) {
        if (recipient == null) {
            System.out.println("  [Error] Recipient account not found.");
            return false;
        }
        if (recipient.getAccountNumber().equals(this.accountNumber)) {
            System.out.println("  [Error] Cannot transfer to the same account.");
            return false;
        }

        // Try to withdraw from this account first
        System.out.println("  Processing transfer...");
        boolean withdrawn = this.withdraw(amount);

        if (withdrawn) {
            recipient.deposit(amount);  // Deposit into recipient's account
            // Add a transfer-specific note to the log
            transactions.add(String.format("[TRANSFER OUT] ₹%.2f to %s (%s)",
                    amount, recipient.getHolderName(), recipient.getAccountNumber()));
            recipient.transactions.add(String.format("[TRANSFER IN]  ₹%.2f from %s (%s)",
                    amount, this.holderName, this.accountNumber));
            System.out.printf("  ✓ Transfer of ₹%.2f to %s completed.%n",
                    amount, recipient.getHolderName());
            return true;
        }
        return false;
    }

    // Print the last N transactions (mini statement)
    public void printMiniStatement(int count) {
        System.out.println("\n  ── Mini Statement: " + accountNumber + " ──────────────");
        System.out.println("  Holder  : " + holderName);
        System.out.printf("  Balance : ₹%.2f%n", balance);
        System.out.println("  Last " + count + " transactions:");
        System.out.println("  ──────────────────────────────────────────────");

        int start = Math.max(0, transactions.size() - count);
        // Math.max ensures start is never negative (if fewer than 'count' transactions exist)

        for (int i = start; i < transactions.size(); i++) {
            System.out.println("  " + transactions.get(i));
        }

        if (transactions.isEmpty()) {
            System.out.println("  No transactions yet.");
        }
        System.out.println("  ──────────────────────────────────────────────");
    }

    // Print full account summary (no sensitive info like PIN)
    public void printSummary() {
        System.out.println("\n  ── Account Summary ───────────────────────");
        System.out.println("  Account No. : " + accountNumber);
        System.out.println("  Holder Name : " + holderName);
        System.out.println("  Type        : " + accountType);
        System.out.printf("  Balance     : ₹%.2f%n", balance);
        System.out.println("  Transactions: " + transactions.size());
        System.out.println("  ──────────────────────────────────────────");
    }

    // --- GETTERS (no setter for balance — it MUST go through deposit/withdraw) ---
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName()    { return holderName; }
    public String getAccountType()   { return accountType; }
    public double getBalance()       { return balance; }
}
