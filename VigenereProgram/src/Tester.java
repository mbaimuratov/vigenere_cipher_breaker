
/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;

public class Tester {
    public void testVigenereCipher() {
        int[] key = {17, 14, 12, 4};
        VigenereCipher vc = new VigenereCipher(key);
        FileResource fr = new FileResource();
        String message = fr.asString();
        String enc = vc.encrypt(message);
        System.out.println("Encrypt:");
        System.out.println(enc);
        
        String dec = vc.decrypt(enc);
        System.out.println("Decrypt:");
        System.out.println(dec);
    }
    
    public void testSliceString() {
        VigenereBreaker vb = new VigenereBreaker();
        String enc = vb.sliceString("abcdefghijklm", 4, 5);
        System.out.println(enc);
    }
    
    public void testTryKeyLength() {
        FileResource fr = new FileResource();
        String message = fr.asString();
        VigenereBreaker vb = new VigenereBreaker();
        int[] key = vb.tryKeyLength(message, 4, 'e');
        for (int i = 0; i < 4; i++) {
            System.out.print(key[i] + " ");
        }
    }
    public void testBreakVigenere() {
        VigenereBreaker vb = new VigenereBreaker();
        vb.breakVigenere();
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        //First select one encrypted test file
        //Then choose several dictionaries from "dictionary" folder
        t.testBreakVigenere();
    }
}