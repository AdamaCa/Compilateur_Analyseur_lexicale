package Compilateur_Analyse_Lexicale;

import java.util.*;

public class Etat {
    private static int idCounter = 0;
    private final int id;
    private boolean estAcceptant;
    private String tokenType;
    private List<Etat> listeEpsilonTransitions = new ArrayList<>();
    private Map<Character , List<Etat>> transitions  = new HashMap<>();
    private int priority = Integer.MAX_VALUE; 
    


    public Etat() {
        this.id = idCounter++;
        this.estAcceptant = false;
        this.tokenType = null;
    }


    public void ajouterTransition(char c, Etat e) {
            this.transitions.computeIfAbsent(c, x -> new ArrayList<>()).add(e); //computeifabsent renvoie l'element crer
    }

    public void ajouterEpsilonTransition(Etat e) {
        this.listeEpsilonTransitions.add(e);
    }

    //GETTERS


    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public int getId() { return this.id;}

    public boolean estAcceptant() {return this.estAcceptant;}
    public void setAcceptant(boolean bool) {this.estAcceptant = bool;}

    public void setTokenType(String tokenType) {this.tokenType = tokenType;}
    public String getTokenType() {return this.tokenType;}

    public Map<Character , List<Etat>>  getTransitions() {return this.transitions;}
    public List<Etat> getEpsilonTransitions() {return this.listeEpsilonTransitions;}
}
