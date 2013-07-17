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
    
    public void setWorkPath(String path){
        DoxyApp.workPath = path;
    }
    public void setListJavaItem(String item){
        this.listJavaSrc.add(item);
    }
    public List<String> getListJavaSrc(){
        return listJavaSrc;
    }
    public void setListSources(String path) {
        if(listSources==null) {
            listSources = new MyVector(path);
        } else {
            listSources.add(path);
        }
    }
    public MyVector getListSources() {
        return this.listSources;
    }
    public void setSelectedSrcFile(String file) {
        this.selectedSrcFile = file;
    }
    public String getSelectedSrcFile() {
        return this.selectedSrcFile;
    }
    public void addLineOfCode() {
        totalLineOfCode++;
    }
    public int getLineOfCode() {
        return totalLineOfCode;
    }
    private int countLineOfComments(String comment){
        comment = Pattern.compile("\\s*$", Pattern.MULTILINE).matcher(comment).replaceAll("");
        return comment.split("\n").length;
    }
    public int getCommentsLine(String comment) {
        return countLineOfComments(comment);
    }
}
