package de.lomboker.app;

import de.lomboker.app.constructor.ReduceNoArgsConstructor;
import de.lomboker.app.getter.ReduceGetter;
import de.lomboker.app.setter.ReduceSetter;
import de.lomboker.app.summarize.SummarizeGetterSetter;
import picocli.CommandLine.Command;

@Command(name = "summarize",
        subcommands = {
            SummarizeGetterSetter.class},
        description = "no options or positional parameters")
public class Summarize implements Runnable {

    @Override
    public void run() {
        System.out.println("I'm Summarize. You need to call a subcommand like gs");
    }

}
