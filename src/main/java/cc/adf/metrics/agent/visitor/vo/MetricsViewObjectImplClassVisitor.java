package cc.adf.metrics.agent.visitor.vo;

import cc.adf.metrics.agent.visitor.ApplicationModuleImplModifier;
import cc.adf.metrics.agent.visitor.MetricsMethodVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 *
 * @author cc
 */
public class MetricsViewObjectImplClassVisitor extends ClassVisitor {
    private String className;
    private ClassWriter cw;
    public MetricsViewObjectImplClassVisitor(ClassWriter cw, String className) {
        super(Opcodes.ASM4, cw);
        this.cw = cw;
        this.className = className;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println("Visit Field " + name);
        return super.visitField(access, name, desc, signature, value); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
        //System.out.println("Visiting Method " + name);
        MethodVisitor mv = cv.visitMethod(access, methodName, desc, signature, exceptions);
        boolean isInterface = (access & ACC_INTERFACE) != 0;
        System.out.println("Visiting Method " + methodName);
        if (!isInterface && mv != null && (methodName.equals("<clinit>"))){
            System.out.println("Transforming Method " + methodName);
            MetricsCommonConstructorMethodVisitor newMv = new MetricsCommonConstructorMethodVisitor(mv, this.className, methodName, desc);
            System.out.println("this.name = " + this.className + ", desc = " + desc);
            newMv.localVariablesSorter = new LocalVariablesSorter(access, desc, newMv);
            return newMv.localVariablesSorter;
        }
        if (!isInterface && mv != null && (methodName.equals("executeQuery"))){
            System.out.println("Transforming Method " + methodName);
            MetricsExecuteQueryMethodVisitor newMv = new MetricsExecuteQueryMethodVisitor(mv, this.className, methodName, desc);
            System.out.println("this.name = " + this.className + ", desc = " + desc);
            newMv.localVariablesSorter = new LocalVariablesSorter(access, desc, newMv);
            return newMv.localVariablesSorter;
        }
        if (!isInterface && mv != null && (methodName.equals("getEstimatedRowCount"))){
            System.out.println("Transforming Method " + methodName);
            MetricsGetEstimatedRowCountMethodVisitor newMv = new MetricsGetEstimatedRowCountMethodVisitor(mv, this.className, methodName, desc);
            System.out.println("this.name = " + this.className + ", desc = " + desc);
            newMv.localVariablesSorter = new LocalVariablesSorter(access, desc, newMv);
            return newMv.localVariablesSorter;
        }
        
        return mv;
    }

    @Override
    public void visitSource(String string, String string1) {
        System.out.println("Visiting Source " + string);
        super.visitSource(string, string1); 
        //Add final static logger
        FieldVisitor fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, 
                "cc_metrics_logger", "Loracle/adf/share/logging/ADFLogger;", null, null);
        fv.visitEnd();
    }
    
    
    
}
