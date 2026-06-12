package Compilateur_Analyse_Lexicale;



public class Lexer {

    private final String sourceCode;
    private final Etat initial;

    private int curseur = 0;

    public Lexer(String sourceCode, Etat initial) {
        this.sourceCode = sourceCode;
        this.initial = initial;
    }


    public Token nextToken() {
        ignorerEspaces();

        if (curseur >= sourceCode.length()) {
            return new Token("EOF", "");
        }        

        Etat courant = this.initial;

        Etat dernierEtatAcceptant = null;
        int posDernierEtatAcceptant = 0;
        int indice = curseur;

        while(indice < sourceCode.length()) {
            char c = sourceCode.charAt(indice);
            var transitionsPossibles = courant.getTransitions().get(c);
        if (transitionsPossibles == null || transitionsPossibles.isEmpty()) {
            break; 
        }

        courant = transitionsPossibles.getFirst();
        if (courant.estAcceptant()) {
            if (posDernierEtatAcceptant < indice){
                dernierEtatAcceptant = courant;
                posDernierEtatAcceptant = indice + 1;
            }   
        }
        indice++;
        }

        if (dernierEtatAcceptant == null) {
            char caractereFautif = sourceCode.charAt(curseur);
            throw new RuntimeException("Erreur Lexicale : Caractère inattendu '" + caractereFautif + "' à l'index " + curseur);
        
        } else {
            String lexeme = sourceCode.substring(curseur, posDernierEtatAcceptant);

            curseur = posDernierEtatAcceptant;

            return new Token(dernierEtatAcceptant.getTokenType(), lexeme);
        }

}


private void ignorerEspaces() {
        while (curseur < sourceCode.length()) {
            char c = sourceCode.charAt(curseur);
            // Si c'est un espace, une tabulation ou un retour à la ligne, on avance
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                curseur++;
            } else {
                break;
            }
        }
    }
}