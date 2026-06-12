
package Compilateur_Analyse_Lexicale


public class Regle {
    public final String TokenType;
    public final String regex;


    public Regle(String T, String r) {
        this.TokenType = T;
        this.regex = r;
    }
}