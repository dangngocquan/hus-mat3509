package lastterm.exe01.minimization;

import java.util.*;

public class Automata {
    public TreeSet<State> states;
    public TreeSet<String> chars;
    public State initialState;
    public TreeSet<State> finishStates;
    public Map<State, Map<String, State>> transitions;

    public Automata() {
        this.states = new TreeSet<>();
        this.chars = new TreeSet<>();
        this.initialState = null;
        this.finishStates = new TreeSet<>();
        this.transitions = new LinkedHashMap<>();
    }

    public Automata(
            TreeSet<State> states,
            TreeSet<String> chars,
            State initialState,
            TreeSet<State> finishStates,
            Map<State, Map<String, State>> transitions) {
        this.states = states;
        this.chars = chars;
        this.initialState = initialState;
        this.finishStates = finishStates;
        this.transitions = transitions;
    }

    public State[][] getStateTransitionTable() {
        // Clone 'chars' from Set to Array
        String[] chars = new String[this.chars.size()];
        Iterator<String> iterator = this.chars.iterator();
        for (int i = 0; i < chars.length; i++) chars[i] = iterator.next();

        // Create a list of state rows
        List<State[]> rows = new LinkedList<>();    // list rows - table states
        List<State> states = new LinkedList<>(this.states); // clone 'states'
        List<State> checkedStates = new LinkedList<>(); // contains checked states in procession
        Queue<State> queue = new LinkedList<>(); // save states in procession
        if (initialState != null) {
            queue.add(initialState);
            checkedStates.add(initialState);
            states.remove(initialState);
        }
        while (!queue.isEmpty() || !states.isEmpty()) {
            State[] columns = new State[chars.length + 1]; // a row in table - an element in list 'rows'
            columns[0] = queue.isEmpty()? states.remove(0) : queue.poll();
            for (int i = 0; i < chars.length; i++) {
                columns[i+1] = getStateTarget(columns[0], chars[i]);
                states.remove(columns[i+1]); // remove checked state
                if (!checkedStates.contains(columns[i+1]) && columns[i+1] != null) {
                    queue.add(columns[i+1]);
                    checkedStates.add(columns[i+1]); // add to list checked states
                }
            }
            rows.add(columns);
        }

        // Convert List<State[]> to State[][]
        State[][] table = new State[rows.size()][chars.length+1];
        for (int i = 0; i < rows.size(); i++) table[i] = rows.get(i);

        return table;
    }

    public String[][] getStateStringTransitionTable() {
        // Clone 'chars' from Set to Array
        String[] chars = new String[this.chars.size()];
        Iterator<String> iterator = this.chars.iterator();
        for (int i = 0; i < chars.length; i++) chars[i] = iterator.next();

        // Create State String Table
        State[][] stateTransition = getStateTransitionTable();
        String[][] stateStringTable = new String[stateTransition.length + 1][chars.length + 1];
        stateStringTable[0][0] = "δ";
        System.arraycopy(chars, 0, stateStringTable[0], 1, chars.length);
        for (int i = 0; i  < stateTransition.length; i++)
            for (int j = 0; j < stateTransition[i].length; j++) {
                stateStringTable[i+1][j] = stateTransition[i][j] == null? "" : stateTransition[i][j].toString();
            }

        return stateStringTable;
    }

    public String[][] getTableMark(Automata automata, boolean[][] table) {
        // Clone 'states' from Set to Array
        String[] states = new String[automata.states.size()];
        Iterator<State> iterator = automata.states.iterator();
        for (int i = 0; i < states.length; i++) states[i] = iterator.next().toString();

        // Create table string
        String[][] stringTable = new String[states.length + 1][states.length + 1];
        for (int i = 0; i < states.length; i++) {
            stringTable[i][0] = states[i];
            for (int j = 0; j < table.length; j++) stringTable[i][j+1] = table[i][j]? "X" : "";
        }
        stringTable[states.length][0] = "";
        System.arraycopy(states, 0, stringTable[states.length], 1, states.length);
        return stringTable;
    }

    public State getStateTarget(State currentState, String character) {
        return transitions
                .getOrDefault(currentState, new LinkedHashMap<>())
                .getOrDefault(character, null);
    }

    public void addTransition(State currentState, String character, State targetState) {
        if (!transitions.containsKey(currentState))
            transitions.put(currentState, new LinkedHashMap<>());
        transitions.get(currentState).put(character, targetState);
    }

    public void addFinishState(State state) {
        finishStates.add(state);
    }

    public void setInitialState(State state) {
        initialState = state;
    }

    public void addState(State state) {
        states.add(state);
    }

    public void addCharacter(String character) {
        chars.add(character);
    }

    @Override
    public String toString() {
        return "AUTOMATA" + "\n" +
                "\t" + "- States: " + states + "\n" +
                "\t" + "- Characters: " + chars + "\n" +
                "\t" + "- Initial state: " + initialState + "\n" +
                "\t" + "- Finish states: " + finishStates + "\n" +
                "\t" + "- State Transition Functions: " + "\n" +
                "\t  " +
                Service.matrixToTable(getStateStringTransitionTable())
                        .replaceAll("\n", "\n\t  ");
    }

