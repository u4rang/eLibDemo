package com.elib.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="Document", 
uniqueConstraints = {@UniqueConstraint(columnNames = {"title","ISBNCode"})})
public class Document {

	// Types of documents
	public static final int BOOK = 1;
	public static final int JOURNAL = 2;
	public static final int TECHNICAL_REPORT = 3;
	private int type;
	
	@Id
	private int documentCode;
	
	private String title;
	private String ISBNCode;
	private String authors;
	private String refNo;
	private String organization;
	
	private static int nextDocumentCodeAvailable = 0;
	
	@OneToOne
	private Loan loan = null;
	
	Document() {}	// for hibernate
	
	public Document(String title) { 
		this.title = title;
		ISBNCode = "";
		authors = "";
		refNo = "";
		organization = "";
		type = JOURNAL;
		documentCode = getNextDocumentCode(); 
	}

	public Document(String title, String authors, String ISBNCode) {
		this.title = title;
		this.authors = authors;
		this.ISBNCode = ISBNCode;
		refNo = "";
		organization = "";
		type = BOOK;
		documentCode = getNextDocumentCode(); 
	}
	
	public Document(String title, String authors, String refNo, String organization) {
		this.title = title;
		this.authors = authors;
		this.refNo = refNo;
		this.organization = organization;
		type = TECHNICAL_REPORT;
		ISBNCode = "";
		documentCode = getNextDocumentCode(); 
	}
	
	// for testing purpose
	static void reset() {
		nextDocumentCodeAvailable = 0;
	}
	
	protected int getNextDocumentCode() {
		return Document.nextDocumentCodeAvailable++;
	}
	
	public boolean equals(Object obj) { 
		Document doc = (Document)obj;
		return documentCode == doc.documentCode;
	}
	
	public boolean isAvailable() { 
		return loan == null;
	}
	
	public boolean isOut() { 
		return !isAvailable();
	}
	
	public boolean authorizedLoan(User user) { 
		switch (type) {
		case BOOK: 
			return true;
		case JOURNAL: 
			return user.authorizedUser();
		case TECHNICAL_REPORT:
			return false;
		default:
			return false;
		}
	}
	
	public User getBorrower() { 
		if (loan != null)
			return loan.getUser() ; 
		return null;
	}
	
	public int getType() {
		return type;
	}
	
	public int getCode() {
		return documentCode;
	}

	public String getTitle() {
		return title;
	}
	
	public String getAuthors() {
		return authors;
	}
	
	public String getISBN() {
		return ISBNCode;
	}
	
	public String getOrganization() {
		return organization;
	}
	
	public String getRefNo() {
		return refNo;
	}
	
	public void addLoan(Loan loan) {
		this.loan = loan;
	}
	
	public void removeLoan() {
		loan = null;
	}

	protected void printHeader() { 
		System.out.println("Document: " + getCode());
		System.out.println("Title: " + getTitle());
	}
	
	protected void printAvailability() { 
		if (loan == null) {
			System.out.println("Available."); 
		} else {
			User user = loan.getUser() ; 
			System.out.println("Hold by " + user.getCode() +
						" - " + user.getName()) ;
		} 
	}
	
	protected void printGeneralInfo() { 
		switch (type) {
		case BOOK: 
			System.out.println("Authors: " + getAuthors());
			System.out.println("IBSN: " + getISBN());
			break;
		case JOURNAL:
			break;
		case TECHNICAL_REPORT:
			System.out.println("Authors: " + getAuthors());
			System.out.println("Ref. NO: " + getRefNo());
			System.out.println("Organization : " + getOrganization());
			break;
		} 
	}
		
	public void printInfo() { 
		printHeader() ; 
		printGeneralInfo(); 
		printAvailability();	
	}
}
