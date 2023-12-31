package lastterm.exe02.dfa_minimization.models;

public class CharacterInput implements Comparable<CharacterInput> {
    public String name;

    public CharacterInput(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CharacterInput o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}