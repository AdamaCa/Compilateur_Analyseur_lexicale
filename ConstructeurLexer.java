
package Compilateur_Analyse_Lexicale;

import java.util.List;

public class ConstructeurLexer {

    public Automate construireLexerAutomate(List<Regle> regles) {
        Etat initial = new Etat();
        Automate lexerAutomate = new Automate(initial, null);

        for (Regle regle : regles) {
            Automate a = regle.getAutomate();
            initial.ajouterEpsilonTransition(a.getEtatInitial());
        }

        return lexerAutomate;
    }

    public Etat construireDepuisFichier(String file) {
        LireRegle lireRegle = new LireRegle();
        List<Regle> regles = lireRegle.lireFichier(file);

        for (Regle regle : regles) {
            String regexConcat = LireRegex.formattageEnConcat(regle.regex);
            String postfix = LireRegex.toPostfix(regexConcat);
            ConstructeurAutomate builderAutomate = new ConstructeurAutomate();
            Automate a = builderAutomate.postfixToAutomate(postfix);
            a.getEtatAcceptant().setTokenType(regle.tokenType);
            a.getEtatAcceptant().setPriority(regle.priorite);
            regle.setAutomate(a);
        }

        Automate nfaGlobal = construireLexerAutomate(regles);
        Automate dfa = CompresseurDFA.convertirVersDFA(nfaGlobal, lireRegle.getAlphabet());
        
        return dfa.getEtatInitial();
    }

}