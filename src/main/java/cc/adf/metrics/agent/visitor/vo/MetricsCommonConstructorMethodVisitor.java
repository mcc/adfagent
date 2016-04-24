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
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 *
 * @author cc
 */
public class MetricsCommonConstructorMethodVisitor extends MethodVisitor{
    public LocalVariablesSorter localVariablesSorter;
    private String className;
    private String methodName;
    private String desc;
    private int time;

    public MetricsCommonConstructorMethodVisitor(MethodVisitor mv, String className, String methodName, String desc) {
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
        mv.visitLdcInsn("cc.metrics");
        mv.visitMethodInsn(INVOKESTATIC, "oracle/adf/share/logging/ADFLogger", "createADFLogger", "(Ljava/lang/String;)Loracle/adf/share/logging/ADFLogger;", false);
        mv.visitFieldInsn(PUTSTATIC, className, "cc_metrics_logger", "Loracle/adf/share/logging/ADFLogger;");
        System.out.println("End Visit Code " + methodName);
        //maxStack = 1;
    }
    
}
