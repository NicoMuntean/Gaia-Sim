
package organism;

import BrainNet.Brain;
import java.util.ArrayList;
import logic.general.Vector;
import map.TileMap;


public class Organism {
    
    private Brain b;
    
    private Vector pos;
    private Vector vel;
    private Vector acc;
    
    private double maxVel = 2;
    private double maxAcc = 0.002;
    
    private double orient = 0;
    private double acceleration = 0;
    private double velocity = 0;
    private double age = 0;
    private double ageSinceLastBirth = 0;
    
    private int lastAction = 0;
    
    private double energy;
    
    // Constructor Methods
    
    public Organism(Brain initB, Vector initPos) {
        
        b = initB;
        energy = 1.0;
        
        pos = initPos.copy();
        vel = new Vector(new double[] {0,0});
        acc = new Vector(new double[] {0,0});
        
    }
    
    public Organism(Brain initB, Vector initPos,double initEnergy) {
        
        b = initB;
        energy = initEnergy;
        
        pos = initPos.copy();
        
        velocity = (Math.random()*2*maxVel)-maxVel;
        acceleration = (Math.random()*2*maxAcc)-maxAcc; 
        
        vel = new Vector(new double[] {0,0});
        acc = new Vector(new double[] {0,0});
        
    }
    
    public Organism(Brain initB, int h, int w) {
        
        b = initB;
        energy = 1.0;
        
        double xP = w;
        double yP = h;
        
        pos = new Vector(new double[] {xP,yP});
        vel = new Vector(new double[] {0,0});
        acc = new Vector(new double[] {0,0});     
        
    }
    
    public Organism(int h, int w) {
        
        b = new Brain(1,1,1,1);
        energy = 1.0;
        
        double xP = Math.random() * w;
        double yP = Math.random() * h;
        
        pos = new Vector(new double[] {xP,yP});
        vel = new Vector(new double[] {0,0});
        acc = new Vector(new double[] {0,0});     
        
    }
    
    // Organism Methods
    
    public void perceiveEnvironment(double[] inputArr,TileMap tm,ArrayList<Organism> organismList) {
        
        initiateAction(inputArr,tm,organismList);
    }
    
    public void updatePos(TileMap tm) {
        
        double deg = orient;
        //setPos(getXPos() + getXVel(),getYPos() + getYVel());
        setPos(getXPos() + velocity*Math.sin(deg),getYPos() + velocity*Math.cos(deg));
        drainEnergy(tm);
        age += 1;
        ageSinceLastBirth += 1;
    }
    
    /*
    * Initiates an action based on the Brain NN output value, mapping is defined as:
    *
    * 0 => Accelerate upwards
    * 1 => Accelerate Downwards
    * 2 => Accelerate Left
    * 3 => Accelerate Right
    * 4 => Try to feed
    * 5 => Try to Procreate
    *
    */
    private void initiateAction(double[] input, TileMap tm,ArrayList<Organism> organismList) {
        
         Vector outVec = b.retreiveActionAll(input);
         
         //lastAction = out;
         
         //accUp(outVec.getComponent(0));
         //accDown(outVec.getComponent(1));
         //accLeft(outVec.getComponent(2));
         //accRight(outVec.getComponent(3));
         
         changeDir(outVec.getComponent(0));
         
         
         accellerate(outVec.getComponent(1)/100);
         
         if(outVec.getComponent(2) > 0.0) {
             feed(tm);
         }
         if(outVec.getComponent(3) > 0.5) {
             procreate(organismList);
         }
         
    }
    
    /*
    * Organism Actions
    */
    
    private void changeDir(double dir) {
        
        orient += dir;
        
        if(orient < 0)
            orient = orient + 2*Math.PI;
        else if(orient > 2*Math.PI)
            orient = orient-2*Math.PI;
        
    }
    
    private void accellerate(double acc) {
        
        if(acceleration + acc <= maxAcc && acceleration + acc >= -maxAcc) {
            acceleration += acc;
        }
        else if(acceleration + acc > maxAcc) {
            acceleration = maxAcc;
        }
        else
            acceleration = -maxAcc;
        
        updateVelocity();
    }
    
    private void updateVelocity() {
        
        if(velocity + acceleration < maxVel && velocity + acceleration + acceleration > -maxVel) {
            velocity += acceleration;
        }
        else if(velocity + acceleration > maxVel) {
            velocity = maxVel;
        }
        else
            velocity = -maxVel;
        
    }
      
