package BrainNet;

import logic.general.MathOperations;
import logic.general.Vector;
import logic.general.Matrix;

public class Neuron {
    
    private Vector weights;
    private Vector inputData;
    private double bias;
    private int inputConnections;
    
    MathOperations mo = new MathOperations();
    
    public Neuron(int inputSize) {
        
        weights = mo.randomizeWeights(inputSize);
        inputData = new Vector(inputSize);
        bias = 0;
        inputConnections = inputSize;
        
    }
    
    public Neuron(double[] w, double b) {
        
        inputData = new Vector(w.length);
        weights = new Vector(w);
        bias = b;
        inputConnections = w.length;
        
    }
    
    public double generateOutput(double input[]) {
        
        double out;
        
        inputData.setAllComp(input);
        
        out = mo.dotV(inputData, weights) + bias;
        
        out = mo.activationTanh(out);
        
        
        return out;
    }
    
    public Vector getWeights() {
        
        return weights;
    }
    
    public double[] getWeightComponents() {
        
        return weights.getAllComponents();
    }
    
    public double getWeight(int i) {
        
        if(i < inputConnections && i >= 0) {
            return weights.getComponent(i);
        }
        else {
            System.out.println("WARNING! TRIED TO SET A VALUE FOR A WEIGHT OUT OF THE INDEX RANGE! 0 WILL BE RETURNED INSTEAD!");
            return 0.0D;
        }
    }
    
    public void setWeights(Vector nW) {
        
        weights = nW;
    }
    
    public void setWeightAtIndex(double nW, int i) {
        
        if(i < inputConnections && i >= 0) {
            weights.setComp(nW, i);
        }
        else {
            System.out.println("WARNING! TRIED TO SET A VALUE FOR A WEIGHT OUT OF THE INDEX RANGE! NO ACTION WAS PERFORMED!");
        }
    }
    
    public double getBias() {
        
        return bias;
    }
    
    public void setBias(double nB) {
       
        bias = nB;
    }
    
}
