/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lomboker.app;

import lomboker.list.LinkedList;

import static lomboker.utilities.StringUtils.join;
import static lomboker.utilities.StringUtils.split;
import static lomboker.app.MessageUtils.getMessage;

public class App {
    public static void main(String[] args) {
        LinkedList tokens;
        tokens = split(getMessage());
        System.out.println(join(tokens));
    }
}
