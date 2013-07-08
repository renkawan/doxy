/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tuts.learn;

import java.io.File;

/**
 *
 * @author Rendra
 */
public class FileWalker {

    public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            } else {
                if(f.getAbsoluteFile().toString().endsWith(".java"))
                    System.out.println( "File:" + f.getAbsoluteFile() );
            }
        }
    }

    public static void main(String[] args) {
        FileWalker fw = new FileWalker();
        fw.walk("D:\\College\\Semester 8\\Skripsi\\Java Source\\doxy");
    }
}
