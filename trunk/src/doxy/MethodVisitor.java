/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doxy;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 *
 * @author Rendra
 */
public class MethodVisitor extends VoidVisitorAdapter {
    @Override
    public void visit(MethodDeclaration n, Object arg) {
        // extract method information here.
        // put in to hashmap
        System.out.println(n.getModifiers());
    }
}