package lastterm.exe01.dfa_minimization.models;

import java.util.Objects;

public class StateTransition implements Comparable<StateTransition> {
    public State currentState;
    public CharacterInput character;
    public State targetState;

    public StateTransition(State currentState, CharacterInput character, State targetState) {
        this.currentState = currentState;
        this.character = character;
        this.targetState = targetState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateTransition that = (StateTransition) o;
        return toString().compareTo(that.toString()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public String toString() {
        return "δ(" + currentState + ", " + character + ") = " + targetState;
    }

    @Override
    public int compareTo(StateTransition o) {
        return this.toString().compareTo(o.toString());
    }
}
