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
    /**
     * Use for translate comment into Indonesian from English
     * 
     * @param comments
     * @return
     * @throws IOException
     */
    public static String getServerResponse(String comments, String accessFile) throws IOException {
        String extStm = ".php";
        String result = "";
        String server = DoxyApp.myServer;
        
        URL url = new URL(server+accessFile+extStm);
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
    
    /**
     * Open URL in browser
     * Support on Windows, Linux and Mac Operating System
     * @param url 
     */
    public static void openURL(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
 
        try {
            if (os.indexOf( "win" ) >= 0) {
                rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.indexOf( "mac" ) >= 0) {
                rt.exec( "open " + url);
            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape","opera","links","lynx"};
                StringBuilder cmd = new StringBuilder();
	        for (int i=0; i<browsers.length; i++)
                    cmd.append(i==0  ? "" : " || ").append(browsers[i]).append(" \"").append(url).append( "\" ");
 
	        rt.exec(new String[] { "sh", "-c", cmd.toString() });
           }
       }catch (Exception e){ }
    }
}
