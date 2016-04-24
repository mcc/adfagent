/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.agent.visitor;

import java.util.logging.Logger;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
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
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 *
 * @author cc
 */
public class MetricsMethodVisitor extends MethodVisitor{
    public LocalVariablesSorter localVariablesSorter;
    public AnalyzerAdapter analyzerAdapter;
    private String methodName;
    private String methodDescriptor;
    private int time;
    private int maxStack = 0;

    public MetricsMethodVisitor(MethodVisitor mv, String name) {
        super(ASM4, mv);
        System.out.println("methodName: " + name);
        methodName = name;

    }
    
    @Override
    public void visitCode() {
        System.out.println("Visit Code " + methodName);
        mv.visitCode(); 
        Label label = new Label();
        mv.visitLabel(label);
        mv.visitLineNumber(1, label);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        time = localVariablesSorter.newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, time);
        
        //maxStack = 1;
    }

    @Override
    public void visitInsn(int opCode) {
        System.out.println("visitInsn " + opCode);
        if(opCode >= IRETURN && opCode <=RETURN || opCode == ATHROW){
            System.out.println("Proceed to add ending injection");
            /**
    LDC "testing"
    INVOKESTATIC java/util/logging/Logger.getLogger (Ljava/lang/String;)Ljava/util/logging/Logger;
    NEW java/lang/StringBuilder
    DUP
    INVOKESPECIAL java/lang/StringBuilder.<init> ()V
    LDC "ElapsedTime="
    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
    INVOKESTATIC java/lang/System.nanoTime ()J
    LLOAD 1
    LSUB
    INVOKEVIRTUAL java/lang/StringBuilder.append (J)Ljava/lang/StringBuilder;
    INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
    INVOKEVIRTUAL java/util/logging/Logger.info (Ljava/lang/String;)V
             */
            Label label = new Label();
            mv.visitLabel(label);
            mv.visitLineNumber(999, label);
            mv.visitLdcInsn(MetricsMethodVisitor.class.toString());
            mv.visitMethodInsn(INVOKESTATIC, "java/util/logging/Logger", "getLogger", "(Ljava/lang/String;)Ljava/util/logging/Logger;", false);
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("ElapsedTime=");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System","nanoTime","()J", false);
            mv.visitVarInsn(LLOAD, time);
            mv.visitInsn(LSUB);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "info", "(Ljava/lang/String;)V", false);
            //System.out.println("maxStack = " + maxStack + ", analyzerAdapter.stack.size() + 4 = " + (analyzerAdapter.stack.size() + 4));
            //maxStack = Math.max(maxStack, analyzerAdapter.stack.size() + 4);
            //maxStack = maxStack+2;
        }
        mv.visitInsn(opCode); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        System.out.println("maxStack = " + maxStack + ", this.maxStack = " + this.maxStack + ", maxLocals = " + maxLocals);
        mv.visitMaxs(Math.max(this.maxStack, maxStack), maxLocals);
    }
    
    

    
    
    
    
}
