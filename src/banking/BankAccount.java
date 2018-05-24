package banking;
public class BankAccount {
	String accNo;
	double balance;

	public BankAccount() {
	}

	public void deposit(double amount) {
	}

	public void withdraw(double amount) {
	}

	protected boolean check(double amount) {
		return true;
	}

	public String toString() {
		return "Account Number: " + this.accNo + "\nBalance: " + this.balance;
	}

}
