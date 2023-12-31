package midterm.nfa_to_dfa;

public class Controller {
    public void printInputForm() {
        View.printInputForm();
    }
    public void createDataFromKeyboard() {
        Service.createDataFromKeyboard();
        View.printDataOfAutomata();
    }

    public void solve() {
        Service.nfaToDfa();
    }
}
