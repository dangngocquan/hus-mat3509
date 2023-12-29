package dfa_minimization;

import dfa_minimization.models.Automata;

public class Application {
    public static Automata automata;
    private final Controller controller;

    public Application() {
        controller = new Controller();
    }

    public void run() {
        controller.printInputForm();
        controller.createAutomataFromKeyboard();
        controller.minimizationAutomata();
    }
}
