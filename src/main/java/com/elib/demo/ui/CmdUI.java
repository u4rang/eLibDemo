package com.elib.demo.ui;

import com.elib.demo.app.Interactor;

import java.util.Scanner;

public class CmdUI implements UI {

    private Interactor interactor;

    public CmdUI(){
        interactor = new Interactor();
    }

    @Override
    public void start() {

        interactor.generateSamples();
        displayMenu();

        String s = "";
        Scanner scanner = new Scanner(System.in);
        s = scanner.nextLine();
        while (!s.equals("exit")) {
            dispatchCommand(s);

            displayMenu();
            s = scanner.nextLine();
        }

        scanner.close();
    }

    @Override
    public void displayMenu() {
        System.out.println("\nCOMMANDS:");
        System.out.println("addUser name, address, phone");
        System.out.println("addStaff name, address, phone, id");
        System.out.println("rmUser userld");
        System.out.println("addBook title, authors, ISBN");
        System.out.println("addReport title, authors, refs, organization");
        System.out.println("addJournal title");
        System.out.println("rmDoc docid");
        System.out.println("borrowDoc userld, docId");
        System.out.println("returnDoc docId");
        System.out.println("searchUser name");
        System.out.println("searchDoc title");
        System.out.println("isHolding userld, docId");
        System.out.println("printLoans");
        System.out.println("printUser userld");
        System.out.println("printDoc docId");
        System.out.println("exit\n\n");
    }

    @Override
    public void dispatchCommand(String cmd) {
        if (cmd.startsWith("addUser"))
            interactor.addUser(cmd);
        if (cmd.startsWith("addStaff"))
            interactor.addStaff(cmd);
        if (cmd.startsWith("rmUser"))
            interactor.rmUser(cmd);
        if (cmd.startsWith("addBook"))
            interactor.addBook(cmd);
        if (cmd.startsWith("addReport"))
            interactor.addReport(cmd);
        if (cmd.startsWith("addJournal"))
            interactor.addJournal(cmd);
        if (cmd.startsWith("rmDoc"))
            interactor.rmDocument(cmd);
        if (cmd.startsWith("borrowDoc"))
            interactor.borrowDoc(cmd);
        if (cmd.startsWith("returnDoc"))
            interactor.returnDocument(cmd);
        if (cmd.startsWith("searchUser"))
            interactor.searchUser(cmd);
        if (cmd.startsWith("searchDoc"))
            interactor.searchDocument(cmd);
        if (cmd.startsWith("isHolding"))
            interactor.isHolding(cmd);
        if (cmd.startsWith("printLoans"))
            interactor.printAllLoans();
        if (cmd.startsWith("printUser"))
            interactor.printUser(cmd);
        if (cmd.startsWith("printDoc"))
            interactor.printDoc(cmd);
    }
}
