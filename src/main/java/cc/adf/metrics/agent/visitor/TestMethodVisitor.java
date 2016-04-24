/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.agent.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ASM5;

/**
 *
 * @author cc
 */
public class TestMethodVisitor extends MethodVisitor {
    private String name;

    public TestMethodVisitor(MethodVisitor cv, String name) {
        super(ASM5, cv);
        this.name = name;
        System.out.print("TestMethodVisitor " + name);
    }
    

    @Override
    public void visitCode() {
        super.visitCode(); //To change body of generated methods, choose Tools | Templates.
        System.out.print("Visit Code " + name);
    }
    
}
