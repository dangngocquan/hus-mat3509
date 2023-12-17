package nfa_to_dfa;

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
                "6\n" +
                "S0 a S0\n" +
                "S0 a S1\n" +
                "S1 b S1\n" +
                "S1 c S2\n" +
                "S2 c S2\n" +
                "S2 c S3\n" +
                "S0\n" +
                "1\n" +
                "S3\n\n" +
                "YOUR INPUT:";
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


    public static void printTable() {
        StringBuilder sb = new StringBuilder();
        int widthColumn = 0;
        for (String state : Application.mapStates.keySet())
            widthColumn = Math.max(widthColumn, state.length());
        for (String character : Application.mapCharacters.keySet())
            widthColumn = Math.max(widthColumn, character.length());
        widthColumn += 2;

        // Print header
        sb.append(String.format(
                String.format("%%-%ds", widthColumn),
                "δ"
        ));
        for (String character : Application.mapCharacters.keySet())
            sb.append(String.format(
                    String.format("%%-%ds", widthColumn),
                    " " + character
            ));
        sb.append("\n");

        // Print rows
        Iterator<String> iterator = Application.mapStates.keySet().iterator();
        for (int i = 0; i < Application.table.size(); i++) {
            sb.append(String.format(
                    String.format("%%-%ds", widthColumn),
                    iterator.hasNext()? iterator.next() : " -"
            ));
            for (Model.State state : Application.table.get(i)) {
                sb.append(String.format(
                        String.format("%%-%ds", widthColumn),
                        state == null? " -" : state
                ));
            }
            sb.append("\n");
        }

        System.out.println("\nTABLE:");
        System.out.println(sb);
    }
}
