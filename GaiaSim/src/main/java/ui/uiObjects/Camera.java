package ui.uiObjects;

import java.util.Arrays;

public class Camera {

    private int[] midPos;
    private int[][] edgePos = new int[4][2];
    
    private int[] mapDim;
    
    private int z;
    
    public Camera(double[][] map) {
        
        int xDim = map.length;
        int yDim = map[0].length;
        midPos = new int[] {(int)(xDim/2),(int)(yDim/2)};
        mapDim = new int[] {xDim,yDim};
        z = 2;
        
        setEdgePos(xDim,yDim);
        
    }
    
    public Camera(double[][] map, int initZ) {
        
        int xDim = map.length;
        int yDim = map[0].length;
        midPos = new int[] {(int)(xDim/2),(int)(yDim/2)};
        mapDim = new int[] {xDim, yDim};
        z = initZ;
        
        setEdgePos(xDim,yDim);
        
    }
    
    public Camera(int dimX, int dimY) {
        
        z = 2;
        midPos = new int[] {(int)(dimX/2),(int)(dimY/2)};
        mapDim = new int[] {dimX,dimY};
        
        setEdgePos(dimX,dimY);
        
    }
    
    public Camera(int dimX, int dimY, int initZ) {
        
        z = initZ;
        midPos = new int[] {(int)(dimX/2),(int)(dimY/2)};
        mapDim = new int[] {dimX,dimY};
        
        setEdgePos(dimX,dimY);
    }
    
    private void setEdgePos(int xDim, int yDim) {
        
        edgePos[0][0] = (int)(midPos[0] - xDim/(2*z));
        edgePos[0][1] = (int)(midPos[1] - yDim/(2*z));
        
        edgePos[1][0] = (int)(midPos[0] + xDim/(2*z));
        edgePos[1][1] = (int)(midPos[1] - yDim/(2*z));
        
        edgePos[2][0] = (int)(midPos[0] - xDim/(2*z));
        edgePos[2][1] = (int)(midPos[1] + yDim/(2*z));
        
        edgePos[3][0] = (int)(midPos[0] + xDim/(2*z));
        edgePos[3][1] = (int)(midPos[1] + yDim/(2*z));
        
    }
    
    private void resetMidPoint() {
        
        int xIncr = mapDim[0]/(2*z);
        int yIncr = mapDim[1]/(2*z);
        
        if(midPos[0] + xIncr > mapDim[0])
            midPos[0] = mapDim[0] - xIncr;
        if (midPos[0] - xIncr < 0)
            midPos[0] = xIncr;
        
        if(midPos[1] + yIncr > mapDim[1])
            midPos[1] = midPos[1] - yIncr;
        if (midPos[1] - yIncr < 0)
            midPos[1] = yIncr;

    }
    
    public int[][] getEdgePos() {
        return edgePos;
    }
    
    public int[] getMidPos() {
        return midPos;
    }
    
    public int getXmin() {
        return edgePos[0][0];
    }
    
    public int getXmax() {
        return edgePos[1][0];
    }
    
    public int getYmin() {
        return edgePos[0][1];
    }
    
    public int getYmax() {
        return edgePos[2][1];
    }
    
    public void alterZoom(int nZ) {
        if(nZ >= 1 && nZ <= 15) {
            z = nZ;
            resetMidPoint();
            setEdgePos(mapDim[0],mapDim[1]);
            
        }
    }
    
    public void shiftByIncr(int xIncr, int yIncr) {
        
        if(xIncr >= 0) {
            if(edgePos[1][0] + xIncr < mapDim[0]) {
                midPos[0] = midPos[0] + xIncr;
                edgePos[0][0] = edgePos[0][0] + xIncr;
                edgePos[1][0] = edgePos[1][0] + xIncr;
                edgePos[2][0] = edgePos[2][0] + xIncr;
                edgePos[3][0] = edgePos[3][0] + xIncr;
            }
            
        }
        
        else {
            if(edgePos[0][0] + xIncr > 0) {
                midPos[0] = midPos[0] + xIncr;
                edgePos[0][0] = edgePos[0][0] + xIncr;
                edgePos[1][0] = edgePos[1][0] + xIncr;
                edgePos[2][0] = edgePos[2][0] + xIncr;
                edgePos[3][0] = edgePos[3][0] + xIncr;
            }
           
        }
        
        if(yIncr >= 0) {
            if(edgePos[2][1] + yIncr < mapDim[1]) {
                midPos[1] = midPos[1] + yIncr;
                edgePos[0][1] = edgePos[0][1] + yIncr;
                edgePos[1][1] = edgePos[1][1] + yIncr;
                edgePos[2][1] = edgePos[2][1] + yIncr;
                edgePos[3][1] = edgePos[3][1] + yIncr;
            }
            
        }
        else {
            if(edgePos[0][1] + yIncr > 0) {
                midPos[1] = midPos[1] + yIncr;
                edgePos[0][1] = edgePos[0][1] + yIncr;
                edgePos[1][1] = edgePos[1][1] + yIncr;
                edgePos[2][1] = edgePos[2][1] + yIncr;
                edgePos[3][1] = edgePos[3][1] + yIncr;
            }
           
        }
        

    }
}
