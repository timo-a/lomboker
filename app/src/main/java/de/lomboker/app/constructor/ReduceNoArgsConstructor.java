/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.lomboker.app.constructor;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static de.lomboker.lib.NoArgsConstructor.reduceNoArgsConstructor;
import static de.lomboker.lib.TrivialGetters.reduceGetters;

@Command(name = "no-args-constructor", description = "reduce no args constructor")
public class ReduceNoArgsConstructor implements Runnable {

    @Parameters(index = "0")
    File file;

    @Override
    public void run() {
        try {
            String code = Files.readString(file.toPath());
            String converted = reduceNoArgsConstructor(code);
            Files.writeString(file.toPath(), converted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
