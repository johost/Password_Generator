import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PasswordGenerator {
    public static void main(String[] args) {
        System.out.println(generateSuperPassword(16));

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
}