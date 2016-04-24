/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.agent.visitor;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 *
 * @author cc
 */
public class MetricsClassVisitor extends ClassVisitor{
    private String className;
    private ClassWriter cw;
    public MetricsClassVisitor(ClassWriter cw, String className) {
        super(Opcodes.ASM4, cw);
        this.cw = cw;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
        //System.out.println("Visiting Method " + name);
        MethodVisitor mv = cv.visitMethod(access, methodName, desc, signature, exceptions);
        boolean isInterface = (access & ACC_INTERFACE) != 0;
        if (!isInterface && mv != null && (methodName.equals("passivateState") || methodName.equals("create"))){
            System.out.println("Visiting Method " + methodName);
            System.out.println("Pre Enter MetricsMethodVisitor");
            MetricsMethodVisitor newMv = new MetricsMethodVisitor(mv, this.className, methodName, desc);
            System.out.println("this.name = " + this.className + ", desc = " + desc);
            //newMv.analyzerAdapter = new AnalyzerAdapter(this.name, access, name, desc, newMv);
            //newMv.localVariablesSorter = new LocalVariablesSorter(access, desc, newMv.analyzerAdapter);
            newMv.localVariablesSorter = new LocalVariablesSorter(access, desc, newMv);
            return newMv.localVariablesSorter;
            //return newMv;
        }
        
        return mv;
        //return new MetricsMethodVisitor(access, desc,
        //        cv.visitMethod(access, name, desc, signature, exceptions)
        //);
    }


    @Override
    public void visitSource(String string, String string1) {
        System.out.println("Visiting Source " + string);
        super.visitSource(string, string1); //To change body of generated methods, choose Tools | Templates.
        ApplicationModuleImplModifier.addDetailNameMethod(this.cw);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] exceptions) {
        System.out.println("Visit " + name);
        super.visit(version, access, name, signature, superName, exceptions); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println("Visit Field " + name);
        return super.visitField(access, name, desc, signature, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitInnerClass(String string, String string1, String string2, int i) {
        System.out.println("Visit InnerClass " + string);
        super.visitInnerClass(string, string1, string2, i); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitOuterClass(String string, String string1, String string2) {
        System.out.println("Visit OuterClass " + string);
        super.visitOuterClass(string, string1, string2); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
