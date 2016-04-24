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
import static org.objectweb.asm.Opcodes.LCONST_1;
import static org.objectweb.asm.Opcodes.LDIV;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LRETURN;
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
public class MetricsGetEstimatedRowCountMethodVisitor extends MethodVisitor{
    public LocalVariablesSorter localVariablesSorter;
    private String className;
    private String methodName;
    private String desc;
    private int time;

    public MetricsGetEstimatedRowCountMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
        super(ASM4, mv);
        System.out.println("methodName: " + methodName);
        this.className = className;
        this.methodName = methodName;
        this.desc = desc;
    }
    
    @Override
    public void visitInsn(int opCode) {
        System.out.println("visitInsn " + opCode);
        if(opCode == LRETURN){
            int estimatedRowCount = localVariablesSorter.newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, estimatedRowCount);
            mv.visitVarInsn(LLOAD, estimatedRowCount);
            mv.visitInsn(LCONST_1);
            mv.visitInsn(LCMP);
            Label l0 = new Label();
            mv.visitJumpInsn(IFLE, l0);
            mv.visitFieldInsn(GETSTATIC, this.className, "cc_metrics_logger", "Loracle/adf/share/logging/ADFLogger;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("getEstimatedRowCount, ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, this.className, "getName", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(", count = ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(LLOAD, estimatedRowCount);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(", query = ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, this.className, "getQuery", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "oracle/adf/share/logging/ADFLogger", "warning", "(Ljava/lang/String;)V", false);
            mv.visitLabel(l0);
            mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {Opcodes.LONG}, 0, null);
            mv.visitVarInsn(LLOAD, estimatedRowCount);
        }
        mv.visitInsn(opCode); 
    }
    
}
