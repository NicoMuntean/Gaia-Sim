package map;

import java.awt.Color;
import logic.general.Matrix;
import logic.general.MathOperations;

public class TileMap{
    
    MathOperations mo = new MathOperations();
    
    private Matrix heightMap;
    private Matrix foodMap;
    private Matrix tileMap;
    private Color[][] colorMap;
    
    public TileMap(int xD, int yD) {
        
        heightMap = new Matrix(xD-3,yD-3);
        foodMap = new Matrix(xD-3,yD-3);
        tileMap = new Matrix(xD-3,yD-3);
        colorMap = new Color[xD-3][yD-3];
        
        fillMap(xD,yD);
        fillTileMap();
        
    }
    
    public void regrowFood() {
        
        for(int i = 0; i < foodMap.getNumCols(); i++){
            for(int j = 0; j < foodMap.getNumRows();j++) {
                
                double tv = heightMap.getVal(i, j);
                
                if(tv > 0.49 && tv < 0.75 && foodMap.getVal(i, j) < 0.999) {

                    foodMap.setVal(i,j,foodMap.getVal(i,j)+0.0001);
                    //colorMap[i][j] = mo.calcColorGrad(tv,foodMap.getVal(i, j));
                    
                }
                                                
            }
        }
        
    }
    
    public void updateFoodColor(){
        for(int i = 0; i < foodMap.getNumCols(); i++){
            for(int j = 0; j < foodMap.getNumRows();j++) {
                
                double tv = heightMap.getVal(i, j);
                
                if(tv > 0.49 && tv < 0.75) {

                    colorMap[i][j] = mo.calcColorGrad(tv,foodMap.getVal(i, j));
                    
                }
                                                
            }
        }
    }
    
    private void fillMap(int xD, int yD) {
        
        double[][] base = new double[xD][yD];
        
        for(int i = 0; i < xD; i++){
            for(int j = 0; j < yD; j++) {
                base[i][j] = -1;
            }
        }
        
        double[][] p0 = mo.createPerlinFill(xD,yD, 1, 1.0);
        double[][] p1 = mo.createPerlinFill(xD,yD,2,1.0);
        double[][] p2 = mo.createPerlinFill(xD,yD,3,1.0);
        double[][] p3 = mo.createPerlinFill(xD,yD,4,1.0);
        double[][] p4 = mo.createPerlinFill(xD,yD,5,1.0);
        double[][] p5 = mo.createPerlinFill(xD,yD,6,1.0);
        
        double[][] finalPerlin = new double[xD][yD];
        
        for(int i = 0; i < xD; i++) {
            for(int j = 0; j < yD; j++) {
                if(j>20 && i > 20 && j < 225 && i <225)
                    finalPerlin[i][j] = (p0[i][j]*3.5 + p1[i][j]*2 + p2[i][j]*2 + p3[i][j] + p4[i][j] + p5[i][j])/10.5;
                else
                    finalPerlin[i][j] = 0;
                //finalPerlin[i][j] = (p0[i][j]);
            }
        }
        
        finalPerlin = mo.normArr(mo.gaußKernelConvol(finalPerlin));
        
        heightMap.setVals(finalPerlin);
        
    }
    
    private void fillTileMap() {
        
        for(int i = 0; i < heightMap.getNumRows(); i++) {
            for(int j = 0; j < heightMap.getNumCols(); j++) {
                
                double tv = heightMap.getVal(i, j);
                
                if(tv < 0.15) {
                    tileMap.setVal(i, j, 0);
                    foodMap.setVal(i,j,0);
                    colorMap[i][j] = mo.calcColorGrad(0, tv);
                }
                else if(tv < 0.25) {
                    tileMap.setVal(i, j, 1);
                    foodMap.setVal(i,j,0);
                    colorMap[i][j] = mo.calcColorGrad(0, tv);
                }
                else if(tv < 0.35) {
                    tileMap.setVal(i, j, 2);
                    foodMap.setVal(i,j,0);
                    colorMap[i][j] = mo.calcColorGrad(0, tv);
                }
                else if(tv < 0.45) {
                    tileMap.setVal(i, j, 3);
                    foodMap.setVal(i,j,0);
                    colorMap[i][j] = mo.calcColorGrad(0, tv);
                }
                else if(tv < 0.49) {
                    tileMap.setVal(i, j, 4);
                    foodMap.setVal(i,j,-1);
                    colorMap[i][j] = new Color(226,202,108);
                } 
                else if(tv < 0.60) {
                    tileMap.setVal(i, j, 5);
                    foodMap.setVal(i,j,1);
                    colorMap[i][j] = mo.calcColorGrad(tv,foodMap.getVal(i, j));
                }
                else if(tv < 0.65) { 
                    tileMap.setVal(i, j, 6);
                    foodMap.setVal(i,j,1);
                    colorMap[i][j] = mo.calcColorGrad(tv,foodMap.getVal(i, j));
                }
                else if(tv < 0.75) {
                    tileMap.setVal(i, j, 7);
                    foodMap.setVal(i,j,1);
                    colorMap[i][j] = mo.calcColorGrad(tv,foodMap.getVal(i, j));
                }
                else if(tv < 0.81) {
                    tileMap.setVal(i, j, 8);
                    foodMap.setVal(i,j,0);
                    colorMap[i][j] = mo.calcColorGrad(2, tv);
                }
                else {
                    tileMap.setVal(i, j, 9);
                    foodMap.setVal(i,j,0);
                    colorMap[i][j] = mo.calcColorGrad(2, tv);
                }
                
            }
        }
    }
    
    public Matrix getTM() {
        return tileMap;
    }
    
    public Matrix getFM() {
        return foodMap;
    }
    
    public Matrix getHM() {
        return heightMap;
    }
    
    public Color getTileColor(int i, int j) {
        return colorMap[i][j];
    }
    
    public Color[][] getColorMap(){
        return colorMap;
    }
     
}
