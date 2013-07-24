/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kit;

import global.DoxyApp;
import global.MyVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Rendra
 */
public class FileKit {
    /**
     * Get list of file from selected project directory
     * @param directory
     * @return vectorDirectory
     */
    public static MyVector getFileDirectory(File directory) {
        MyVector vectorDirectory = new MyVector(directory.getName());
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".java") || (pathname.isDirectory() && !("System Volume Information".equalsIgnoreCase(name)));
            }
        });
        for (File file : files) {
            if(file.isDirectory()) {
                MyVector vector = getFileDirectory(file);
                vectorDirectory.add(vector);
            } else {
                vectorDirectory.add(file.getName());
                DoxyApp.bridge.setListSources(file.getAbsolutePath());
            }
        }
        return vectorDirectory;
    }
    
    /**
     * Read selected java source file and save into String for further operation     *
     * @param file_location
     * @return String text
     */
    public static String readSrcFile(String file_location) {
        try {
            File src = new File(file_location);
            FileInputStream fis = new FileInputStream(src);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            StringBuilder text = new StringBuilder((int)src.length());
            String line;
            while((line = buffer.readLine()) != null) {
                text.append(line).append("\n");
                DoxyApp.bridge.addLineOfCode();
            }
            return text.toString();
        }
        catch (FileNotFoundException notfound) { return notfound.toString(); }
        catch (IOException ioex) { return ioex.toString(); }
    }
    
    /**
     * Function for getting line number from the needle character position
     * in a large String.
     * 
     * The needle is a offset (start / end) position
     * which got from regex matcher operation.
     * 
     * @param offset
     * @param contents
     * @return int offset
     */
    public static int getNumbLine(int offset, String[] contents) {
        int lastLine = DoxyApp.bridge.getLastLine();
        int temp = 0;
        if (lastLine > 0) temp = offset;        
        for(int i=DoxyApp.bridge.getLastLine();i<contents.length;i++) {
            temp = temp + contents[i].length() + 1;
            if (temp>=offset) {
                DoxyApp.bridge.setLastLine(i);
                return i+1;
            }
        }
        return offset;
    }
    
    /**
     * Get file name from a full path location
     * @param full_path
     * @return String[] explode
     */
    public static String getFileName(String full_path) {
        String[] explode = full_path.replace("\\", "/").split("/");
        return explode[explode.length-1];
    }
    
    /**
     * Read text file for recent projects
     * @param aFileName
     * @return List<String>
     * @throws IOException
     */
    public static List<String> readTextFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
  
    /**
     * Write existing file with new contents
     * @param aLines
     * @param aFileName
     * @throws IOException
     */
    public static void writeTextFile(List<String> aLines, String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        Files.write(path, aLines, StandardCharsets.UTF_8);
    }
    
    /**
     * Extract comments to be read per line, then translate the comment
     * @param contents
     * @return StringBuilder bContent
     * @throws Exception 
     */
    public static StringBuilder extractAndTranslate(String [] contents) throws Exception {
        StringBuilder bContent = new StringBuilder();
        for (int i=0;i<contents.length;i++) {
            if (contents[i].trim().equals("")) {
                if (i!=(contents.length)-1)
                    bContent.append(contents[i]).append("\r\n");
                else
                    bContent.append(contents[i]);
            } else {
                if (contents[i].trim().equals("*")) {
                    bContent.append(contents[i]).append("\r\n");
                } else {
                    if (!contents[i].matches("(?i).*@param.*") && !contents[i].matches("(?i).*@return.*")
                            && !contents[i].matches("(?i).*@author.*")) {
                        String translated = ServerKit.translateComments(contents[i]);
                        bContent.append("  ").append(translated).append("\r\n");
                    } else {
                        bContent.append(contents[i]).append("\r\n");
                    }
                }
            }
        }
        return bContent;        
    }
    
    /**
     * Copy directory and file recursively
     * @param src
     * @param dest
     * @throws IOException 
     */
    public static void copyFolder(File src, File dest) throws IOException{
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
    		File destFile = new File(dest, file);
    		copyFolder(srcFile, destFile);
            }
    	}else{
            InputStream in = new FileInputStream(src);
    	    OutputStream out = new FileOutputStream(dest); 
 
            byte[] buffer = new byte[1024];
            int length;
    	    while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
 
    	    in.close();
    	    out.close();
    	}
    }
}
