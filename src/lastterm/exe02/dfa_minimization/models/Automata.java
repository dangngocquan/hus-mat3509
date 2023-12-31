package lastterm.exe02.dfa_minimization.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Automata {
    public Map<String, State> mapStates;
    public Map<String, CharacterInput> mapCharacters;
    public List<StateTransition> stateTransitions;
    public State initialState;
    public List<State> finishStates;
    public List<State> unreachableStates;
    public boolean[][] tableMinimization;

    public Automata() {
        mapStates = new TreeMap<>();
        mapCharacters = new TreeMap<>();
        stateTransitions = new LinkedList<>();
        initialState = null;
        finishStates = new LinkedList<>();
        unreachableStates = new LinkedList<>();
    }
}