package lastterm.exe01.minimization;

public class Application {
    public Automata automata;

    public Application() {
        this.automata = createAutomataFromFileData();
        Service.writeFile(automata.toString());
        automata.minimize();
    }


    public Automata createAutomataFromFileData() {
        String[] lines = Service.readFile();
        int numberTransitions;
        Automata automata = new Automata();
        try {
            int indexLine = 0;
            // Read and create transitions
            numberTransitions = lines.length == 0? 0 : Integer.parseInt(lines[indexLine++]);
            for (int i = 0; i < numberTransitions; i++) {
                String[] tokens = lines[indexLine++].split(" ");
                State currentState = new State(tokens[0]);
                String[] characters = tokens[1].split(",");
                State targetState = new State(tokens[2]);
                automata.addState(currentState);
                automata.addState(targetState);
                for (String character: characters) {
                    automata.addCharacter(character);
                    automata.addTransition(currentState, character, targetState);
                }
            }

            // Read and create initial state
            State initialState = new State(lines[indexLine++]);
            automata.setInitialState(initialState);
            automata.addState(initialState);

            // Read and create finish states
            String[] tokens = lines[indexLine].split(" ");
            for (String token: tokens) {
                State state = new State(token);
                automata.addFinishState(state);
                automata.addState(state);
            }
        } catch (Exception exception) {
            System.out.println("Error when create automata from file.");
        }

        return automata;
    }
}
