package com.elib.demo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
public class Loan {
	@Id @GeneratedValue
	private int id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private User user;
	@OneToOne(fetch=FetchType.EAGER)
	private Document document;
	private LocalDate occurredOn;
	private LocalDate dueDate;
	
	Loan() {} // for hibernate
	
	public Loan(User usr, Document doc) { 
		user = usr;
		document = doc;
		occurredOn = LocalDate.now();
		
		if (user.getType() == User.STAFF) {
			dueDate = occurredOn.plusDays(14); 
		} else 
			dueDate = occurredOn.plusDays(7); 			
	}
	
	public int getLateCharge(LocalDate returnedDate) {
	
		int diff = (int) DAYS.between(dueDate, returnedDate);
		System.out.println("diff = " + diff);
		
		if (document.getType() == Document.BOOK)
			return diff > 0 ? diff : 0;
		else if (document.getType() == Document.JOURNAL) 
			return diff > 0 ? 3 * diff: 0;
		else
			return 0;
	}
	
	public LocalDate getDueDate() {
		return dueDate;
	}
	
	public User getUser() { 
		return user;
	}
	
	public Document getDocument() { 
		return document;
	}
	
	public boolean equals(Object obj) { 
		Loan other = (Loan)obj;
		return user.equals(other.user) &&
				document.equals(other.document) && occurredOn.equals(other.occurredOn); 
	}
	
	public void print() {
		System.out.println("User: " + user.getCode() +
				" - " + user.getName() +
				" holds doc: " + document.getCode() + 
				" - " + document.getTitle() +
				" - " + "due to: " + getDueDate());
	} 
}