    public Automata cloneAutomata() {
        TreeSet<State> states = new TreeSet<>();
        for (State state : this.states) states.add(state.cloneState());
        TreeSet<String> chars = new TreeSet<>(this.chars);
        State initialState = this.initialState.cloneState();
        TreeSet<State> finishStates = new TreeSet<>();
        for (State state : this.finishStates) finishStates.add(state.cloneState());
        Map<State, Map<String, State>> transitions = new LinkedHashMap<>();
        for (State keyState: this.transitions.keySet()) {
            transitions.put(keyState.cloneState(), new LinkedHashMap<>());
            Map<String, State> map = this.transitions.get(keyState);
            for (String keyChar: map.keySet()) {
                transitions.get(keyState).put(keyChar, map.get(keyChar).cloneState());
            }
        }
        return new Automata(states, chars, initialState, finishStates, transitions);
    }

    public Automata minimize() {
        Automata automata = cloneAutomata();
        minimizeStep1(automata);
        boolean[][] tableMark = minimizeStep2(automata);
        minimizeStep3(automata, tableMark);
        return automata;
    }

    public void minimizeStep1(Automata automata) {
        // Find reachable states
        Set<State> reachableStates = new TreeSet<>();
        if (automata.initialState != null) reachableStates.add(automata.initialState);
        for (State currentState : automata.transitions.keySet()) {
            Map<String, State> map = automata.transitions.get(currentState);
            for (State targetState: map.values()) {
                if (currentState.compareTo(targetState) != 0) reachableStates.add(targetState);
            }
        }

        // Find unreachable states
        Set<State> unreachableStates = new TreeSet<>(automata.states);
        unreachableStates.removeAll(reachableStates);

        // Remove unreachable states
        automata.states.removeAll(unreachableStates);
        for (State unreachableState: unreachableStates) automata.transitions.remove(unreachableState);
        automata.finishStates.removeAll(unreachableStates);

        // Save process to file
        String contents = "\n\n" +
                "STEP 01: Check and remove unreachable states." + "\n" +
                "Unreachable states: " + unreachableStates + "\n" +
                automata + "\n";
        Service.writeFile(contents);
    }

    public boolean[][] minimizeStep2(Automata automata) {
        // Clone 'states' from Set to List
        List<State> states = new LinkedList<>(automata.states);

        StringBuilder contents = new StringBuilder("\n\nSTEP 02: Minimization automata.\n");

        // Create initial table mark
        boolean[][] table = new boolean[states.size()][states.size()];
        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < states.size(); j++) {
                if (i == j) continue;
                table[i][j] = automata.finishStates.contains(states.get(i))
                        != automata.finishStates.contains(states.get(j));
            }
        }
        contents.append("Table:\n").append(Service.matrixToTablePattern2(getTableMark(automata, table))).append("\n\n");

        // Update table
        boolean done;
        do {
            done = true;
            for (int i = 0; i < states.size(); i++) {
                for (int j = 0; j < states.size(); j++) {
                    if (i == j) continue;
                    if (table[i][j]) continue;
                    for (String character : automata.chars) {
                        State targetSi = getStateTarget(states.get(i), character);
                        State targetSj = getStateTarget(states.get(j), character);
                        int indexOfTargetSi = states.indexOf(targetSi);
                        int indexOfTargetSj = states.indexOf(targetSj);
                        if (indexOfTargetSi == -1 && indexOfTargetSj == -1) continue;
                        if (indexOfTargetSi == -1 || indexOfTargetSj == -1) {
                            table[i][j] = true; // marked
                            done = false;
                            break;
                        }
                        if (table[indexOfTargetSi][indexOfTargetSj]) {
                            table[i][j] = true;
                            done = false;
                            break;
                        }
                    }
                }
            }
            if (!done) contents.append("Table:\n").append(Service.matrixToTablePattern2(getTableMark(automata, table))).append("\n\n");
        } while (!done);

        // Save contents process to file
        Service.writeFile(contents.toString());

        return table;
    }

    public void minimizeStep3(Automata automata, boolean[][] tableMark) {
        // Clone 'states' from Set to List
        List<State> states = new LinkedList<>(automata.states);

        // Mapping old state with new state
        Map<State, State> mapStates = new LinkedHashMap<>(); // <OldState, NewState>
        for (int i = 0; i < tableMark.length; i++) {
            State newSi = new State();
            newSi.mergeState(states.get(i));
            for (int j = 0; j < tableMark[i].length; j++)
                if (!tableMark[i][j]) newSi.mergeState(states.get(j));
            mapStates.put(states.get(i), newSi);
        }

        // Update states
        TreeSet<State> newStates = new TreeSet<>();
        for (State oldState : automata.states) newStates.add(mapStates.get(oldState));

        // Update initial state
        State newInitialState = mapStates.get(automata.initialState);

        // Update finish states
        TreeSet<State> newFinishStates = new TreeSet<>();
        for (State oldState : automata.finishStates) newFinishStates.add(mapStates.get(oldState));

        // Update transition functions
        Map<State, Map<String, State>> newTransitions = new LinkedHashMap<>();
        for (State key : automata.transitions.keySet()) {
            State newKey = mapStates.get(key);
            Map<String, State> newMapValue = new LinkedHashMap<>();
            Map<String, State> oldMapValue = automata.transitions.get(key);
            for (String character : oldMapValue.keySet()) {
                newMapValue.put(character, mapStates.get(oldMapValue.get(character)));
            }
            if (!newTransitions.containsKey(newKey)) newTransitions.put(newKey, new LinkedHashMap<>());
            newTransitions.get(newKey).putAll(newMapValue);
        }

        // Update automata
        automata.states = newStates;
        automata.initialState = newInitialState;
        automata.finishStates = newFinishStates;
        automata.transitions = newTransitions;

        // Save to file
        String contents = "\nSTEP 3: Merge states." + "\n" + automata;
        Service.writeFile(contents);
    }

}
