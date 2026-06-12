

package Compilateur_Analyse_Lexicale;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GenererLexer {


private String construireCodeJava(String nomClasse, int[][] transitions, String[] jetons, int nbEtats, int nbCaracteres) {
        StringBuilder sb = new StringBuilder();

        // Package et imports
        sb.append("package Compilateur_Analyse_Lexicale;\n\n");
        
        sb.append("public class ").append(nomClasse).append(" {\n");
        sb.append("    private final String sourceCode;\n");
        sb.append("    private int curseur = 0;\n\n");

        // --- MATRICE DES TRANSITIONS (Version Morcelée / Chunked) ---
        // 1. On déclare le tableau vide
        sb.append("    private static final int[][] TRANSITIONS = new int[").append(nbEtats).append("][").append(nbCaracteres).append("];\n\n");

        // 2. On calcule le nombre de blocs nécessaires (50 états par méthode pour être très large)
        int chunkSize = 50;
        int nbChunks = (int) Math.ceil((double) nbEtats / chunkSize);

        // 3. Le bloc statique qui appelle toutes les petites méthodes
        sb.append("    static {\n");
        for (int i = 0; i < nbChunks; i++) {
            sb.append("        initBlock").append(i).append("();\n");
        }
        sb.append("    }\n\n");

        // 4. On génère les petites méthodes d'initialisation
        for (int i = 0; i < nbChunks; i++) {
            sb.append("    private static void initBlock").append(i).append("() {\n");
            
            int debut = i * chunkSize;
            int fin = Math.min(debut + chunkSize, nbEtats);
            
            for (int j = debut; j < fin; j++) {
                sb.append("        TRANSITIONS[").append(j).append("] = new int[] { ");
                for (int c = 0; c < nbCaracteres; c++) {
                    sb.append(transitions[j][c]).append(", ");
                }
                sb.append("};\n");
            }
            sb.append("    }\n\n");
        }

        // --- TABLEAU DES JETONS ACCEPTANTS ---
        // (En général, ce tableau prend beaucoup moins de place, on le laisse tel quel)
        sb.append("    private static final String[] JETONS = {\n        ");
        for (int i = 0; i < nbEtats; i++) {
            if (jetons[i] == null) sb.append("null, ");
            else sb.append("\"").append(jetons[i]).append("\", ");
        }
        sb.append("\n    };\n\n");

        // --- CONSTRUCTEUR ---
        sb.append("    public ").append(nomClasse).append("(String sourceCode) {\n");
        sb.append("        this.sourceCode = sourceCode;\n");
        sb.append("    }\n\n");

        // --- ALGORITHME DU MAXIMAL MUNCH ---
        sb.append("    public Token nextToken() {\n");
        sb.append("        ignorerEspaces();\n\n");
        
        sb.append("        if (curseur >= sourceCode.length()) {\n");
        sb.append("            return new Token(\"EOF\", \"\");\n");
        sb.append("        }\n\n");
        
        sb.append("        int etatCourant = 0;\n");
        sb.append("        String dernierJeton = null;\n");
        sb.append("        int posDernierJeton = curseur;\n");
        sb.append("        int indice = curseur;\n\n");

        sb.append("        while (indice < sourceCode.length()) {\n");
        sb.append("            char c = sourceCode.charAt(indice);\n");
        sb.append("            if (c >= 128) break;\n\n");
        
        sb.append("            etatCourant = TRANSITIONS[etatCourant][c];\n");
        sb.append("            if (etatCourant == -1) {\n");
        sb.append("                break;\n");
        sb.append("            }\n\n");
        
        sb.append("            if (JETONS[etatCourant] != null) {\n");
        sb.append("                dernierJeton = JETONS[etatCourant];\n");
        sb.append("                posDernierJeton = indice + 1;\n");
        sb.append("            }\n");
        sb.append("            indice++;\n");
        sb.append("        }\n\n");

        sb.append("        if (dernierJeton == null) {\n");
        sb.append("            char caractereFautif = sourceCode.charAt(curseur);\n");
        sb.append("            throw new RuntimeException(\"Erreur Lexicale : Caractère inattendu '\" + caractereFautif + \"' à l'index \" + curseur);\n");
        sb.append("        } else {\n");
        sb.append("            String lexeme = sourceCode.substring(curseur, posDernierJeton);\n");
        sb.append("            curseur = posDernierJeton;\n");
        sb.append("            return new Token(dernierJeton, lexeme);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // --- METHODE UTILITAIRE ---
        sb.append("    private void ignorerEspaces() {\n");
        sb.append("        while (curseur < sourceCode.length()) {\n");
        sb.append("            char c = sourceCode.charAt(curseur);\n");
        sb.append("            if (c == ' ' || c == '\\t' || c == '\\n' || c == '\\r') {\n");
        sb.append("                curseur++;\n");
        sb.append("            } else {\n");
        sb.append("                break;\n");
        sb.append("            }\n");
        sb.append("        }\n");
        sb.append("    }\n");

        sb.append("}\n");

        return sb.toString();
    }


    

    public void genererLexer(Etat dfainitial, String nomClasse, String dossierDestination ){
        List<Etat> tousLesEtats = new ArrayList<>();
        Map<Etat, Integer> dicoIndices = new HashMap<>();
        Queue<Etat> aVisiter = new LinkedList<>();


        dicoIndices.put(dfainitial, 0);
        tousLesEtats.add(dfainitial);
        aVisiter.add(dfainitial);


        while (!aVisiter.isEmpty()) {
            Etat courant = aVisiter.poll();

            for (List<Etat> cible : courant.getTransitions().values()) {

                Etat suivant = cible.get(0);

                if (!dicoIndices.containsKey(suivant)) {
                    dicoIndices.put(suivant, tousLesEtats.size());
                    tousLesEtats.add(suivant);
                    aVisiter.add(suivant);
                }
            }
        }

        int nbEtats = tousLesEtats.size();
        int nbCaracteres = 128; // ASCII

        int[][] matriceTransitions = new int[nbEtats][nbCaracteres];
        String[] tableauJetons = new String[nbEtats];


        for (int[] ligne : matriceTransitions) {
            Arrays.fill(ligne, -1);
        }

        for (int i = 0; i < nbEtats; i++) {
        Etat etat = tousLesEtats.get(i);
        tableauJetons[i] = etat.getTokenType();

        for (Map.Entry<Character, List<Etat>> transition : etat.getTransitions().entrySet()) {
                char c = transition.getKey();

                if (c < nbCaracteres) {
                    Etat suivant = transition.getValue().get(0);
                    int indiceSuivant = dicoIndices.get(suivant);
                    matriceTransitions[i][c] = indiceSuivant;
                }
            }
        }
    
    
        String codeSource = construireCodeJava(nomClasse, matriceTransitions, tableauJetons, nbEtats, nbCaracteres);
        
        String cheminComplet = dossierDestination + "/" + nomClasse + ".java";
        try (FileWriter writer = new FileWriter(cheminComplet)) {
            writer.write(codeSource);
            System.out.println("✅ SUCCÈS : Ton Lexer a été généré avec succès dans -> " + cheminComplet);
            System.out.println("📊 Statistiques : " + nbEtats + " états déterministes compilés.");
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}