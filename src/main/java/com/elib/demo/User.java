package com.elib.demo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
//@Table(name="User",
//uniqueConstraints = {@UniqueConstraint(columnNames = {"fullName","phoneNumber"})})
public class User {
	
	public static final int GUEST = 1;
	public static final int STAFF = 2;
	
	@Id
	private int userCode; 
	private String id; // STAFF only
	private String fullName; 
	private String address;
	private String phoneNumber;
	private int type;
	private int latePenalty;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Loan> loans = new ArrayList<>();
	
	private static int nextUserCodeAvailable = 0;
	
	User() {} // for hibernate
	
	public User(String name, String addr, String phone) { 
		fullName = name;
		address = addr;
		phoneNumber = phone;
		type = GUEST;
		userCode = nextUserCode(); 
		latePenalty = 0;
	}

	// for testing purpose
	static void reset() {
		nextUserCodeAvailable = 0;
	}
	
	protected int nextUserCode() {
		return User.nextUserCodeAvailable++;
	}
	
	public User(String name, String addr, String phone, String id) { 
		fullName = name;
		address = addr;
		phoneNumber = phone;
		this.id = id;
		type = STAFF;
		userCode = nextUserCode(); 
		latePenalty = 0;
	}
	
	public boolean equals(Object obj) { 
		User user = (User)obj;
		return userCode == user.userCode;
	}

	public boolean authorizedUser() { 
		if (getType() == GUEST)
			return false;
		
		return true;
	}
	
	public int getType() {
		return type;
	}
	
	public String getId() {
		return id;
	}
	
	public int getCode() { 
		return userCode;
	}
	
	public String getName() { 
		return fullName;
	}
	
	public String getAddress() { 
		return address;
	}
	
	public String getPhone() { 
		return phoneNumber;
	}
	
	public void addLoan(Loan loan) {
		loans.add(loan);
	}
	
	public int numberOfLoans() { 
		return loans.size();
	}

    public List<Loan> getLoans() {
        return loans;
    }

    public void removeLoan(Loan loan) {
		loans.remove(loan);
	}
	public void addCharge(int amount) {
		latePenalty += amount;
	}
	
	public int getLatePenalty() {
		return latePenalty;
	}
	
	public void printInfo() {
		System.out.println("User: " + getCode() + " - " + getName()); 
		if (getType() == STAFF)
			System.out.println("Id: " + getId());
		System.out.println("Address: " + getAddress()); 
		System.out.println("Phone: " + getPhone()); 
		System.out.println("Borrowed documents:");

		for (Loan loan : loans) {
			Document doc = loan.getDocument();
			System.out.println(doc.getCode() + " - " + doc.getTitle());
		}
	}
}
	
	

