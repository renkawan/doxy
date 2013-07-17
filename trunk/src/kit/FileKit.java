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
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Rendra
 */
public class FileKit {
    /**
     * Get list of file from selected project directory
     * 
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
     * Read selected java source file and save into String for further operation
     *
     * @param file_location
     * @return 
     */
    public static String readSrcFile(String file_location) {
        try {
            File src = new File(file_location);
            FileInputStream fis = new FileInputStream(src);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            StringBuilder text = new StringBuilder((int)src.length());
            String line = null;
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
     * @return 
     */
    public static int getNumbLine(int offset, String[] contents) {
        int temp = 0;
        int i = 0;
        for(i=0;i<contents.length;i++) {
            temp = temp + contents[i].length() + 1;
            if (temp>=offset)
                return i+1;
        }
        return offset;
    }
    
    public static String getFileName(String full_path) {
        String[] explode = full_path.replace("\\", "/").split("/");
        return explode[explode.length-1];
    }
}
