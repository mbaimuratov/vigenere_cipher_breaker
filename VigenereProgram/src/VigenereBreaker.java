import java.util.*;
import edu.duke.*;
import java.io.*;
public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sb = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
            sb.append(message.charAt(i));
        }
        return sb.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker ccr = new CaesarCracker(mostCommon);
        for (int i = 0; i < klength; i++) {
            int num = ccr.getKey(sliceString(encrypted, i, klength));
            key[i] = num;
        }
        return key;
    }

    public HashSet<String> readDictionary(FileResource fr) {
        HashSet<String> dictionary = new HashSet<>();
        for (String line : fr.lines()) {
            if (!dictionary.contains(line.toLowerCase())) {
                dictionary.add(line.toLowerCase());
            }
        }
        return dictionary;
    }

    public int countWords(String message, HashSet<String> dictionary) {
        String[] dictionarySplit = message.split("\\W+");
        int countRealWords = 0;
        for (String word : dictionarySplit) {
            if (dictionary.contains(word.toLowerCase())) {
                countRealWords++;
            }
        }
        return countRealWords;
    }

    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        int largestCountOfReadWords = 0;
        String realDecrypt = "";
        for (int i = 1; i <= 500; i++) {
            int key[] = tryKeyLength(encrypted, i, mostCommonCharIn(dictionary));
            VigenereCipher vc = new VigenereCipher(key);
            String dec = vc.decrypt(encrypted);
            int countRealWords = countWords(dec, dictionary);
            if (countRealWords > largestCountOfReadWords) {
                largestCountOfReadWords = countRealWords;
                realDecrypt = dec;
            }
        }
        return realDecrypt;
    }

    public char mostCommonCharIn(HashSet<String> dictionary) {
        HashMap<Character, Integer> counts = new HashMap<Character, Integer>();
        for (String word : dictionary) {
            for(int k=0; k < word.length(); k++) {
                if (!counts.containsKey(word.charAt(k))) {
                    counts.put(word.charAt(k), 1);
                } else {
                    counts.put(word.charAt(k), counts.get(word.charAt(k)) + 1);
                }
            }
        }
        int max = 0;
        char out = ' ';
        for (char ch : counts.keySet()) {
            if (counts.get(ch) > max) {
                max = counts.get(ch);
                out = ch;
            }
        }
        return out;
    }

    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages) {
        int realWords = 0;
        String finalDec = "";
        String finalLanguage = "";
        for(String lang : languages.keySet()) {
            String possbileDec = breakForLanguage(encrypted, languages.get(lang));
            int countOfRealWords = countWords(possbileDec, languages.get(lang));
            if (countOfRealWords > realWords) {
                realWords = countOfRealWords;
                finalDec = possbileDec;
                finalLanguage = lang;
            }
        }
        System.out.println("Final language: " + finalLanguage);
        System.out.println(finalDec);
    }
    
    public void breakVigenere() {
        FileResource frm = new FileResource();
        String message = frm.asString();
        HashMap<String, HashSet<String>> library = new HashMap<>();
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource frd = new FileResource(f);
            HashSet<String> dict = readDictionary(frd);
            library.put(f.getName(), dict);
        }
        breakForAllLangs(message, library);
    }
}