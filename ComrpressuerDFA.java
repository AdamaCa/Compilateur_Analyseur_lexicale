package Compilateur_Analyse_Lexicale ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import Compilateur_Analyse_Lexicale.Etat;


public class ComrpressuerDFA {

    public static Set<Etat> clotureEpsilon(Set<Etat> etats) {
        Set<Etat> clotureEtats = new HashSet<>(etats);

        Stack<Etat> pile = new Stack();
        pile.addAll(clotureEtats);

        while (!pile.isEmpty() ) {
            Etat courant = pile.pop();

            for (Etat prochain : courant.getEpsilonTransitions()) {
                
                if (!clotureEtats.contains(prochain)) {
                    clotureEtats.add(prochain);
                    pile.push(prochain);
                }
            }   
        }
        return clotureEtats;
    }


    public static Set<Etat> clotureEpsilon(Etat etat) {
        Set<Etat> initial = new HashSet<>();
        initial.add(etat);
        return clotureEpsilon(initial);
    }




    public static Set<Etat> move(Set<Etat> etats, char c) {
        Set<Etat> result = new HashSet<>();

        for (Etat e : etats) {

            List<Etat> liste = e.getTransitions().get(c);

            if (liste != null) {
                result.addAll(liste);
            }
        }
        return result;
    }



    public static Automate ConvertisseurVersDFA(Automate A,  Set<Character> alphabet) {

        Set<Etat> cloture_initial = clotureEpsilon(A.getEtatInitial());

        Etat etat_initial_dfa = new Etat();

        Map<Set<Etat> , Etat> dico_determinisation = new HashMap<>();
        dico_determinisation.put(cloture_initial, etat_initial_dfa);

        Stack<Set<Etat>> unvisited = new Stack<>();
        unvisited.push(cloture_initial);


        while(!unvisited.isEmpty()) {

            Set<Etat> groupe_etat_courant = unvisited.pop();
            Etat DfaEtatCourant = dico_determinisation.get(groupe_etat_courant);


            String TokenPrioritaire;
            int priorite = Integer.MAX_VALUE ;
            for (Etat e : groupe_etat_courant) {
                if (e.estAcceptant()) {
                    DfaEtatCourant.setAcceptant(true);
                }

                if ( e.getPriority() <= priorite) {
                    TokenPrioritaire = e.getToKenType();
                    priorite = e.getPriority();
                }
            }

            DfaEtatCourant.setPriority(priorite);
            DfaEtatCourant.setTokenType(TokenPrioritaire);


            for (char c : alphabet) {
                Set<Etat> etat_accesible = move(groupe_etat_courant, c);
                Set<Etat> cloture_acessible = clotureEpsilon(etat_accesible);

                if (!dico_determinisation.containsKey(cloture_acessible)) {
                    Etat DfaNouveauEtat = new Etat();
                    dico_determinisation.put(cloture_acessible, DfaNouveauEtat);
                    unvisited.push(cloture_acessible);
                }

                Etat e = dico_determinisation.get(cloture_acessible);
                DfaEtatCourant.ajouter_transitions(c, e);
            }
        }
    
    return new Automate(etat_initial_dfa, null);
    }
}



