/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Rendra
 */
public class Bridge {
    private List<String> listJavaSrc = new ArrayList<>();
    private MyVector listSources;
    private String selectedSrcFile;
    private int totalLineOfCode;
    private int lastLineVisited;
    private boolean isChoosen = false;
    private boolean fromRecent = false;
    private String lastPath = ".";
    
    /**
     * Set working directory which used for further analyzing
     *
     * @param path
     */
    public void setWorkPath(String path){
        DoxyApp.workPath = path;
    }
    
    /**
     * Set item list source
     *
     * @param item
     */
    
    public void setListJavaItem(String item){
        this.listJavaSrc.add(item);
    }
    
    /**
     * Retrieve all sources list
     *
     * @return
     */
    public List<String> getListJavaSrc(){
        return listJavaSrc;
    }
    
    /**
     * Set sources list
     *
     * @param path
     */
    public void setListSources(String path) {
        if(listSources==null) {
            listSources = new MyVector(path);
        } else {
            listSources.add(path);
        }
    }
    
    /**
     * Get list sources file
     *
     * @return
     */
    public MyVector getListSources() {
        return this.listSources;
    }
    
    /**
     * Set selected file for further analyzing about it
     *
     * @param file
     */
    public void setSelectedSrcFile(String file) {
        this.isChoosen = true;
        this.selectedSrcFile = file;
    }
    
    /**
     * Get selected source file
     *
     * @return
     */
    public String getSelectedSrcFile() {
        return this.selectedSrcFile;
    }
    
    /**
     * Summary total line of code
     */
    public void addLineOfCode() {
        totalLineOfCode++;
    }
    
    /**
     * Get total line of code
     *
     * @return
     */
    public int getLineOfCode() {
        return totalLineOfCode;
    }
    
    /**
     * Count total lines of current comment block
     * 
     * @param comment
     * @return 
     */
    private int countLineOfComments(String comment){
        comment = Pattern.compile("\\s*$", Pattern.MULTILINE).matcher(comment).replaceAll("");
        return comment.split("\n").length;
    }
    /**
     * Get comment per line
     *
     * @param comment
     * @return
     */
    public int getCommentsLine(String comment) {
        return countLineOfComments(comment);
    }
    
    /**
     * Set las visited line
     *
     * @param line
     */
    public void setLastLine(int line) {
        lastLineVisited = line;
    }
    
    /**
     * Get last visited line of code
     * @return int
     */
    public int getLastLine() {
        return lastLineVisited;
    }
    
    /**
     * Check if a source file has been choose
     * @return boolean
     */
    public boolean isFileChoosen() {
        return isChoosen;
    }
    
    /**
     * Set working path from recent project
     * @param recent 
     */
    public void setFromRecent(boolean recent) {
        fromRecent = recent;
    }
    
    /**
     * Check if working path is from recent project or not
     * @return boolean
     */
    public boolean isFromRecent() {
        return fromRecent;
    }
    
    /**
     * Set last path location
     * @param lastPath 
     */
    public void setLastPath(String lastPath) {
        this.lastPath = lastPath;
    }
    
    /**
     * Get last visited path
     * @return 
     */
    public String getLastPath() {
        return lastPath;
    }
}
