package com.example.demo;
import java.util.Random;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class PasswordGenerator {
    public static void main(String[] args) {
        String test = "hello";
        int length = test.length();

        HashMap<Character, Integer> freq = findFrequency(test);
        HashMap<Character, Double> probs = computeProbabilities(freq, length);
        HashMap<Character, Double> logarithms = computeLogarithms(probs);
        HashMap<Character, Double> pTimesLogP = computePTimesLogP(logarithms, probs);
        Double entropy = computeEntropy(pTimesLogP);

        System.out.println("Frequencies: " + freq);
        System.out.println("Probabilities: " + probs);
        System.out.println("Logarithms: " + logarithms);
        System.out.println("p * log2(p): "+ pTimesLogP);
        System.out.println("Entropy: " + entropy);


        //H=−∑pi​⋅log2​(pi​)
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
        char[] lowercaseArray = "abcedfghijklmnopqrstuvwxyz".toCharArray();
        int randomIndex = new Random().nextInt(lowercaseArray.length);
        char lowercaseLetter = lowercaseArray[randomIndex];
        return lowercaseLetter;
    }
    
    public static char uppercaseRandomLetter() {
        char[] uppercaseArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int randomIndex = new Random().nextInt(uppercaseArray.length);
        char uppercaseLetter = uppercaseArray[randomIndex];
        return uppercaseLetter;
    }

    public static char numberRandomLetter() {
        char[] numberArray = "0123456789".toCharArray();
        int randomIndex = new Random().nextInt(numberArray.length);
        char number = numberArray[randomIndex];
        return number;
    }

    public static char specialRandomLetter() {
        char[] specialcharArray = "~!@#$%^&*()_+".toCharArray();
        int randomIndex = new Random().nextInt(specialcharArray.length);
        char special = specialcharArray[randomIndex];
        return special;
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
}