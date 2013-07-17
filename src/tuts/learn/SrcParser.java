/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tuts.learn;

import japa.parser.JavaParser;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rendra
 */
public class SrcParser {
    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream("D:/NIO2_FileVisitor.java");
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        List<Comment> srcComments = new ArrayList<>();
        srcComments = cu.getComments();
        for(Comment cm : srcComments){
            // Get Content = get the comments content
            // To String = print all the comment components
            System.out.println(cm.toString());
        }
    }
}
