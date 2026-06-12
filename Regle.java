
package Compilateur_Analyse_Lexicale;


public class Regle {
    public final String tokenType;
    public final String regex;
    public final int priorite;
     
    private Automate automate;


    public Regle(String tokenType, String r, int priorite) {
        this.tokenType = tokenType;
        this.regex = r;
        this.priorite = priorite;
        this.automate = null;
    }


    public void setAutomate(Automate a ) {
        this.automate = a;
    }

    public Automate getAutomate() {return this.automate;}
}