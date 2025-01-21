package logic.general;

public class Vector {
    
    private int dim;
    private double[] vals;
    
    /*
    * Creates an Vector object with the dimension of the length of the input array inputVals, the vector object will hold
    * the data provided in inputVals.
    */
    public Vector(double[] inputVals) {
        
        vals = inputVals;
        dim = vals.length;
        
    }
    
    /*
    * Creates a 3D-Vector-object, x,y and z components have to be provided
    */
    public Vector(double x, double y, double z) {
        
        vals = new double[] {x,y,z};
        dim = 3;
    }
    
    public Vector(double[] p1, double[] p2) {
        
        vals = new double[p1.length];
        
        for(int i = 0; i < p1.length; i++) {
            
            vals[i] = p2[i] - p1[i];
        }
        dim = vals.length;
    }
    
    /*
    * Creates a zero-vector of dimension vecDim
    */
    public Vector(int vecDim) {
        
        dim = vecDim;
        vals = new double[dim];
        
        for(int x = 0; x < dim; x++) {
            vals[x] = 0;
        }
    }

    
    /*
    * Returns the dimension of the Vector.
    * The output is an Integer.
    */
    public int getDim() {
        return dim;
    }
    
    /*
    * Returns the Vector Component with the index i.
    * The output is a float.
    */
    public double getComponent(int i) {
        if(i < dim || i >= 0)
            return vals[i];
        else{
            System.out.println("ERROR: REQUESTED COMPONENT DIMENSION IS LARGER THAN THE VECTOR OBJECTS DIMENSION!");
            return -9999;
        }    
    }
    
    /*
    * DOuble implementation of the absolute length bc I am lazy
    */
    public double getLen() {
        double sum = 0;
        for(double i : vals) {
            sum += i*i;
        }
        return Math.sqrt(sum);
    }
    
    /*
    * Returns all Vector Components as a float array possessing the length equal to the vector dimension.
    * The output is a float array.
    */
    public double[] getAllComponents() {
        return vals;
    }
    
    /*
    * Sets a specific component of the vector object to a new value
    */
    public void setComp(double newComp, int i) {
        if(i < dim || i >= 0)
            vals[i] = newComp;
        else{
            System.out.println("ERROR: REQUESTED COMPONENT DIMENSION IS LARGER THAN THE VECTOR OBJECTS DIMENSION!");
        }   
    }
    
    /*
    * Sets the all Vector object Components to new values nComp, can also alter the Vector objects dimension
    */
    public void setAllComp(double[] nComp) {
        vals = nComp;
        dim = vals.length;
    }
    
    /*
    * Prints the Vector object into the console
    * The Integer variable mode defines if the Vector is to be printed as a column (mode == true) or a row vector (mode == false)
    */
    public void printVector(boolean mode) {
        
        if(mode) {
            for(int x = 0; x < dim; x++) {
                System.out.print("|" + vals[x] + "|");
                System.out.println();
            }
        }
        else {
            System.out.print("(");
            for(int x = 0; x < dim; x++) {
                System.out.print(vals[x] + ",");
            }
            System.out.println(")");
        }
    }
    
    /*
    * Returns a copy of the Vector object
    */
    public Vector copy() {
        return new Vector(vals.clone());
    }
}
