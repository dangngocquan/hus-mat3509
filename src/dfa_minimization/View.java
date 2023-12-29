package dfa_minimization;

import dfa_minimization.models.Automata;
import dfa_minimization.models.StateTransition;

import java.util.Iterator;

public class View {
    public static void printInputForm() {
        String contents = """
                INPUT FORMAT:
                The first line contains a number n - the number of state transition function.
                In next n lines, each line defines a State Transition Function, contains 3 words, separate by whitespace:\s
                \t+ The first word is the Current State of function
                \t+ The second word is the Character Input of function
                \t+ The third word is the Target State of function
                Nest line contains a word - the initial state.
                Next line contains a number m - the number of finish states.
                In next m lines, each line contains a word - the state.
                Example for valid input form:
                16
                A 0 B
                A 1 F
                B 0 G
                B 1 C
                C 0 A
                C 1 C
                D 0 C
                D 1 G
                E 0 H
                E 1 F
                F 0 C
                F 1 G
                G 0 G
                G 1 E
                H 0 G
                H 1 C
                A
                1
                C

                YOUR INPUT:
                """;
        System.out.println(contents);
    }

    public static void printDataOfAutomata(Automata automata) {
        StringBuilder sb = new StringBuilder();
        sb.append("CURRENT AUTOMATA:").append("\n");
        sb.append("\t").append("- States: ").append(automata.mapStates.keySet()).append("\n");
        sb.append("\t").append("- Characters: ").append(automata.mapCharacters.keySet()).append("\n");
        sb.append("\t").append("- Initial state: ").append(automata.initialState).append("\n");
        sb.append("\t").append("- Finish states: ").append(automata.finishStates).append("\n");
        sb.append("\t").append("- State Transition Functions: ").append("\n");
        int i = 1;
        for (StateTransition function : automata.stateTransitions) {
            sb.append("\t\t").append(i++).append(". ").append(function).append("\n");
        }
        System.out.println(sb);
    }

    public static void printAfterRemoveUnreachableStates(Automata automata) {
        System.out.println("STEP 1: Remove unreachable states");
        System.out.println("UNREACHABLE STATES: " + automata.unreachableStates);
        printDataOfAutomata(automata);
    }

    public static void printAfterMergeStates(Automata automata) {
        System.out.println("STEP 3: Update Automata");
        printDataOfAutomata(automata);
    }

    public static void printTable(Automata automata) {
        StringBuilder sb = new StringBuilder();
        int widthColumn = 0;
        for (String state : automata.mapStates.keySet()) widthColumn = Math.max(widthColumn, state.length());
        widthColumn += 2;
        Iterator<String> iterator = automata.mapStates.keySet().iterator();
        iterator.next();
        for (int i = 1; i < automata.tableMinimization.length; i++) {
            sb.append(String.format(String.format("%%-%ds ", widthColumn), iterator.next()));
            for (int j = 0; j < i; j++) {
                sb.append(String.format(
                        String.format("%%-%ds", widthColumn),
                        automata.tableMinimization[i][j]? " X" : "")
                );
            }
            sb.append("\n");
        }
        iterator = automata.mapStates.keySet().iterator();
        sb.append(String.format(String.format("%%-%ds ", widthColumn), ""));
        while (iterator.hasNext())
            sb.append(String.format(String.format("%%-%ds", widthColumn), iterator.next()));
        if (automata.mapStates.size() > 1) sb.delete(sb.length() - widthColumn, sb.length());
        sb.append("\n");

        System.out.println("TABLE:");
        System.out.println(sb);
    }
}
