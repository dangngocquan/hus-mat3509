package dfa_minimization;

import java.util.List;
import java.util.Map;

public class Application {
    public static Map<String, Model.State> mapStates;
    public static Map<String, Model.CharacterInput> mapCharacters;
    public static List<Model.StateTransition> stateTransitions;
    public static Model.State initialState;
    public static List<Model.State> finishStates;
    public static List<Model.State> unreachableStates;
    public static boolean[][] table;
    private final Controller controller;

    public Application() {
        controller = new Controller();
    }

    public void run() {
        controller.printInputForm();
        controller.createDataFromKeyboard();
        controller.solve();
    }
}
