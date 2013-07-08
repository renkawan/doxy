/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kit;

import global.DoxyApp;
import global.MyVector;
import java.io.File;
import java.io.FileFilter;

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
                DoxyApp.bridge.setListJavaItem(file.getAbsolutePath());
            }
        }
        return vectorDirectory;
    }
}
