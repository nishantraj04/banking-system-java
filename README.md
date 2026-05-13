# Banking Management System

A console-based Java banking application with account creation, deposits, withdrawals, fund transfers, and mini statements.

## Features
- Create Savings or Current accounts with PIN security
- Deposit and withdraw with minimum balance enforcement (₹500)
- Transfer funds between accounts with confirmation step
- Mini statement showing last 10 transactions
- Login/logout session flow (like an ATM)
- Admin view of all accounts

## How to Run

```bash
cd src
javac *.java
java Main
```

## Concepts Used
- Object-Oriented Programming (Encapsulation — balance is private, all changes go through methods)
- Object interaction (Account objects work with each other in transferTo())
- ArrayList for transaction history
- Input validation: regex for PIN, range checks for amounts
- Session-based two-level menu (main menu → account menu)
- String comparison with .equals() vs ==

## Project Structure
```
BankingSystem/
└── src/
    ├── Main.java       # Entry point, menus, session management
    ├── Bank.java       # Manages all accounts, handles authentication
    └── Account.java    # Account model with deposit/withdraw/transfer logic
```

## Key Design Decisions
- **Balance has no setter** — it can only change via `deposit()` or `withdraw()`. This prevents invalid states.
- **Authentication returns null on failure** — never reveals which field (account number or PIN) was wrong.
- **Transfer validates both sides** — withdrawal must succeed before deposit happens.

## Sample Flow
```
1. Create Account → Name: Nishant Raj, Type: Savings, Deposit: ₹5000, PIN: 1234
   → Account Number: ACC1001

2. Login → ACC1001 + PIN 1234
   → Deposit ₹2000 → Balance: ₹7000
   → Withdraw ₹1000 → Balance: ₹6000
   → Mini Statement → shows all 3 transactions
```
