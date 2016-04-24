/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.adf.metrics.agent;

import cc.adf.metrics.transformer.AdfMetricsClassTransformer;
import java.lang.instrument.Instrumentation;

/**
 *
 * @author cc
 */
public class AdfMetricsAgent {
    public static void premain(String agentArgument, Instrumentation instrumentation){
        System.out.println("Test Java Agent");
        instrumentation.addTransformer(new AdfMetricsClassTransformer());
    }
}