    private void drainEnergy(TileMap tm) {
        
        double drainElem = 0;
        double hVal = tm.getHM().getVal((int)(pos.getComponent(0)/4), (int)(pos.getComponent(1)/4));
        
        if(0.45 < hVal && hVal < 0.75) {
            drainElem = Math.abs((velocity/maxVel)*0.005) + 0.001 + 0.005*(age/10000);
            //drainElem = 0.01 + 0.1*(age/10000);
        }
        else if(hVal < 0.45) {
            //drainElem = ((0.45-hVal)/0.45)*0.5;
            drainElem = Math.abs(velocity/maxVel)*0.05+0.05 + 0.005*(age/10000);
            //drainElem = 0.1 + 0.1*(age/10000);
        }
        else if(hVal > 0.75) {
            //drainElem = ((hVal-0.65)/0.35)*0.5;
            drainElem=Math.abs(velocity/maxVel)*0.05+0.05 + 0.005*(age/10000);
            //drainElem= 0.1 + 0.1*(age/10000);
        }
        energy -= drainElem;
    }
   
    
    private void feed(TileMap tm) {
        
        double foodVal = tm.getFM().getVal((int)(getPos().getComponent(0)/4), (int)(getPos().getComponent(1)/4));
        if(foodVal > 0) {
            
            if(foodVal >= 0.2) {
                if(energy < 1.0) {
                    energy +=0.2;
                    if(energy > 1.0)
                        energy = 1.0;
                    tm.getFM().setVal((int)(pos.getComponent(0)/4), (int)(pos.getComponent(1)/4),foodVal-0.2);
                }
            }
            else {
                energy += foodVal;
                if(energy > 1.0)
                        energy = 1.0;
                tm.getFM().setVal((int)(pos.getComponent(0)/4), (int)(pos.getComponent(1)/4),0.0);
            }
            
        }
        else
            energy -= 0.005;
        
    }
    
    private void procreate(ArrayList<Organism> organismList) {
        if(getEnergy() > 0.7 && age > 50 && ageSinceLastBirth > 50) {
            organismList.add(new Organism(new Brain(b.getBrainModel()),getPos(),0.5));
            energy -= 0.7;
            ageSinceLastBirth = 0;
        }
        
    }
    
    // Getter and Setter methods
    
    // Brain Getter & Setter
    
    public Brain getBrain() {
        return b;
    }
    
    // Energy Getter & Setter
    
    public double getEnergy() {
        return energy;
    }
    
    public void setEnergy(double nE) {
        energy = nE;
    }
    
    public void alterEnergy(double eIncr) {
        energy += eIncr;
    }
    
    // Position Getter & Setter
    
    // Getter
    
    public double getOrient() {
        return orient;
    }
    
    public Vector getPos() {
        return pos;
    }
    
    public double[] getPosAsDoubleArr() {
        return pos.getAllComponents();
    }
    
    public double getXPos() {
        return pos.getComponent(0);
    }
    
    public double getYPos() {
        return pos.getComponent(1);
    }
    
    public int getLastAction(){
        return lastAction;
    }
    
    // Setter
    
    public void setPos(Vector nPos) {
        pos = nPos.copy();
    }
    
    public void setPos(double[] nPosArr) {
        pos.setAllComp(nPosArr);
    }
    
    public void setPos(double nX, double nY) {
        pos.setComp(nX, 0);
        pos.setComp(nY,1);
    }
    
    public void setX(double nX) {
        pos.setComp(nX,0);
    }
    
    public void setY(double nY) {
        pos.setComp(nY,1);
    }
    
    public void setAge(double newAge) {
        age = newAge;
    }
    
    // Velocity Getter & Setter
    
    // Getter
    
    public double getAge() {
        return age;
    }
    
    public Vector getVelV() {
        return vel;
    }
    
    public double getVel() {
        return velocity;
    }
    
    public double[] getVelAsDoubleArr() {
        return vel.getAllComponents();
    }
    
    public double getXVel() {
        return vel.getComponent(0);
    }
    
    public double getYVel() {
        return vel.getComponent(1);
    }
    
    // Setter
    
    public void setVel(Vector nVel) {
        vel = nVel.copy();
    }
    
    public void setVel(double[] nVelArr) {
        vel.setAllComp(nVelArr);
    }
    
    public void setVel(double nXvel, double nYvel) {
        vel.setComp(nXvel, 0);
        vel.setComp(nYvel,1);
    }
    
    public void setXvel(double nXvel) {
        vel.setComp(nXvel,0);
    }
    
    public void setYvel(double nYvel) {
        vel.setComp(nYvel,1);
    }
    
    // Acceleration Getter & Setter
    
    // Getter
    
    public Vector getAccV() {
        return acc;
    }
    
    public double getAcc() {
        return acceleration;
    }
    
    public double[] getAccAsDoubleArr() {
        return acc.getAllComponents();
    }
    
    public double getXAcc() {
        return acc.getComponent(0);
    }
    
    public double getYAcc() {
        return acc.getComponent(1);
    }
    
    // Setter
    
    public void setAcc(Vector nAcc) {
        acc = nAcc.copy();
    }
    
    public void setAcc(double[] nAccArr) {
        acc.setAllComp(nAccArr);
    }
    
    public void setAcc(double nXacc, double nYacc) {
        acc.setComp(nXacc,0);
        acc.setComp(nYacc,1);
    }
    
    public void setXacc(double nXacc) {
        acc.setComp(nXacc,0);
    }
    
    public void setYacc(double nYacc) {
        acc.setComp(nYacc,1);
    }
}
