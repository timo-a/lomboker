package de.lomboker.app;

import de.lomboker.app.getter.MarkGetter;
import de.lomboker.app.getter.ReduceGetter;
import de.lomboker.app.setter.MarkSetter;
import de.lomboker.app.setter.ReduceSetter;
import picocli.CommandLine.Command;

@Command(name = "mark",
        subcommands = {
            MarkGetter.class,
            MarkSetter.class},
        description = "mark non trivial setters")
public class Mark implements Runnable {

    @Override
    public void run() {
        System.out.println("I'm Mark. You need to call a subcommand like getter|setter");
    }

}
