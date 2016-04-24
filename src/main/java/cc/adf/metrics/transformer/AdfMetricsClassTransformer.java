/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.transformer;

import cc.adf.metrics.agent.visitor.MetricsClassVisitor;
import cc.adf.metrics.agent.visitor.MetricsMethodVisitor_OLD;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 *
 * @author cc
 */
public class AdfMetricsClassTransformer implements ClassFileTransformer {
    private static final Logger LOGGER = Logger.getLogger(AdfMetricsClassTransformer.class.getName());
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(className.contains("Test")){
            System.out.println("Processing class "+className);
            //String normalizedClassName = className.replace("/", ".");
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassReader classReader = new ClassReader(classfileBuffer);

            classReader.accept(new MetricsClassVisitor(writer, className), 0);
            byte[] result = writer.toByteArray();
            /* for debug to print the generated byte code
            ClassReader classReaderAfter = new ClassReader(result);
            TraceClassVisitor tcv = new TraceClassVisitor(new PrintWriter(System.out));
            classReaderAfter.accept(tcv, 0);*/
            return result;
        } else {
            return classfileBuffer;
        }
        
        //return classfileBuffer;
    }

}