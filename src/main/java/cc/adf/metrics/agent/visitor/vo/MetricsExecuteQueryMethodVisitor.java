package cc.adf.metrics.agent.visitor.vo;

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
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LCMP;
import static org.objectweb.asm.Opcodes.LDIV;
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
public class MetricsExecuteQueryMethodVisitor extends MethodVisitor{
    public LocalVariablesSorter localVariablesSorter;
    private String className;
    private String methodName;
    private String desc;
    private int time;

    public MetricsExecuteQueryMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
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
        System.out.println("End Visit Code " + methodName);
        //maxStack = 1;
    }
    
    private void addElapsedTimerEndingInjection(){
        System.out.println("Proceed to add ending injection");
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        mv.visitVarInsn(LLOAD, time);
        mv.visitInsn(LSUB);
        mv.visitLdcInsn(new Long(1000000L));
        int elapsedTime = localVariablesSorter.newLocal(Type.LONG_TYPE);
        mv.visitInsn(LDIV);
        mv.visitVarInsn(LSTORE, elapsedTime);
        mv.visitVarInsn(LLOAD, elapsedTime);
        mv.visitLdcInsn(new Long(10L));
        mv.visitInsn(LCMP);
        Label l0 = new Label();
        mv.visitJumpInsn(IFLE, l0);
        mv.visitFieldInsn(GETSTATIC, "oracle/jbo/server/ViewObjectImpl", "cc_metrics_logger", "Loracle/adf/share/logging/ADFLogger;");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("executeQuery, name=");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "oracle/jbo/server/ViewObjectImpl", "getName", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitLdcInsn(", elapsedTime=");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(LLOAD, elapsedTime);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitLdcInsn(", query=");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "oracle/jbo/server/ViewObjectImpl", "getQuery", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "oracle/adf/share/logging/ADFLogger", "warning", "(Ljava/lang/String;)V", false);
        mv.visitLabel(l0);
        mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {Opcodes.LONG, Opcodes.LONG}, 0, null);
    }
    
    @Override
    public void visitInsn(int opCode) {
        System.out.println("visitInsn " + opCode);
        if(opCode == RETURN){
            addElapsedTimerEndingInjection();
        }
        /*if((opCode >= IRETURN && opCode <=RETURN) || opCode == ATHROW){
            addElapsedTimerEndingInjection();
        }*/
        mv.visitInsn(opCode); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        System.out.println("maxStack = " + maxStack + ", maxLocals = " + maxLocals);
        mv.visitMaxs(maxStack, maxLocals);
    }
    
}
