package de.lomboker.app.toString;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static de.lomboker.lib.FuzzyToString.reduceFuzzyToString;

@CommandLine.Command(name = "toString", description = "reduce equals and hash code")
public class ReduceFuzzyToString implements Runnable {

    @CommandLine.Parameters(index = "0")
    File file;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ReduceFuzzyToString()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            String code = Files.readString(file.toPath());
            String converted = reduceFuzzyToString(code);
            Files.writeString(file.toPath(), converted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
