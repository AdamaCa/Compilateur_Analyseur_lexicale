package Compilateur_Analyse_Lexicale;

import java.util.*;

public class Etat {
    private static int idCounter = 0;
    private final int id;
    private boolean estAcceptant;
    private String TokenType;
    private List<Etat> listeEpsilonTransitions = new ArrayList<>();
    private Map<Character , List<Etat>> transitions  = new HashMap<>();


    public Etat() {
        this.id = idCounter++;
        this.estAcceptant = false;
        this.TokenType = null;
    }


    public void ajouter_transitions(char c, Etat e) {
            this.transitions.computeIfAbsent(c, x -> new ArrayList<>()).add(e); //computeifabsent renvoie l'element crer
    }

    public void ajouter_epsilon_transition(Etat e) {
        this.listeEpsilonTransitions.add(e);
    }

    //GETTERS
    public int getId() { return this.id;}

    public boolean estAcceptant() {return this.estAcceptant;}
    public void setAcceptant(boolean bool) {this.estAcceptant = bool;}

    public void setTokenType(String token_type) {this.TokenType = token_type;}
    public String getToKenType() {return this.TokenType;}

    public Map<Character , List<Etat>>  getTransitions() {return this.transitions;}
    public List<Etat> getEpsilonTransitions() {return this.listeEpsilonTransitions;}
}
