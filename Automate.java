/**
 * Une implementation des automates dans l'optique de l'utilisation de l'algorithme de THOMSON 
 *   -on a un seul etat final et un seul etat initial
 */

package Compilateur_Analyse_Lexicale;


public class Automate {
    private final Etat initial;
    private final Etat acceptant;

    public Automate(Etat initial, Etat acceptant) {
        this.initial = initial;
        this.acceptant = acceptant;
        // Par défaut, la fin d'un sous-automate est marquée comme acceptante
        this.acceptant.setAcceptant(true);
    }

    public Etat getEtatInitial() { return initial; }
    public Etat getEtatAcceptant() { return acceptant; }
}