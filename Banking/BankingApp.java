import java.util.*;

class UserNotFoundException extends Exception {
    public UserNotFoundException(String msg) { super(msg); }
}


class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String msg) { super(msg); }
}
// -----------------------------------------------------------------------------
class Transaction {
    String type;
    double amount;
    String date;


    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = new Date().toString();
    }


    @Override
    public String toString() {
        return String.format("| %-20s | %-10s | %-10.2f |", date, type, amount);
    }
}


class Account {
    protected String accountNo;
    protected String type;
    private double balance; // Private as requested
    protected List<Transaction> history = new ArrayList<>();


    public Account(String accountNo, String type, double initialBalance) {
        this.accountNo = accountNo;
        this.type = type;
        this.balance = initialBalance;
        history.add(new Transaction("OPENING", initialBalance));
    }


    public double getBalance() { return balance; }
   
    // Protected method so subclasses can adjust balance
    protected void updateBalance(double amount) { this.balance += amount; }


    public void deposit(double amt) {
        updateBalance(amt);
        history.add(new Transaction("CREDIT", amt));
    }


    public void withdraw(double amt) throws Exception {
        if (amt > balance) throw new InsufficientFundsException("Withdrawal amount > Balance");
        updateBalance(-amt);
        history.add(new Transaction("DEBIT", amt));
    }


    public void viewHistory() {
        System.out.println("\n--- Transaction History: " + accountNo + " ---");
        System.out.println("-------------------------------------------------------------");
        System.out.println("| Date                 | Type       | Amount     |");
        System.out.println("-------------------------------------------------------------");
        for (Transaction t : history) System.out.println(t);
        System.out.println("-------------------------------------------------------------");
    }
}


//--------------------------------------------------------------------------------------------------------


class SavingsAccount extends Account {
    public SavingsAccount(String nr, double bal) { super(nr, "SAVINGS", bal); }


    @Override
    public void withdraw(double amt) throws Exception {
        if (getBalance() - amt < 500) throw new InsufficientFundsException("Balance cannot go below 500");
        super.withdraw(amt);
    }
}


class CurrentAccount extends Account {
    private double limit = 2000;
    public CurrentAccount(String nr, double bal) { super(nr, "CURRENT", bal); }


    @Override
    public void withdraw(double amt) throws Exception {
        if (amt > (getBalance() + limit)) throw new InsufficientFundsException("Overdraft limit exceeded");
        updateBalance(-amt);
        history.add(new Transaction("DEBIT(OD)", amt));
    }
}


class FixedDepositAccount extends Account {
    public FixedDepositAccount(String nr, double bal) { super(nr, "FIXED", bal); }


    @Override
    public void withdraw(double amt) throws Exception {
        throw new Exception("Fixed Deposit locked until maturity!");
    }
}


//----------------------------------------------------------------------------------------------


class Customer {
    String custId, name, aadhar;
    List<Account> accounts = new ArrayList<>();


    public Customer(String id, String n, String a) {
        this.custId = id; this.name = n; this.aadhar = a;
    }
}


class Bank {
    List<Customer> customers = new ArrayList<>();


    public void addCustomer(Customer c) { customers.add(c); }


    public Account findAccount(String accNo) throws UserNotFoundException {
        for (Customer c : customers) {
            for (Account a : c.accounts) {
                if (a.accountNo.equals(accNo)) return a;
            }
        }
        throw new UserNotFoundException("Account " + accNo + " not found.");
    }
}
// ---------------------------------------------------------------------------
public class BankingApp {
    static Bank myBank = new Bank();
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Register Customer | 2. Transaction | 3. View Details | 4. Exit");
            int choice = sc.nextInt();
            try {
                if (choice == 1) createAccount();
                else if (choice == 2) handleTransaction();
                else if (choice == 3) showDetails();
                else break;
            } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
        }
    }


    private static void createAccount() {
        System.out.print("Name: "); String n = sc.next();
        System.out.print("Aadhar: "); String aa = sc.next();
        Customer c = new Customer("CUST" + (myBank.customers.size() + 1), n, aa);
       
        System.out.print("Type (1.Savings 2.Current 3.FD): ");
        int t = sc.nextInt();
        System.out.print("Acc No: "); String an = sc.next();
        System.out.print("Initial $: "); double bal = sc.nextDouble();


        if (t == 1) c.accounts.add(new SavingsAccount(an, bal));
        else if (t == 2) c.accounts.add(new CurrentAccount(an, bal));
        else c.accounts.add(new FixedDepositAccount(an, bal));


        myBank.addCustomer(c);
        System.out.println("Customer Registered with ID: " + c.custId);
    }


    private static void handleTransaction() throws Exception {
        System.out.print("1.Deposit 2.Withdraw 3.Transfer: ");
        int op = sc.nextInt();
        if (op == 3) {
            System.out.print("From Acc: "); Account f = myBank.findAccount(sc.next());
            System.out.print("To Acc: "); Account t = myBank.findAccount(sc.next());
            System.out.print("Amt: "); double amt = sc.nextDouble();
            if (amt > f.getBalance()) throw new InsufficientFundsException("Transfer amt > Balance");
            f.withdraw(amt); t.deposit(amt);
        } else {
            System.out.print("Acc No: "); Account a = myBank.findAccount(sc.next());
            System.out.print("Amt: "); double amt = sc.nextDouble();
            if (op == 1) a.deposit(amt); else a.withdraw(amt);
        }
    }


    private static void showDetails() throws Exception {
        System.out.print("Acc No: ");
        Account a = myBank.findAccount(sc.next());
        System.out.println("\n================ ACCOUNT DETAILS ================");
        System.out.printf("%-15s: %s\n", "Account No", a.accountNo);
        System.out.printf("%-15s: %s\n", "Type", a.type);
        System.out.printf("%-15s: %.2f\n", "Balance", a.getBalance());
        a.viewHistory();
    }
}


// ----------------------------------------------------------------





