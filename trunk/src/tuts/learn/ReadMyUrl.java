/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tuts.learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Rendra
 */
public class ReadMyUrl {
    public static void main(String[]args){
        try {
            URL url = new URL("http://localhost/test.php");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String htmlText;     
                while ((htmlText = in.readLine()) != null) {
                    // Keep in mind that readLine() strips the newline characters
                    System.out.println(htmlText);
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
}
