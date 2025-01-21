package BrainNet;

import logic.general.MathOperations;
import logic.general.Vector;

public class Brain {
    
    private MathOperations mo = new MathOperations();
    
    private Neuron[] inputLayer;
    private Neuron[] h1Layer;
    private Neuron[] h2Layer;
    private Neuron[] outputLayer;
    
    /*
    * Initializing the Brain object, which is assembled from one input Layer, two hidden Layers as well as the output Layer
    */
    public Brain(int[] neurDistr) {
        
        if(neurDistr.length == 4) {
            
            inputLayer = new Neuron[neurDistr[0]];
            h1Layer = new Neuron[neurDistr[1]];
            h2Layer = new Neuron[neurDistr[2]];
            outputLayer = new Neuron[neurDistr[3]];
            
            for(int i = 0; i < neurDistr[0]; i++) {
                inputLayer[i] = new Neuron(1);
            }
            
            for(int i = 0; i < neurDistr[1]; i++) {
                h1Layer[i] = new Neuron(neurDistr[0]);
            }
            
            for(int i = 0; i < neurDistr[2]; i++) {
                h2Layer[i] = new Neuron(neurDistr[1]);
            }
            
            for(int i = 0; i < neurDistr[3]; i++) {
                outputLayer[i] = new Neuron(neurDistr[2]);
            }
            
        }
        else {
            
            System.out.println("ERROR! DIMENSION OF LAYER STRUCTURE MUST BE EQUAL TO 4!");
        }
        
    }
    
    public Brain(int inputDim, int h1Dim, int h2Dim, int outputDim) {
        
        inputLayer = new Neuron[inputDim];
        h1Layer = new Neuron[h1Dim];
        h2Layer = new Neuron[h2Dim];
        outputLayer = new Neuron[outputDim];
            
        for(int i = 0; i < inputDim; i++) {
            inputLayer[i] = new Neuron(1);
        }
            
        for(int i = 0; i < h1Dim; i++) {
            h1Layer[i] = new Neuron(inputDim);
        }
            
        for(int i = 0; i < h2Dim; i++) {
            h2Layer[i] = new Neuron(h1Dim);
        }
            
        for(int i = 0; i < outputDim; i++) {
            outputLayer[i] = new Neuron(h2Dim);
        }
        
    }
    
    /*
    * Called, if the Organism posessig the brain has been the result of another organisms reproduction
    * Needs an already defined Brain object to function. Mutation (Random shifting of the Neurons weights
    * and Biases) is induced.
    */
    public Brain(Neuron[][] parentalBrainstructure) {
        
        inputLayer = new Neuron[parentalBrainstructure[0].length];
        h1Layer = new Neuron[parentalBrainstructure[1].length];
        h2Layer = new Neuron[parentalBrainstructure[2].length];
        outputLayer = new Neuron[parentalBrainstructure[3].length];
        
        for(int i = 0; i < inputLayer.length; i++) {
            inputLayer[i] = induceMutation(parentalBrainstructure[0][i]);
        }
        
        for(int i = 0; i < h1Layer.length; i++) {
            h1Layer[i] = induceMutation(parentalBrainstructure[1][i]);
        }
        
        for(int i = 0; i < h2Layer.length; i++) {
            h2Layer[i] = induceMutation(parentalBrainstructure[2][i]);
        }
        
        for(int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = induceMutation(parentalBrainstructure[3][i]);
        }
        
    }
    
    /*
    * Returns the connections and layers of the current Brain model as a 2D Neuron array
    */
    public Neuron[][] getBrainModel() {
        return new Neuron[][] {inputLayer,h1Layer,h2Layer,outputLayer};
    }
        
    /*
    * Forward propagation of an initial signal, passing through 2 hidden layers
    */
    public double[] processInput(double[] input) {
        
        double[] initialOutput = new double[inputLayer.length];
        double[] h1Output = new double[h1Layer.length];
        double[] h2Output = new double[h2Layer.length];
        double[] finalOutput = new double[outputLayer.length];
        
        for(int i = 0; i < inputLayer.length; i++) {
            initialOutput[i] = inputLayer[i].generateOutput(new double[] {input[i]});
        }
        
        for(int i = 0; i < h1Layer.length; i++) {
            h1Output[i] = h1Layer[i].generateOutput(initialOutput);
        }
        
        for(int i = 0; i < h2Layer.length; i++) {
            h2Output[i] = h2Layer[i].generateOutput(h1Output);
        }
        
        for(int i = 0; i < outputLayer.length; i++) {
            finalOutput[i] = outputLayer[i].generateOutput(h2Output);
        }
        
        return finalOutput;
    }
    
    /*
    * Interpreting the received signal from the forward propagation. The action mapped to the strongest signal
    * is ultimately performed.
    */
    public int retreiveAction(double[] input) {
        
        double[] neurOutput = processInput(input);
        
        double maxSignal = 0;
        int actionToPerform = 0;
        
        for(int i = 0; i < neurOutput.length; i++) {
            
            if(neurOutput[i] > maxSignal){
                actionToPerform = i;
                maxSignal = neurOutput[i];
            }
        }
        
        return actionToPerform;
    }
    
    /*
    * Retreives the complete signals from the neural net
    */
    
    public Vector retreiveActionAll(double[] input) {
        
        double[] neurOutput = processInput(input);
        
        return new Vector(neurOutput);
    }
    
    /*
    * Inducing stochastic Mutation governed by the Java random number generator
    * Bias and Weights have a seperate possibility of being reshuffeled
    */
    public Neuron induceMutation(Neuron parentalNeuron) {
        
        int mutationChance = (int) (Math.random() * 10);
        
        double[] mutatedWeights = parentalNeuron.getWeightComponents();
        double mutatedBias = parentalNeuron.getBias();
        
        if(mutationChance <= 5) {
            
            for(int i = 0; i < mutatedWeights.length; i++) {
                
                int weightMutationChance = (int) (Math.random()*10);
            
                if(weightMutationChance <=5) {
                
                    mutatedWeights[i] = mutatedWeights[i] + Math.random()/50 - (1/100);
                }
            }
            
        }
        
        if(mutationChance >= 3 && mutationChance <= 5) {
            
            mutatedBias = mutatedBias + Math.random() / 50 - (1/100);
            //mutatedBias = 0;
            
        }
       
        return new Neuron(mutatedWeights, mutatedBias);
        
    }
    
}
