/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.lomboker.app;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "lomboker",
        subcommands = {
                CounterApp.class,
                Reduce.class,
                Mark.class
})
public class App implements Runnable {
    public static void main(String[] args) {
        new CommandLine(new App()).execute(args);
    }

    @Override
    public void run() {
        System.out.println("I'm lomboker. you need to cal a subcommand like count|reduce|mark");
    }

}
