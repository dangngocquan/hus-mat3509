package dfa_minimization;

import java.util.Iterator;

public class View {
    public static void printInputForm() {
        String contents = "INPUT FORMAT:\n" +
                "The first line contains a number n - the number of state transition function.\n" +
                "In next n lines, each line defines a State Transition Function, contains 3 words, separate by whitespace: \n" +
                "\t+ The first word is the Current State of function\n" +
                "\t+ The second word is the Character Input of function\n" +
                "\t+ The third word is the Target State of function\n" +
                "Nest line contains a word - the initial state.\n" +
                "Next line contains a number m - the number of finish states.\n" +
                "In next m lines, each line contains a word - the state.\n" +
                "Example for valid input form:\n" +
                "16\n" +
                "A 0 B\n" +
                "A 1 F\n" +
                "B 0 G\n" +
                "B 1 C\n" +
                "C 0 A\n" +
                "C 1 C\n" +
                "D 0 C\n" +
                "D 1 G\n" +
                "E 0 H\n" +
                "E 1 F\n" +
                "F 0 C\n" +
                "F 1 G\n" +
                "G 0 G\n" +
                "G 1 E\n" +
                "H 0 G\n" +
                "H 1 C\n" +
                "A\n" +
                "1\n" +
                "C\n\n" +
                "YOUR INPUT:\n";
        System.out.println(contents);
    }

    public static void printDataOfAutomata() {
        StringBuilder sb = new StringBuilder();
        sb.append("CURRENT AUTOMATA:").append("\n");
        sb.append("\t").append("- States: ").append(Application.mapStates.keySet()).append("\n");
        sb.append("\t").append("- Characters: ").append(Application.mapCharacters.keySet()).append("\n");
        sb.append("\t").append("- Initial state: ").append(Application.initialState).append("\n");
        sb.append("\t").append("- Finish states: ").append(Application.finishStates).append("\n");
        sb.append("\t").append("- State Transition Functions: ").append("\n");
        int i = 1;
        for (Model.StateTransition function : Application.stateTransitions) {
            sb.append("\t\t").append(i++).append(". ").append(function).append("\n");
        }
        System.out.println(sb);
    }

    public static void printAfterRemoveUnreachableStates() {
        System.out.println("STEP 1: Remove unreachable states");
        System.out.println("UNREACHABLE STATES: " + Application.unreachableStates);
        printDataOfAutomata();
    }

    public static void printAfterMergeStates() {
        System.out.println("STEP 3: Update Automata");
        printDataOfAutomata();
    }

    public static void printTable() {
        StringBuilder sb = new StringBuilder();
        int widthColumn = 0;
        for (String state : Application.mapStates.keySet()) widthColumn = Math.max(widthColumn, state.length());
        widthColumn += 2;
        Iterator<String> iterator = Application.mapStates.keySet().iterator();
        iterator.next();
        for (int i = 1; i < Application.table.length; i++) {
            sb.append(String.format(String.format("%%-%ds ", widthColumn), iterator.next()));
            for (int j = 0; j < i; j++) {
                sb.append(String.format(
                        String.format("%%-%ds", widthColumn),
                        Application.table[i][j]? " X" : "")
                );
            }
            sb.append("\n");
        }
        iterator = Application.mapStates.keySet().iterator();
        sb.append(String.format(String.format("%%-%ds ", widthColumn), ""));
        while (iterator.hasNext())
            sb.append(String.format(String.format("%%-%ds", widthColumn), iterator.next()));
        if (Application.mapStates.size() > 1) sb.delete(sb.length() - widthColumn, sb.length());
        sb.append("\n");

        System.out.println("TABLE:");
        System.out.println(sb);
    }
}
