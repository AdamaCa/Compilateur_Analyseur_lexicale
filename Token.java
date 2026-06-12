package Compilateur_Analyse_Lexicale;


public class Token {

    public final String tokenType;
    public final String lexeme;

    public Token(String tokenType , String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }
}