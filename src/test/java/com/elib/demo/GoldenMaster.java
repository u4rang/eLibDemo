package com.elib.demo;

import com.elib.demo.ui.CmdUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class GoldenMaster {

    public static final String GOLDENMASTER_GOLDENMASTER = "goldenmaster/goldenmaster.txt";

    private CmdUI ui;


    String simulatedInput = "printUser 0\n"
            + "printDoc 1\n"
            + "printLoans\n"
//				+ "isHolding 0, 1\n"
            + "searchUser Paula\n"
            + "searchDoc Refactoring\n"
            + "returnDoc 0\n"
            + "borrowDoc 0, 2\n"
            + "rmDoc 3\n"
            + "rmUser 3\n"
            + "addUser Kershaw, LA, 010-2220-2345\n"
            + "addStaff Furguson, London, 010-3456-1111, A777\n"
            + "addBook CQRS, Young, ISBN8938\n"
            + "addReport Event Storming, Brandolini, TX0287, Consulting\n"
            + "addJournal Software Architectures\n"
            + "exit\n"; // Bye

    public void generateGoldenMaster() throws IOException {
        String result = runResult();
        Files.write(Paths.get(GOLDENMASTER_GOLDENMASTER), result.getBytes());
    }

    public String readGoldenMaster() throws IOException{
        return Files.readAllLines(Paths.get(GOLDENMASTER_GOLDENMASTER)).stream().collect(Collectors.joining("\n")) + "\n";
    }

    public String runResult(){

        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        PrintStream pstream = new PrintStream(ostream);
        System.setOut(pstream);

        ByteArrayInputStream istream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(istream);

        ui = new CmdUI();
        ui.start();

        return ostream.toString();

    }
}
