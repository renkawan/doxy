/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kit;

import global.DoxyApp;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Rendra
 */
public class ServerKit {
    public static String translateComments(String comments) throws IOException {
        String result = "";
        String server = DoxyApp.myServer;
        String translatorFile = "trans_comments.php";
        
        URL url = new URL(server+translatorFile);
        String data = "text=" + URLEncoder.encode(comments, "UTF-8");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            // Send the POST data
            DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
            dataOut.writeBytes(data);
            dataOut.flush();
            dataOut.close();

            // Read thes result from POST data
            BufferedReader in = null;
            try {
                String line;
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } finally {
            connection.disconnect();
            return result;
        }
    }
}
