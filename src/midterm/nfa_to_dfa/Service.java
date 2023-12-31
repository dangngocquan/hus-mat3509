package midterm.nfa_to_dfa;

import java.util.*;

public class Service {
    public static void createDataFromKeyboard() {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine());
        Set<Model.StateTransition> stateTransitions =
                Application.stateTransitions = new LinkedHashSet<>();
        Map<String, Model.State> states =
                Application.mapStates = new LinkedHashMap<>();
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
            String[] characterWords = words[1].split(",");
            Model.CharacterInput[] characterInputs = new Model.CharacterInput[characterWords.length];
            for (int j = 0; j < characterInputs.length; j++) {
                characterInputs[j] = new Model.CharacterInput(characterWords[j]);
                if (!characters.containsKey(characterWords[j])) {
                    characters.put(characterWords[j], characterInputs[j]);
                } else {
                    characterInputs[j] = characters.get(characterWords[j]);
                }
            }

            // Create Target State of function
            Model.State targetState = new Model.State(words[2]);
            if (!states.containsKey(targetState.toString())) {
                states.put(targetState.toString(), targetState);
            } else {
                targetState = states.get(targetState.toString());
            }
            // Create State Transition Function
            for (int j = 0; j < characterInputs.length; j++) {
                stateTransitions.add(new Model.StateTransition(currentState, characterInputs[j], targetState));
            }
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

    public static List<Model.State> getTargetState(Model.State currentState, Model.CharacterInput c) {
        List<Model.State> list = new LinkedList<>();
        for (Model.StateTransition stateTransition : Application.stateTransitions) {
            if (stateTransition.currentState.compareTo(currentState) == 0
                    && stateTransition.character.compareTo(c) == 0) {
                list.add(stateTransition.targetState);
            }
        }
        return list;
    }

    public static void nfaToDfa() {
        Application.table = new LinkedList<>(); // will contains last result
        Model.CharacterInput[] characters = new Model.CharacterInput[Application.mapCharacters.size()];
        int i = 0;
        for (Model.CharacterInput c : Application.mapCharacters.values()) characters[i++] = c;
        Set<Model.State> newStates = new LinkedHashSet<>(); // will contains last result
        Set<Model.State> newFinishStates = new LinkedHashSet<>(); // will contains last result
        Queue<Model.State> queue = new LinkedList<>();
        queue.add(Application.initialState);
        newStates.add(Application.initialState);

        while (!queue.isEmpty()) {
            // State use in this row
            Model.State state = queue.poll();
            Model.State[] rowStates = new Model.State[Application.mapCharacters.size()];
            for (i = 0; i < rowStates.length; i++) {
                List<Model.State> targetStates = getTargetState(state, characters[i]);
                if (!targetStates.isEmpty()) {
                    Model.State newState = new Model.State();
                    for (Model.State targetState : targetStates) {
                        newState.mergeState(targetState);
                    }
                    rowStates[i] = newState;

                    // Add new state transition functions
                    Application.stateTransitions.add(
                            new Model.StateTransition(state, characters[i], newState)
                    );
                    for (String nameState : newState.names) {
                        List<Model.StateTransition> tempList = new LinkedList<>();
                        for (Model.StateTransition stateTransition : Application.stateTransitions) {
                            if (stateTransition.currentState.toString().compareTo("{" + nameState + "}") == 0) {
                                tempList.add(
                                        new Model.StateTransition(
                                                newState, stateTransition.character, stateTransition.targetState
                                        )
                                );
                            }
                        }
                        Application.stateTransitions.addAll(tempList);
                    }

                    // Check new finish state
                    if (isIntersectState(Application.finishStates, state.toListState())) {
                        newFinishStates.remove(state);
                        Model.State newFinishState = new Model.State();
                        newFinishState.mergeState(state);
                        newFinishState.mergeState(newState);
                        newFinishStates.add(newFinishState);
                    }

                    if (!newStates.contains(newState)) {
                        queue.add(newState);
                        newStates.add(newState);
                    }
                }
            }

            Application.table.add(rowStates);
        }

        // Update data
        Application.mapStates.clear();
        for (Model.State state : newStates) Application.mapStates.put(state.toString(), state);

        Iterator<Model.State> iterator = Application.mapStates.values().iterator();
        Application.stateTransitions.clear();
        for (i = 0; i < Application.table.size(); i++) {
            Model.State state = iterator.next();
            for (int j = 0; j < characters.length; j++) {
                if (Application.table.get(i)[j] == null) continue;
                Application.stateTransitions.add(
                        new Model.StateTransition(
                              state, characters[j], Application.table.get(i)[j]
                        )
                );
            }
        }

        Application.finishStates.addAll(newFinishStates);
        Set<Model.State> temp = new LinkedHashSet<>();
        for (Model.State state : Application.finishStates) {
            if (Application.mapStates.containsKey(state.toString())) temp.add(state);
        }
        Application.finishStates = new LinkedList<>(temp);

        View.printTable();
        System.out.println();
        View.printDataOfAutomata();
    }

    public static boolean isIntersectState(List<Model.State> a, List<Model.State> b) {
        for (Model.State state : a) {
            if (b.contains(state)) return true;
        }
        return false;
    }
}
