package dfa_minimization;

import dfa_minimization.models.Automata;
import dfa_minimization.models.CharacterInput;
import dfa_minimization.models.State;
import dfa_minimization.models.StateTransition;

import java.util.*;

public class Service {
    public static Automata createAutomataFromKeyboard() {
        Automata automata = new Automata();
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine());
        List<StateTransition> stateTransitions =
                automata.stateTransitions = new ArrayList<>(n);
        Map<String, State> states =
                automata.mapStates = new TreeMap<>();
        Map<String, CharacterInput> characters =
                automata.mapCharacters = new TreeMap<>();

        // Create State Transition Functions
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] words = line.split(" ");
            // Create Current State of function
            State currentState = new State(words[0]);
            if (!states.containsKey(currentState.toString())) {
                states.put(currentState.toString(), currentState);
            } else {
                currentState = states.get(currentState.toString());
            }
            // Create Character Input of function
            CharacterInput characterInput = new CharacterInput(words[1]);
            if (!characters.containsKey(words[1])) {
                characters.put(words[1], characterInput);
            } else {
                characterInput = characters.get(words[1]);
            }
            // Create Target State of function
            State targetState = new State(words[2]);
            if (!states.containsKey(targetState.toString())) {
                states.put(targetState.toString(), targetState);
            } else {
                targetState = states.get(targetState.toString());
            }
            // Create State Transition Function
            stateTransitions.add(new StateTransition(currentState, characterInput, targetState));
        }

        // Create Initial state
        automata.initialState = states.get("{" + scanner.nextLine() + "}");

        // Create finish states
        int m = Integer.parseInt(scanner.nextLine());
        List<State> finishStates = automata.finishStates = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            String word = scanner.nextLine();
            finishStates.add(states.get("{" + word + "}"));
        }

        scanner.close();

        return automata;
    }

    public static void removeState(Automata automata, State state) {
        automata.mapStates.remove(state.toString());
        automata.stateTransitions.removeIf(
                stateTransition -> stateTransition.currentState.compareTo(state) == 0
                                || stateTransition.targetState.compareTo(state) == 0
        );
    }

    public static void removeUnreachableState(Automata automata) {
        // Find reachable states
        Set<State> reachableStates = new HashSet<>();
        for (StateTransition stateTransition : automata.stateTransitions) {
            reachableStates.add(stateTransition.targetState);
        }

        // Find unreachable states
        automata.unreachableStates = new LinkedList<>();
        for (State state : automata.mapStates.values()) {
            if (state.compareTo(automata.initialState) != 0
                    && !automata.finishStates.contains(state)
                    && !reachableStates.contains(state)) {
                automata.unreachableStates.add(state);
            }
        }

        // Remove unreachable states
        for (State state : automata.unreachableStates) removeState(automata, state);

        View.printAfterRemoveUnreachableStates(automata);
    }

    public static void minimizationAndPrintTablePerStep(Automata automata) {
        System.out.println("STEP 2: CREATE AND UPDATE TABLE");

        // Create initial table
        automata.tableMinimization = new boolean[automata.mapStates.size()][automata.mapStates.size()];
        Map<State, Integer> mapIndexes = new LinkedHashMap<>();
        State[] states = new State[automata.mapStates.size()];
        int i = 0;
        for (State state : automata.mapStates.values()) {
            states[i] = state;
            mapIndexes.put(state, i++);
        }

        for (State state: automata.finishStates) {
            int index = mapIndexes.get(state);
            for (i = 0; i < index; i++) {
                if (automata.finishStates.contains(states[i])) continue;
                automata.tableMinimization[index][i] = true; // marked 'X'
            }
            for (i = index + 1; i < states.length; i++) {
                if (automata.finishStates.contains(states[i])) continue;
                automata.tableMinimization[i][index] = true; // marked 'X'
            }
        }
        View.printTable(automata);

        // Update table
        boolean done;
        do {
            done = true;
            for (i = 0; i < states.length; i++) {
                for (int j = 0; j < i; j++) {
                    State state1 = states[i];
                    State state2 = states[j];
                    if (automata.tableMinimization[i][j]) continue;
                    for (CharacterInput c : automata.mapCharacters.values()) {
                        State targetState1 = getTargetState(automata, state1, c);
                        State targetState2 = getTargetState(automata, state2, c);
                        if (targetState1 == null && targetState2 == null) continue;
                        if (targetState1 == null
                                || targetState2 == null
                                || isMarked(automata, targetState1, targetState2, mapIndexes)) {
                            done = false;
                            automata.tableMinimization[i][j] = true; // mark state1 and state2
                            break;
                        }
                    }
                }
            }
            if (!done) {
                System.out.println();
                View.printTable(automata);
            }
        } while (!done);
    }

    public static State getTargetState(Automata automata, State currentState, CharacterInput c) {
        for (StateTransition stateTransition : automata.stateTransitions) {
            if (stateTransition.currentState.compareTo(currentState) == 0
                    && stateTransition.character.compareTo(c) == 0) {
                return stateTransition.targetState;
            }
        }
        return null;
    }

    public static boolean isMarked(
            Automata automata,
            State state1,
            State state2,
            Map<State, Integer> mapIndexes) {
        return automata.tableMinimization[mapIndexes.get(state1)][mapIndexes.get(state2)]
                || automata.tableMinimization[mapIndexes.get(state2)][mapIndexes.get(state1)];
    }

    public static void mergeStates(Automata automata) {
        State[] states = new State[automata.mapStates.size()];
        int i = 0;
        for (State state : automata.mapStates.values()) states[i++] = state;

        // Update states
        for (i = 0; i < automata.tableMinimization.length; i++) {
            for (int j = 0; j < i; j++) {
                if (!automata.tableMinimization[i][j]) {
                    states[i].mergeState(states[j]);
                    states[j].mergeState(states[i]);
                }
            }
        }

        // Update states in application
        automata.mapStates.clear();
        for (State state : states) {
            if (automata.mapStates.containsKey(state.toString())) continue;
            automata.mapStates.put(state.toString(), state);
        }

        // Update state transition functions
        Set<StateTransition> set = new LinkedHashSet<>(automata.stateTransitions);
        automata.stateTransitions.clear();
        automata.stateTransitions.addAll(set);

        // Update finish states
        Set<State> set0 = new LinkedHashSet<>(automata.finishStates);
        automata.finishStates.clear();
        automata.finishStates.addAll(set0);

        View.printAfterMergeStates(automata);
    }
}
