package com.example.demo;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class PasswordGenerator {
    
    private static final char[] LOWERCASE_ARRAY = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] UPPERCASE_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] NUMBER_ARRAY = "0123456789".toCharArray();
    private static final char[] SPECIAL_CHAR_ARRAY = "~!@#$%^&*()_+".toCharArray();
    
    public static void main(String[] args) {
        //String test = "Hhr37q8GZU4ju43ffhrGDFG&§";
        String test = generateSuperPassword(15);
        int length = test.length();

        HashMap<Character, Integer> freq = findFrequency(test);
        HashMap<Character, Double> probs = computeProbabilities(freq, length);
        HashMap<Character, Double> logarithms = computeLogarithms(probs);
        HashMap<Character, Double> pTimesLogP = computePTimesLogP(logarithms, probs);
        Double entropy = computeEntropy(pTimesLogP);
        Double theoreticalEntropy = findTheoreticalEntropy(sumClassSizes(LOWERCASE_ARRAY, UPPERCASE_ARRAY, NUMBER_ARRAY, SPECIAL_CHAR_ARRAY), length);

        //actual entropy in H=−∑pi​⋅log2​(pi​)
        System.out.println("Password: " + test + "\n");
        System.out.println("Frequencies: " + freq);
        System.out.println("Probabilities: " + probs);
        System.out.println("Logarithms: " + logarithms);
        System.out.println("p * log2(p): "+ pTimesLogP + "\n");
        System.out.println("Entropy: " + entropy + "\n");

        //theoretical search space entropy in E=L×log2​(N)
        System.out.println("Character set size: " + sumClassSizes(LOWERCASE_ARRAY, UPPERCASE_ARRAY, NUMBER_ARRAY, SPECIAL_CHAR_ARRAY) + "\n");
        System.out.println("Bits of Entropy | Brute-Force Resistance\n" + 
            "< 40   |  Very weak (seconds)\n" +
            "40–60  |  Weak (minutes–hours)\n" +
            "60–80  |  Moderate–good (days–months)\n" +
            "80–100 |  Strong (years)\n" +
            "> 100  |  Very strong (centuries+)\n"
        );
        System.out.println("Potential password strength: "+ theoreticalEntropy);
    }

    public static Map<String, Object> generatePasswordDetails(int length) {
        String test = generateSuperPassword(length);
        int passwordLength = test.length();
    
        HashMap<Character, Integer> freq = findFrequency(test);
        HashMap<Character, Double> probs = computeProbabilities(freq, passwordLength);
        HashMap<Character, Double> logarithms = computeLogarithms(probs);
        HashMap<Character, Double> pTimesLogP = computePTimesLogP(logarithms, probs);
        Double entropy = computeEntropy(pTimesLogP);
        Double theoreticalEntropy = findTheoreticalEntropy(
            sumClassSizes(LOWERCASE_ARRAY, UPPERCASE_ARRAY, NUMBER_ARRAY, SPECIAL_CHAR_ARRAY), 
            passwordLength
        );
    
        Map<String, Object> result = new HashMap<>();
        result.put("password", test);
        result.put("frequencies", freq);
        result.put("probabilities", probs);
        result.put("logarithms", logarithms);
        result.put("pTimesLogP", pTimesLogP);
        result.put("entropy", entropy);
        result.put("theoreticalEntropy", theoreticalEntropy);
        result.put("characterSetSize", sumClassSizes(LOWERCASE_ARRAY, UPPERCASE_ARRAY, NUMBER_ARRAY, SPECIAL_CHAR_ARRAY));
    
        return result;
    }

    public static String generatePassword(int length) {
        String password = "";
        for (int i = 1; i <= length; i++) {
            password = password + randomLetter();
        }
        return password;
    }

    public static char randomLetter() {
        //use printable ASCII letters between 33 and 126
        char letter = (char) ((Math.random() * 93) + 33);
        return letter;
    }

    public static char lowercaseRandomLetter() {
        //char[] lowercaseArray = "abcedfghijklmnopqrstuvwxyz".toCharArray();
        int randomIndex = new Random().nextInt(LOWERCASE_ARRAY.length);
        //char lowercaseLetter = lowercaseArray[randomIndex];
        return LOWERCASE_ARRAY[randomIndex];
    }
    
    public static char uppercaseRandomLetter() {
        //char[] uppercaseArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int randomIndex = new Random().nextInt(UPPERCASE_ARRAY.length);
        //char uppercaseLetter = uppercaseArray[randomIndex];
        return UPPERCASE_ARRAY[randomIndex];
    }

    public static char numberRandomLetter() {
        //char[] numberArray = "0123456789".toCharArray();
        int randomIndex = new Random().nextInt(NUMBER_ARRAY.length);
        //char number = numberArray[randomIndex];
        return NUMBER_ARRAY[randomIndex];
    }

    public static char specialRandomLetter() {
        //char[] specialcharArray = "~!@#$%^&*()_+".toCharArray();
        int randomIndex = new Random().nextInt(SPECIAL_CHAR_ARRAY.length);
        //char special = specialcharArray[randomIndex];
        return SPECIAL_CHAR_ARRAY[randomIndex];
    }

    public static String generateSuperPassword(int length) {
        String password = "";

        password = password + lowercaseRandomLetter();
        password = password + uppercaseRandomLetter();
        password = password + numberRandomLetter();
        password = password + specialRandomLetter();
        
        for (int i = 5; i <= length; i++) {
            password = password + randomLetter();
        }

        // convert password string into list, then shuffle
        List<Character> charList = new ArrayList<>();
        for (char c : password.toCharArray()) {
            charList.add(c);
        }
        Collections.shuffle(charList);

        // convert List Character to string
        StringBuilder shuffledPassword = new StringBuilder();
        for (char c : charList) {
            shuffledPassword.append(c);
        } 
        String passwordResult = shuffledPassword.toString();

        return passwordResult;
    }

    public static HashMap<Character, Integer> findFrequency(String password) {
        HashMap<Character, Integer> charCountMap = new HashMap<>();

        for (char c : password.toCharArray()) {
            if (charCountMap.containsKey(c)) {
                charCountMap.put(c, charCountMap.get(c) + 1);
            } else {
                charCountMap.put(c, 1);
            }
        }
        return charCountMap;
    }

    public static HashMap<Character, Double> computeProbabilities(HashMap<Character, Integer> findFrequency, int length) {
        HashMap<Character, Double> charProbability = new HashMap<>();

        for (Map.Entry<Character, Integer> entry : findFrequency.entrySet()) {
            charProbability.put(entry.getKey(), entry.getValue() / (double) length);
        }
        
        return charProbability;
    }

    public static HashMap<Character, Double> computeLogarithms(HashMap<Character, Double> computeProbabilities) {
        HashMap<Character, Double> charLogarithms = new HashMap<>();
        
        for(Map.Entry<Character, Double> entry : computeProbabilities.entrySet()) {
            charLogarithms.put(entry.getKey(), (Math.log(entry.getValue())) / Math.log(2));
        }

        return charLogarithms;
    }

    public static HashMap<Character, Double> computePTimesLogP(HashMap<Character, Double> computeLogarithms, HashMap<Character, Double> computeProbabilities) {
        HashMap<Character, Double> charPMultiplications = new HashMap<>();

        for (Map.Entry<Character, Double> entry : computeLogarithms.entrySet()) {
            charPMultiplications.put(entry.getKey(), entry.getValue() * computeProbabilities.get(entry.getKey()));
        }

        return charPMultiplications;
    }

    public static Double computeEntropy(HashMap<Character, Double> computePTimesLogP) {
        Double sum = 0.0;
        for (Map.Entry<Character, Double> entry: computePTimesLogP.entrySet()) {
            sum += entry.getValue();
        }
        Double entropy = 0.0;
        entropy = -(sum);
        return entropy;
    }

    public static int sumClassSizes(char[] LOWERCASE_ARRAY, char[] UPPERCASE_ARRAY, char[] NUMBER_ARRAY, char[] SPECIAL_CHAR_ARRAY) {
        int sum = 0;
        sum = LOWERCASE_ARRAY.length + UPPERCASE_ARRAY.length + NUMBER_ARRAY.length + SPECIAL_CHAR_ARRAY.length;
        return sum;
    }

    public static Double findTheoreticalEntropy(int sumClassSizes, int length) {
        //theoretical search space entropy in E=L×log2​(N)
        Double theoreticalEntropy = length * (Math.log(sumClassSizes) / Math.log(2));
        return theoreticalEntropy;
    }
}

