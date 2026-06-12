package Compilateur_Analyse_Lexicale;

import java.util.Stack;

public class ConstructeurAutomate {


    public Automate postfixToAutomate(String postfix) {
        Stack<Automate> pile = new Stack<>();

        for (char c : postfix.toCharArray()) {

            if (isLiteral(c)) {
                Automate u = AutomateFactory.creerAutomateBasique(c);
                pile.push(u);
            } else if (c == '*') {
                Automate u = pile.pop();
                u = AutomateFactory.creerEtoileKleene(u);
                pile.push(u);
            }else if (c == '|') {
                Automate droite = pile.pop();
                Automate gauche = pile.pop();
                Automate u = AutomateFactory.creerUnionAutomate(gauche, droite);
                pile.push(u);
            } else if (c == '.') {
                Automate droite = pile.pop();
                Automate gauche = pile.pop();
                Automate u = AutomateFactory.creerConcanetation(gauche, droite);
                pile.push(u);
            }
        }
    

    return pile.pop();

    }



private boolean isLiteral(char c) {
        return c != '|' && c != '*' && c != '.';
    }
}