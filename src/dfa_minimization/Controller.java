package dfa_minimization;

public class Controller {
    public void printInputForm() {
        View.printInputForm();
    }
    public void createDataFromKeyboard() {
        Service.createDataFromKeyboard();
        View.printDataOfAutomata();
    }

    public void solve() {
        Service.removeUnreachableState();
        Service.minimizationAndPrintTablePerStep();
        Service.mergeStates();
    }
}
