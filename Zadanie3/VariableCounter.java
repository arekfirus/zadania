package Zadanie3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableCounter {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\aware\\IdeaProjects\\Kurs\\Test3Poprawka\\src\\Zadanie3\\Example.java";

        try {
            Map<String, Integer> variableCounts = countVariables(filePath);
            for (Map.Entry<String, Integer> entry : variableCounts.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }


    public static Map<String, Integer> countVariables(String filePath) throws IOException {
        Map<String, Integer> variableCounts = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;


        String variableDeclarationRegex = "\\b(?:int|double|float|long|short|byte|boolean|char|String)\\b\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*=.*?;";

        try {
            while ((line = reader.readLine()) != null) {
                line = removeCommentsAndStrings(line); // Usuwanie komentarzy i literałów stringowych
                if (!line.trim().isEmpty()) {
                    Pattern pattern = Pattern.compile(variableDeclarationRegex);
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String declaration = matcher.group();
                        String[] tokens = declaration.split("\\s+"); // Dzielimy deklarację na tokeny
                        String type = tokens[0]; // Pierwszy token to typ zmiennej

                        // Zliczanie wystąpień typów zmiennych
                        if (!variableCounts.containsKey(type)) {
                            variableCounts.put(type, 0);
                        }
                        variableCounts.put(type, variableCounts.get(type) + 1);
                    }
                }
            }
        } finally {
            reader.close();
        }

        return variableCounts;
    }

    private static String removeCommentsAndStrings(String line) {
        // Usuwanie jednolinijkowych komentarzy oraz literałów stringowych
        line = line.replaceAll("//.*", ""); // Usuwanie komentarzy jednolinijkowych
        line = line.replaceAll("\".*?\"", ""); // Usuwanie literałów stringowych
        return line;

    }


}


