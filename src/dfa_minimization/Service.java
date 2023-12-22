package dfa_minimization;

import java.util.*;

public class Service {
    public static void createDataFromKeyboard() {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine());
        List<Model.StateTransition> stateTransitions =
                Application.stateTransitions = new ArrayList<>(n);
        Map<String, Model.State> states =
                Application.mapStates = new TreeMap<>();
        Map<String, Model.CharacterInput> characters =
                Application.mapCharacters = new TreeMap<>();

        // Create State Transition Functions
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] words = line.split(" ");
            // Create Current State of function
            Model.State currentState = new Model.State(words[0]);
            if (!states.containsKey(currentState.toString())) {
                states.put(currentState.toString(), currentState);
            } else {
                currentState = states.get(currentState.toString());
            }
            // Create Character Input of function
            Model.CharacterInput characterInput = new Model.CharacterInput(words[1]);
            if (!characters.containsKey(words[1])) {
                characters.put(words[1], characterInput);
            } else {
                characterInput = characters.get(words[1]);
            }
            // Create Target State of function
            Model.State targetState = new Model.State(words[2]);
            if (!states.containsKey(targetState.toString())) {
                states.put(targetState.toString(), targetState);
            } else {
                targetState = states.get(targetState.toString());
            }
            // Create State Transition Function
            stateTransitions.add(new Model.StateTransition(currentState, characterInput, targetState));
        }

        // Create Initial state
        Application.initialState = states.get("{" + scanner.nextLine() + "}");

        // Create finish states
        int m = Integer.parseInt(scanner.nextLine());
        List<Model.State> finishStates = Application.finishStates = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            String word = scanner.nextLine();
            finishStates.add(states.get("{" + word + "}"));
        }

        scanner.close();
    }

    public static void removeState(Model.State state) {
        Application.mapStates.remove(state.toString());
        Application.stateTransitions.removeIf(
                stateTransition -> stateTransition.currentState.compareTo(state) == 0
                                || stateTransition.targetState.compareTo(state) == 0
        );
    }

    public static void removeUnreachableState() {
        // Find reachable states
        Set<Model.State> reachableStates = new HashSet<>();
        for (Model.StateTransition stateTransition : Application.stateTransitions) {
            reachableStates.add(stateTransition.targetState);
        }

        // Find unreachable states
        Application.unreachableStates = new LinkedList<>();
        for (Model.State state : Application.mapStates.values()) {
            if (state.compareTo(Application.initialState) != 0
                    && !Application.finishStates.contains(state)
                    && !reachableStates.contains(state)) {
                Application.unreachableStates.add(state);
            }
        }

        // Remove unreachable states
        for (Model.State state : Application.unreachableStates) removeState(state);

        View.printAfterRemoveUnreachableStates();
    }

    public static void minimizationAndPrintTablePerStep() {
        System.out.println("STEP 2: CREATE AND UPDATE TABLE");

        // Create initial table
        Application.table = new boolean[Application.mapStates.size()][Application.mapStates.size()];
        Map<Model.State, Integer> mapIndexes = new LinkedHashMap<>();
        Model.State[] states = new Model.State[Application.mapStates.size()];
        int i = 0;
        for (Model.State state : Application.mapStates.values()) {
            states[i] = state;
            mapIndexes.put(state, i++);
        }

        for (Model.State state: Application.finishStates) {
            int index = mapIndexes.get(state);
            for (i = 0; i < index; i++) {
                if (Application.finishStates.contains(states[i])) continue;
                Application.table[index][i] = true; // marked 'X'
            }
            for (i = index + 1; i < states.length; i++) {
                if (Application.finishStates.contains(states[i])) continue;
                Application.table[i][index] = true; // marked 'X'
            }
        }
        View.printTable();

        // Update table
        boolean done;
        do {
            done = true;
            for (i = 0; i < states.length; i++) {
                for (int j = 0; j < i; j++) {
                    Model.State state1 = states[i];
                    Model.State state2 = states[j];
                    if (Application.table[i][j]) continue;
                    for (Model.CharacterInput c : Application.mapCharacters.values()) {
                        Model.State targetState1 = getTargetState(state1, c);
                        Model.State targetState2 = getTargetState(state2, c);
                        if (targetState1 == null && targetState2 == null) continue;
                        if (targetState1 == null
                                || targetState2 == null
                                || isMarked(targetState1, targetState2, mapIndexes)) {
                            done = false;
                            Application.table[i][j] = true; // mark state1 and state2
                            break;
                        }
                    }
                }
            }
            if (!done) {
                System.out.println();
                View.printTable();
            }
        } while (!done);
    }

    public static Model.State getTargetState(Model.State currentState, Model.CharacterInput c) {
        for (Model.StateTransition stateTransition : Application.stateTransitions) {
            if (stateTransition.currentState.compareTo(currentState) == 0
                    && stateTransition.character.compareTo(c) == 0) {
                return stateTransition.targetState;
            }
        }
        return null;
    }

    public static boolean isMarked(
            Model.State state1,
            Model.State state2,
            Map<Model.State, Integer> mapIndexes) {
        return Application.table[mapIndexes.get(state1)][mapIndexes.get(state2)]
                || Application.table[mapIndexes.get(state2)][mapIndexes.get(state1)];
    }

    public static void mergeStates() {
        Model.State[] states = new Model.State[Application.mapStates.size()];
        int i = 0;
        for (Model.State state : Application.mapStates.values()) states[i++] = state;

        // Update states
        for (i = 0; i < Application.table.length; i++) {
            for (int j = 0; j < i; j++) {
                if (!Application.table[i][j]) {
                    states[i].mergeState(states[j]);
                    states[j].mergeState(states[i]);
                }
            }
        }

        // Update states in application
        Application.mapStates.clear();
        for (Model.State state : states) {
            if (Application.mapStates.containsKey(state.toString())) continue;
            Application.mapStates.put(state.toString(), state);
        }

        // Update state transition functions
        Set<Model.StateTransition> set = new LinkedHashSet<>(Application.stateTransitions);
        Application.stateTransitions.clear();
        Application.stateTransitions.addAll(set);

        // Update finish states
        Set<Model.State> set0 = new LinkedHashSet<>(Application.finishStates);
        Application.finishStates.clear();
        Application.finishStates.addAll(set0);

        View.printAfterMergeStates();
    }
}
