/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package global;

import java.util.Vector;

/**
 *
 * @author Rendra
 */
public class MyVector extends Vector{
 
    private String nama;
 
    /**
     *
     * @param nama
     */
    public MyVector(String nama) {
        this.nama = nama;
    }
 
    @Override
    public synchronized String toString() {
        return nama;
    }
}
