package midterm.nfa_to_dfa;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Application {
    public static Map<String, Model.State> mapStates;
    public static Map<String, Model.CharacterInput> mapCharacters;
    public static Set<Model.StateTransition> stateTransitions;
    public static Model.State initialState;
    public static List<Model.State> finishStates;
    public static List<Model.State[]> table;
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
