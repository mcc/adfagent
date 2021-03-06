/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.agent.visitor;

import java.util.logging.Logger;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.ARETURN;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 *
 * @author cc
 */
public class MetricsMethodVisitor extends MethodVisitor{
    public LocalVariablesSorter localVariablesSorter;
    //public AnalyzerAdapter analyzerAdapter;
    private String className;
    private String methodName;
    private String desc;
    //private String methodDescriptor;
    private int time;
    //private int maxStack = 0;

    public MetricsMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
        super(ASM4, mv);
        System.out.println("methodName: " + methodName);
        this.className = className;
        this.methodName = methodName;
        this.desc = desc;
    }
    
    @Override
    public void visitCode() {
        System.out.println("Visit Code " + methodName);
        mv.visitCode(); 
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        time = localVariablesSorter.newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, time);
        if(methodName.equals("passivateState")){
            addPassivationMessageInjection();
        }
        System.out.println("End Visit Code " + methodName);
        //maxStack = 1;
    }

    @Override
    public void visitEnd() {
        System.out.println("Visit End " + methodName);
        //addEndingInjection();
        super.visitEnd(); //To change body of generated methods, choose Tools | Templates.
    }

    private void addPassivationMessageInjection(){
        mv.visitLdcInsn(Type.getType("Loracle/jbo/server/ApplicationModuleImpl;"));
        mv.visitMethodInsn(INVOKESTATIC, "oracle/adf/share/logging/ADFLogger", "createADFLogger", "(Ljava/lang/Class;)Loracle/adf/share/logging/ADFLogger;", false);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("passivateState, ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "oracle/jbo/server/ApplicationModuleImpl", "getDetailName", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "oracle/adf/share/logging/ADFLogger", "warning", "(Ljava/lang/String;)V", false);
    }
    
    private void addElapsedTimerEndingInjection(){
        System.out.println("Proceed to add ending injection");
        mv.visitLdcInsn(Type.getType("L" + this.className +";"));
        //mv.visitLdcInsn(MetricsMethodVisitor.class.toString());
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESTATIC, "oracle/adf/share/logging/ADFLogger", "createADFLogger", "(Ljava/lang/String;)Loracle/adf/share/logging/ADFLogger;", false);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn(this.className + ", " + methodName + ", " + this.desc + " , ElapsedTime=");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System","nanoTime","()J", false);
        mv.visitVarInsn(LLOAD, time);
        mv.visitInsn(LSUB);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "oracle/adf/share/logging/ADFLogger", "warning", "(Ljava/lang/String;)V", false);
    }
    
    @Override
    public void visitInsn(int opCode) {
        System.out.println("visitInsn " + opCode);
        if((opCode >= IRETURN && opCode <=RETURN) || opCode == ATHROW){
            addElapsedTimerEndingInjection();
        }
        mv.visitInsn(opCode); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        System.out.println("maxStack = " + maxStack + ", maxLocals = " + maxLocals);
        mv.visitMaxs(maxStack, maxLocals);
    }
    
}
