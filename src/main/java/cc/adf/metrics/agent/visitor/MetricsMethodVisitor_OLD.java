/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.agent.visitor;

import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 *
 * @author cc
 */
public class MetricsMethodVisitor_OLD extends LocalVariablesSorter{
    private String methodName;
    private String methodDescriptor;
    private int time;

    public MetricsMethodVisitor_OLD(MethodVisitor mv, String name) {
        super(ASM5, name, mv);
        System.out.println("methodName: " + name);
        methodName = name;

    }

    private MetricsMethodVisitor_OLD(MethodVisitor visitor, String name, String descriptor) {
        super(Opcodes.ASM4, name, visitor);
        System.out.println("methodName: " + name);
        methodName = name;
        methodDescriptor = descriptor;
    }
    
    @Override
    public void visitCode() {
        System.out.print("hihihihi");
        mv.visitCode(); 
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", true);
        time = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, time);
        
    }

    @Override
    public void visitInsn(int opCode) {
        if(opCode >= IRETURN && opCode <=RETURN || opCode == ATHROW){
            /**
    LDC Lcc/adf/metrics/agent/test/TestMain;.class
    INVOKESTATIC com/sun/istack/internal/logging/Logger.getLogger (Ljava/lang/Class;)Lcom/sun/istack/internal/logging/Logger;
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
    INVOKEVIRTUAL com/sun/istack/internal/logging/Logger.info (Ljava/lang/String;)V
             */
            mv.visitLdcInsn(MetricsMethodVisitor_OLD.class);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/istack/internal/logging/Logger", "getLogger", "(Ljava/lang/Class;)Lcom/sun/istack/internal/logging/Logger", true);
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("ElapsedTime=");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", true);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System","nanoTime","()J", true);
            mv.visitVarInsn(LLOAD, time);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", true);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", true);
            mv.visitMethodInsn(INVOKESTATIC, "com/sun/istack/internal/logging/Logger", "info", "(Ljava/lang/String;)V", true);
        }
        super.visitInsn(opCode); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 4, maxLocals);
    }
    
    

    
    
    
    
}
