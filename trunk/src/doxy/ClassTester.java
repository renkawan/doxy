/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package doxy;

/**
 *
 * @author Rendra
 */
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import java.io.FileInputStream;
import java.util.List;

/**
 *
 * @author Rendra
 */
public class ClassTester {
    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream("D:/BlurGlassPane.java");
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        
        //Pattern p_c = Pattern.compile("(?://.*)|(/\\*(?:.|[\\n\\r])*?\\*/)", Pattern.MULTILINE);
        /*Matcher m_c = p_c.matcher(cu.toString());
        while (m_c.find()) {
            System.out.println(m_c.group());
        }*/
        
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    System.out.println(member);
                }
            }
        }

        
        //MethodVisitor visitor = new MethodVisitor();
        //visitor.visit(cu, null);
  
        //System.out.println(cu.getTypes().toString());
        //Method m[] = cu.getTypes().getClass().getDeclaredMethods();
        //for(int i = 0; i < m.length; i++) {
        //    System.out.println(m[i]);
        //}
        
        /*
        List<Comment> srcComments = new ArrayList<>();
        srcComments = cu.getComments();
        for(Comment cm : srcComments){
            // Get Content = get the comments content
            // To String = print all the comment components
            System.out.println(cm.toString());
        }
        */
    }
}