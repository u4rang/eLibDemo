package com.elib.demo.app;

import com.elib.demo.domain.document.Document;
import com.elib.demo.domain.user.Loan;
import com.elib.demo.domain.user.User;
import com.elib.demo.infra.RepositoryDBImpl;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Interactor {

    public static final int MAX_NUMBER_OF_LOANS = 2;

    public Interactor() {}

    /*
     * user management commands
     */

    public void addUser(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 3)
            return;

        User user = new User(args[0], args[1], args[2]);

        saveUser(user);

        System.out.println("Added user: " + user.getCode() + " - " + user.getName());
    }

    public void addStaff(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 4)
            return;

        User user = new User(args[0], args[1], args[2], args[3]);

        saveUser(user);

        System.out.println("Added staff: " + user.getCode() + " - " + user.getName());
    }

    public void rmUser(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        int userCode = Integer.parseInt(args[0]);

        User user = findUser(userCode);

        if (user == null || user.numberOfLoans() > 0 || user.getLatePenalty() > 0) {
            return;
        }

        removeUser(user);

        System.out.println("Removed user: " + user.getCode() + " - " + user.getName());
    }

    public void searchUser(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        String name = args[0];

        List<User> users = findAllUsers();

        List<User> usersFound = new LinkedList<User>();
        for (User user : users) {
            if (user.getName().indexOf(name) != -1)
                usersFound.add(user);
        }

        for (User user : usersFound) {
            System.out.println("user found: " + user.getCode() + " - " + user.getName());
        }
    }

    public void printUser(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        int userCode = Integer.parseInt(args[0]);
        User user = findUser(userCode);

        if (user != null)
            user.printInfo();
    }/*
     * document management commands
     */

    public void addBook(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 3)
            return;

        Document doc = new Document(args[0], args[1], args[2]);

        saveDocument(doc);

        System.out.println("Added doc: " + doc.getCode() + " - " + doc.getTitle());
    }

    public void addReport(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 4)
            return;

        Document doc = new Document(args[0], args[1], args[2], args[3]);

        saveDocument(doc);

        System.out.println("Added doc: " + doc.getCode() + " - " + doc.getTitle());
    }

    public void addJournal(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        Document doc = new Document(args[0]);

        saveDocument(doc);

        System.out.println("Added doc: " + doc.getCode() + " - " + doc.getTitle());
    }

    public void rmDocument(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        int docCode = Integer.parseInt(args[0]);

        Document doc = findDocument(docCode);

        if (doc == null || doc.isOut())
            return;

        removeDocument(doc);

        System.out.println("Removed doc: " + doc.getCode() + " - " + doc.getTitle());
    }

    public void searchDocument(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        String title = args[0];

        List<Document> docs = findAllDocuments();

        List<Document> docsFound = new LinkedList<Document>();
        for (Document doc : docs) {
            if (doc.getTitle().indexOf(title) != -1)
                docsFound.add(doc);
        }

        for (Document doc : docsFound) {
            System.out.println("Doc found: " + doc.getCode() + " - " + doc.getTitle());
        }
    }

    public void printDoc(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        int docCode = Integer.parseInt(args[0]);
        Document doc = findDocument(docCode);
        if (doc != null)
            doc.printInfo();
    }/*
     * Loan management commands
     */

    public void borrowDoc(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 2)
            return;

        int userCode = Integer.parseInt(args[0]);
        User user = findUser(userCode);

        int docCode = Integer.parseInt(args[1]);
        Document doc = findDocument(docCode);

        if (user == null || doc == null)
            return;

        if (user.numberOfLoans() < MAX_NUMBER_OF_LOANS && doc.isAvailable() && doc.authorizedLoan(user)
                && user.getLatePenalty() == 0) {
            Loan loan = new Loan(user, doc);

            addLoan(loan);

            System.out.println("New loan: " + user.getName() + " - " + doc.getTitle());
        }
    }

    public void returnDocument(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 1)
            return;

        int docCode = Integer.parseInt(args[0]);
        Document doc = findDocument(docCode);
        if (doc == null)
            return;

        User user = doc.getBorrower();
        if (user == null)
            return;

        if (doc.isOut()) {
            Loan loan = findLoan(user, doc);
            if (loan == null)
                return;

            loan.getUser().addCharge(loan.getLateCharge(LocalDate.now()));

            rmLoan(loan);

            System.out.println("Loan closed: " + user.getName() + " - " + doc.getTitle());
        }
    }

    public void isHolding(String cmd) {
        String args[] = getArgs(cmd);
        if (args.length < 2)
            return;

        int userCode = Integer.parseInt(args[0]);
        User user = findUser(userCode);

        int docCode = Integer.parseInt(args[1]);
        Document doc = findDocument(docCode);

        if (user == null || doc == null)
            return;

        if (findLoan(user, doc) != null)
            System.out.println(user.getName() + " is holding " + doc.getTitle());
        else
            System.out.println(user.getName() + " is not holding " + doc.getTitle());
    }

    public void printAllLoans() {
        findAllLoans().stream().forEach(Loan::print);
    }/*
     *
     */

    public void addLoan(Loan loan) {
        if (loan == null)
            return;

        User user = loan.getUser();
        Document doc = loan.getDocument();

        user.addLoan(loan);
        saveUser(user);

        doc.addLoan(loan);
        saveDocument(doc);
    }

    public void rmLoan(Loan loan) {
        if (loan == null)
            return;

        User user = loan.getUser();
        Document doc = loan.getDocument();

        user.removeLoan(loan);
        saveUser(user);

        doc.removeLoan();
        saveDocument(doc);
    }

    public String[] getArgs(String cmd) {
        String args[] = new String[0];
        String s = cmd.trim();
        if (s.indexOf(" ") != -1) {
            s = s.substring(s.indexOf(" "));
            args = s.trim().split(",");
            for (int i = 0; i < args.length; i++)
                args[i] = args[i].trim();
        }
        return args;
    }

    public void generateSamples() {
        addUser("addUser Paula, Seoul, 010-234-1837");
        addUser("addUser Michael, Washington, 515-234-5667");
        addUser("addUser Neuman, Seatle, 511-927-3857");
        addStaff("addUser John, Pusan, 010-563-5827, A001");
        addStaff("addUser Peter, Ansan, 010-8765-1038, A002");
        addBook("addBook Refactoring, Fowler, ISBN001");
        addBook("addBook Clean Code, Martin, ISBN002");
        addBook("addBook Design Patterns, GOF, ISBN003");
        addJournal("addJournal Communications of ACM");
        addReport("addReport Software Architecture, J.S. Kim, TX001-0284, Hanyang University");

        borrowDoc("borrowDoc 0, 0");
        borrowDoc("borrowDoc 1, 1");
    }/*
     * Database Access private methods
     */

    public TypedQuery<Loan> createLoanQuery(User user, Document doc) {
        return RepositoryDBImpl.em.createQuery("SELECT c FROM Loan c WHERE c.user = :user and c.document = :doc", Loan.class)
                .setParameter("user", user).setParameter("doc", doc);
    }

    public User findUser(int userCode) {
        return RepositoryDBImpl.em.find(User.class, userCode);
    }

    public Document findDocument(int docCode) {
        return RepositoryDBImpl.em.find(Document.class, docCode);
    }

    public Loan findLoan(User user, Document doc) {
        TypedQuery<Loan> query = createLoanQuery(user, doc);
        Loan loan = query.getSingleResult();

        return loan;
    }

    public List<User> findAllUsers() {
        TypedQuery<User> query = RepositoryDBImpl.em.createQuery("SELECT c FROM User c", User.class);
        return query.getResultList();
    }

    public List<Document> findAllDocuments() {
        TypedQuery<Document> query = RepositoryDBImpl.em.createQuery("SELECT c FROM Document c", Document.class);
        List<Document> docs = query.getResultList();
        return docs;
    }

    public List<Loan> findAllLoans() {
        TypedQuery<User> query = RepositoryDBImpl.em.createQuery("SELECT c FROM User c", User.class);
        List<User> users = query.getResultList();
        return users.stream().flatMap(u -> u.getLoans().stream()).collect(Collectors.toList());
    }

    public void saveUser(User user) {
        try {
            RepositoryDBImpl.em.getTransaction().begin();
            RepositoryDBImpl.em.persist(user);
            RepositoryDBImpl.em.getTransaction().commit();
        } catch (PersistenceException e) {
            return;
        }
    }

    public void removeUser(User user) {
        try {
            RepositoryDBImpl.em.getTransaction().begin();
            RepositoryDBImpl.em.remove(user);
            RepositoryDBImpl.em.getTransaction().commit();
        } catch (PersistenceException e) {
            return;
        }
    }

    public void saveDocument(Document doc) {
        try {
            RepositoryDBImpl.em.getTransaction().begin();
            RepositoryDBImpl.em.persist(doc);
            RepositoryDBImpl.em.getTransaction().commit();
        } catch (PersistenceException e) {
            return;
        }
        return;
    }

    public void removeDocument(Document doc) {
        try {
            RepositoryDBImpl.em.getTransaction().begin();
            RepositoryDBImpl.em.remove(doc);
            RepositoryDBImpl.em.getTransaction().commit();
        } catch (PersistenceException e) {
            return;
        }
    }
}