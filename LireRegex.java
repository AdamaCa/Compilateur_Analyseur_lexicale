package Compilateur_Analyse_Lexicale;

import java.util.Stack;

public class LireRegex {



    private static int getPrecedence(char c) {
        switch (c) {
            case '*': return 3;
            case '.': return 2;
            case '|': return 1;
            default: return 0;
        }
    }

    public static String formattageEnConcat(String regex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            result.append(c1);

            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);


                boolean C1EstLegal = isLiteral(c1) || c1 == '*'  || c1 == ')';
                boolean C2EstLegal = isLiteral(c2) || c2 == '(';

                if (C1EstLegal && C2EstLegal) {
                    result.append('.');
                }
            }
        }

        return result.toString();
    }


    private static boolean isLiteral(char c) {
            return c != '|' && c != '*' && c != '(' && c != ')' && c != '.';
        }

    public static String toPostfix(String regex) {
        StringBuilder regexPostfix = new StringBuilder();
        Stack<Character> pile = new Stack<>();



        for (char c : regex.toCharArray()) {


            if (isLiteral(c)) {
                regexPostfix.append(c);
            } else if (c == '(') {
                pile.push(c);
            }else if (c == ')') {
                while(!pile.isEmpty() && pile.peek() != '(') {
                    regexPostfix.append(pile.pop());
                }
                pile.pop();
            } else {
                if (!pile.isEmpty() && getPrecedence(c) < getPrecedence(pile.peek())) {
                    regexPostfix.append(pile.pop());
                }
                pile.push(c);
            }
        }
        while (!pile.isEmpty()) {
            regexPostfix.append(pile.pop());
        }     
        return regexPostfix.toString();
    }
}  