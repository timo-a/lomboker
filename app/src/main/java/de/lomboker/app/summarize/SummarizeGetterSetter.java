package de.lomboker.app.summarize;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static de.lomboker.lib.SummarizeGetterSetter.summarizeGetters;
import static de.lomboker.lib.SummarizeGetterSetter.summarizeSetters;

@CommandLine.Command(name = "gs", description = "summarize getters and setters")
public class SummarizeGetterSetter implements Runnable {
    @CommandLine.Parameters(index = "0")
    File file;

    @Override
    public void run() {
        try {
            String code = Files.readString(file.toPath());
            String convG = summarizeGetters(code);
            String convGS = summarizeSetters(convG);
            Files.writeString(file.toPath(), convGS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
