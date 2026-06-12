package Compilateur_Analyse_Lexicale;


public class AutomateFactory {

    public static Automate creerAutomateBasique(char c) {
        Etat etatInitial = new Etat();
        Etat etatFinal = new Etat();

        Automate a = new Automate(etatInitial, etatFinal);
        etatInitial.ajouterTransition(c, etatFinal);
        return a;
    }

    // Etoile de Kleene A*
    public static Automate creerEtoileKleene(Automate A)  {
        Etat nouvelInitial = new Etat();
        Etat nouveauFinal = new Etat();

        A.getEtatAcceptant().setAcceptant(false);

        nouvelInitial.ajouterEpsilonTransition(A.getEtatInitial());
        nouvelInitial.ajouterEpsilonTransition(nouveauFinal);


        A.getEtatAcceptant().ajouterEpsilonTransition(nouveauFinal);
        A.getEtatAcceptant().ajouterEpsilonTransition(A.getEtatInitial());        

        return new Automate(nouvelInitial, nouveauFinal);
    }


    // Concanetation A.B
    public static Automate creerConcanetation(Automate A, Automate B) {

        A.getEtatAcceptant().setAcceptant(false);

        A.getEtatAcceptant().ajouterEpsilonTransition(B.getEtatInitial());

        return new Automate(A.getEtatInitial(), B.getEtatAcceptant());

    }

    //Union A + B
    public static Automate creerUnionAutomate(Automate A , Automate B) {

        Etat nouvelInitial = new Etat();
        Etat nouveauFinal = new Etat();

        A.getEtatAcceptant().setAcceptant(false);
        A.getEtatAcceptant().ajouterEpsilonTransition(nouveauFinal);

        B.getEtatAcceptant().setAcceptant(false);
        B.getEtatAcceptant().ajouterEpsilonTransition(nouveauFinal);

        nouvelInitial.ajouterEpsilonTransition(A.getEtatInitial());
        nouvelInitial.ajouterEpsilonTransition(B.getEtatInitial());

        return new Automate(nouvelInitial, nouveauFinal);

    }

}