package lastterm.exe02.dfa_minimization.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class State implements Comparable<State>{
    public List<String> names;

    public State(String name) {
        this.names = new LinkedList<>();
        names.add(name);
    }

    public void mergeState(State state) {
        for (String name : state.names) {
            if (names.contains(name)) continue;
            names.add(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return toString().compareTo(state.toString()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public int compareTo(State o) {
        List<String> thisClone = new LinkedList<>(this.names);
        List<String> thatClone = new LinkedList<>(o.names);
        Collections.sort(thisClone);
        Collections.sort(thatClone);
        return thisClone.toString().compareTo(thatClone.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        List<String> clone = new LinkedList<>(names);
        Collections.sort(clone);
        for (String name :  clone) sb.append(name).append(", ");
        if (sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }
}