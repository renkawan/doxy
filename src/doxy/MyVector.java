/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doxy;

import java.util.Vector;

/**
 *
 * @author Rendra
 */
public class MyVector extends Vector{
 
    private String nama;
 
    public MyVector(String nama) {
        this.nama = nama;
    }
 
    @Override
    public synchronized String toString() {
        return nama;
    }
}
