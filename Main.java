package Compilateur_Analyse_Lexicale;


public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("<Usage> : java Compilateur_Analyse_Lexicale.Main regles.lex");
            return;
        }

        ConstructeurLexer lexer = new ConstructeurLexer();
        Etat dfaInitial  = lexer.construireDepuisFichier(args[0]);
        GenererLexer generateur = new GenererLexer();
        generateur.genererLexer(dfaInitial, "MonLexer", "Compilateur_Analyse_Lexicale");
    }
}