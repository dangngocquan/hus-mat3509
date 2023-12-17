package nfa_to_dfa;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Model {
    public static class State implements Comparable<Model.State>{
        public List<String> names;

        public State(String name) {
            this.names = new LinkedList<>();
            names.add(name);
        }

        public State() {
            this.names = new LinkedList<>();
        }

        public void mergeState(Model.State state) {
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

        public List<State> toListState() {
            List<State> list = new LinkedList<>();
            for (String name : names) list.add(new State(name));
            return list;
        }

        @Override
        public int compareTo(Model.State o) {
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

    public static class CharacterInput implements Comparable<Model.CharacterInput> {
        public String name;

        public CharacterInput(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(Model.CharacterInput o) {
            return this.name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class StateTransition implements Comparable<Model.StateTransition> {
        public Model.State currentState;
        public Model.CharacterInput character;
        public Model.State targetState;

        public StateTransition(Model.State currentState, Model.CharacterInput character, Model.State targetState) {
            this.currentState = currentState;
            this.character = character;
            this.targetState = targetState;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model.StateTransition that = (Model.StateTransition) o;
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
        public int compareTo(Model.StateTransition o) {
            return this.toString().compareTo(o.toString());
        }
    }
}
