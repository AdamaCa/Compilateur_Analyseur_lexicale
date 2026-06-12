package Compilateur_Analyse_Lexicale;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;



public class LireRegle {

    private Set<Character> alphabet = new HashSet<>();
    
    public List<Regle> lireFichier(String file) {
        List<Regle> regles = new ArrayList<>();
        Path chemin = Path.of(file);
        int i = 0 ;

        try {
            List<String> lignes = Files.readAllLines(chemin);

            for (String ligne : lignes) {
                Regle r = traiterLigne(ligne, i);
                if (r != null) {
                    regles.add(r);
                }
                i++;
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture du fichier : " + e.getMessage() );

        }

        return regles;
    }


    private Regle traiterLigne(String ligne, int priorite) {

        ligne = ligne.trim(); //Enlever les espaces 

        if (ligne.isEmpty() || ligne.startsWith("#")) {return null;}

        String[] parties = ligne.split(":", 2);

        if (parties.length == 2) {
            String tokenType = parties[0].trim();
            System.out.println("TokenType : " + tokenType);
            String regex = parties[1].trim();
            extraireAlphabet(regex);
            return new Regle(tokenType, regex, priorite);
        } else {
            System.err.println("Erreur de syntaxe à la ligne");
        }

        return null;
    }


    public  void extraireAlphabet(String regex) {
        for (int i = 0; i < regex.length(); i++) {
            char ch = regex.charAt(i);
            this.alphabet.add(ch);
        }
    }

    public Set<Character> getAlphabet() {return this.alphabet;}
}