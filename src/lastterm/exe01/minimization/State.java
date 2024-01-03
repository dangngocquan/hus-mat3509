package lastterm.exe01.minimization;

import java.util.TreeSet;

public class State implements Comparable<State> {
    public TreeSet<String> names;

    public State() {
        this.names = new TreeSet<>();
    }

    public State(String name) {
        this.names = new TreeSet<>();
        this.names.add(name);
    }

    @Override
    public int compareTo(State o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (String name : names) sb.append(name).append(", ");
        if (sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    public State cloneState() {
        State state = new State();
        state.names.addAll(this.names);
        return state;
    }

    public void mergeState(State o) {
        this.names.addAll(o.names);
    }
}
