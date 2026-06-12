package Compilateur_Analyse_Lexicale ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import Compilateur_Analyse_Lexicale.Etat;


public class CompresseurDFA {

    public static Set<Etat> clotureEpsilon(Set<Etat> etats) {
        Set<Etat> clotureEtats = new HashSet<>(etats);

        Stack<Etat> pile = new Stack<>();
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



    public static Automate convertirVersDFA(Automate A,  Set<Character> alphabet) {

        Set<Etat> clotureInitial = clotureEpsilon(A.getEtatInitial());

        Etat etatInitialDfa = new Etat();

        Map<Set<Etat> , Etat> dicoDeterminisation = new HashMap<>();
        dicoDeterminisation.put(clotureInitial, etatInitialDfa);

        Stack<Set<Etat>> unvisited = new Stack<>();
        unvisited.push(clotureInitial);


        while(!unvisited.isEmpty()) {

            Set<Etat> groupeEtatCourant = unvisited.pop();
            Etat dfaEtatCourant = dicoDeterminisation.get(groupeEtatCourant);


            String tokenPrioritaire = null;
            int priorite = Integer.MAX_VALUE ;
            for (Etat e : groupeEtatCourant) {
                if (e.estAcceptant()) {
                    dfaEtatCourant.setAcceptant(true);
                    if ( e.getPriority() <= priorite) {
                    tokenPrioritaire = e.getTokenType();
                    priorite = e.getPriority();
                }
                }

               
            }

            dfaEtatCourant.setPriority(priorite);
            dfaEtatCourant.setTokenType(tokenPrioritaire);


            for (char c : alphabet) {
                Set<Etat> etatAccessible = move(groupeEtatCourant, c);
                Set<Etat> clotureAccessible = clotureEpsilon(etatAccessible);

                if (!dicoDeterminisation.containsKey(clotureAccessible)) {
                    Etat dfaNouveauEtat = new Etat();
                    dicoDeterminisation.put(clotureAccessible, dfaNouveauEtat);
                    unvisited.push(clotureAccessible);
                }

                Etat e = dicoDeterminisation.get(clotureAccessible);
                dfaEtatCourant.ajouterTransition(c, e);
            }
        }
    
    return new Automate(etatInitialDfa, null);
    }
}
