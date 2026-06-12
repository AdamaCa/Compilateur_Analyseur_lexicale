package Compilateur_Analyse_Lexicale;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;



public class LireRegle {
    public List<Regle> lireFichier(String file) {
        List<Regle> regles = new ArrayList<>();
        Path chemin = Path.of(file);

        try {
            List<String> lignes = Files.readAllLines(chemin);

            for (String ligne : lignes) {
                Regle r = traiterLigne(ligne);
                if (r != null) {
                    regles.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture du fichier : " + e.getMessage() );

        }

        return regles;
    }


    private Regle traiterLigne(String ligne) {

        ligne = ligne.trim(); //Enlever les espaces 

        if (ligne.isEmpty() || ligne.startsWith("#")) {return null;}

        String[] parties = ligne.split(":", 2);

        if (parties.length == 2) {
            String TokenType = parties[0].trim();
            String regex = parties[1].trim();
            return new Regle(TokenType, regex);
        } else {
            System.err.println("Erreru de syntaxe  a la ligne");
        }

        return null;
    }
}