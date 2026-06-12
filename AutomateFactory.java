package Compilateur_Analyse_Lexicale;


public class AutomateFactory {

    public Automate creerAutomateBasique(char c) {
        Etat etat_initial = new Etat();
        Etat etat_final = new Etat();

        Automate a = new Automate(etat_initial, etat_final);
        etat_initial.ajouter_transitions(c, etat_final);
        return a;
    }

    // Etoile de Kleene A*
    public static Automate creerEtoileKleene(Automate A)  {
        Etat nv_initial = new Etat();
        Etat nv_final = new Etat();

        A.getEtatAcceptant().setAcceptant(false);

        nv_initial.ajouter_epsilon_transition(A.getEtatInitial());
        nv_initial.ajouter_epsilon_transition(nv_final);


        A.getEtatAcceptant().ajouter_epsilon_transition(nv_final);
        A.getEtatAcceptant().ajouter_epsilon_transition(A.getEtatInitial());        

        return new Automate(nv_initial, nv_final);
    }


    // Concanetation A.B
    public static Automate creerConcanetation(Automate A, Automate B) {

        A.getEtatAcceptant().setAcceptant(false);

        A.getEtatAcceptant().ajouter_epsilon_transition(B.getEtatInitial());

        return new Automate(A.getEtatInitial(), B.getEtatAcceptant());

    }

    //Union A + B
    public static Automate creerUnionAutomate(Automate A , Automate B) {

        Etat nv_initial = new Etat();
        Etat nv_final = new Etat();

        A.getEtatAcceptant().setAcceptant(false);
        A.getEtatAcceptant().ajouter_epsilon_transition(nv_final);

        B.getEtatAcceptant().setAcceptant(false);
        B.getEtatAcceptant().ajouter_epsilon_transition(nv_final);

        nv_initial.ajouter_epsilon_transition(A.getEtatInitial());
        nv_initial.ajouter_epsilon_transition(B.getEtatInitial());

        return new Automate(nv_initial, nv_final);

    }

}