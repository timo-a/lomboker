package de.lomboker.app;

import de.lomboker.app.getter.ReduceGetter;
import de.lomboker.app.setter.ReduceSetter;
import picocli.CommandLine.Command;

@Command(name = "reduce",
        subcommands = {
            ReduceGetter.class,
            ReduceSetter.class},
        description = "no options or positional parameters")
public class Reduce implements Runnable {

    @Override
    public void run() {
        System.out.println("I'm Reduce. You need to call a subcommand like getter|setter");
    }

}
