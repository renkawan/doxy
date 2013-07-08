/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rendra
 */
public class Bridge {
    private List<String> listJavaSrc = new ArrayList<String>();
    public void setWorkPath(String path){
        DoxyApp.workPath = path;
    }
    public void setListJavaItem(String item){
        this.listJavaSrc.add(item);
    }
    public List<String> getListJavaSrc(){
        return listJavaSrc;
    }
}
