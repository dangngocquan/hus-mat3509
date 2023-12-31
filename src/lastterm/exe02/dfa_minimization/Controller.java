package lastterm.exe02.dfa_minimization;

public class Controller {
    public void printInputForm() {
        View.printInputForm();
    }
    public void createAutomataFromKeyboard() {
        Application.automata = Service.createAutomataFromKeyboard();
        View.printDataOfAutomata(Application.automata);
    }

    public void minimizationAutomata() {
        Service.removeUnreachableState(Application.automata);
        Service.minimizationAndPrintTablePerStep(Application.automata);
        Service.mergeStates(Application.automata);
    }
}
