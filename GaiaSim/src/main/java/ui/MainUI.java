package ui;

import java.awt.Color;
import logic.general.MathOperations;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import map.TileMap;
import ui.uiObjects.Camera;
import organism.Organism;
import BrainNet.Brain;
import java.util.ArrayList;
import javax.swing.RepaintManager;

public class MainUI extends JPanel implements KeyListener {

    private TileMap tileMap;
    private MathOperations mo = new MathOperations();
    private int z = 2;

    private boolean w_pressed = false;
    private boolean a_pressed = false;
    private boolean s_pressed = false;
    private boolean d_pressed = false;
    private boolean zoomInRequest = false;
    private boolean zoomOutRequest = false;

    private boolean paintDone = false;
    private boolean updateDone = false;

    private final long TARGET_FPS = 160;
    private long elapsedTime;
    private long totalElapsed = 0;
    private long milliCnt = 0;
    private long startTime;

    private Camera c;

    private ArrayList<Organism> organismList = new ArrayList<Organism>();

    public MainUI(int sx, int sy, TileMap tm) {

        tileMap = tm;

        for (int i = 0; i < 10; i++) {
            organismList.add(new Organism(new Brain(27, 16, 16, 4), (int) (Math.random() * 900) + 100, (int) (Math.random() * 900) + 100));
            //organismList.add(new Organism(new Brain(10, 16, 16, 4), (int) (Math.random() * 900) + 100, (int) (Math.random() * 900) + 100));
        }

        c = new Camera(tm.getHM().toFloatArray(), z);
        setSize(sx, sy);
        setVisible(true);
        setFocusable(true);
        requestFocusInWindow(true);
        setDoubleBuffered(true);
        addKeyListener(this);
        setDoubleBuffered(true);
                
        new Thread() {
            @Override
            public void run() {

                long referenceTime = System.currentTimeMillis();
                startTime = System.currentTimeMillis();

                while (true) {

                    if (milliCnt % 3 == 0) {
                        checkPresses();
                        
                    }
                    
                    if (milliCnt % (5) == 0)
                        repaint();

                    elapsedTime = System.currentTimeMillis() - startTime;

                    totalElapsed = System.currentTimeMillis() - referenceTime;
                    milliCnt += 1;

                    if (milliCnt % 10 == 0 && paintDone) {
                        
                        updateDone = false;

                        tm.updateFoodColor();
                        
                        ArrayList<Organism> toBeRemovedIDs = new ArrayList<Organism>();

                        for (int i = 0; i < organismList.size(); i++) {

                            Organism o = organismList.get(i);

                            if (o.getXPos() - o.getXAcc() > 15 && o.getYPos() - o.getYAcc() > 15 && o.getXPos() + o.getXAcc() < 230 * 4
                                    && o.getYPos() + o.getYAcc() < 230 * 4 && o.getEnergy() > 0) {

                                double h = tileMap.getHM().getVal((int) o.getXPos() / 4, (int) o.getYPos() / 4);
                                double food = 0;
                                if (h > 0.3 && h < 0.6) {
                                    food = (0.3 + h) / 0.9;
                                }

                                double v = mo.activationTanh(o.getVel());
                                double a = mo.activationTanh(o.getAcc());
                                double foodVal = tileMap.getFM().getVal((int) o.getXPos() / 4, (int) o.getYPos() / 4);

                                double organismDir = o.getOrient();

                                double[] heightValArray = new double[20];
                                
                                double foodAvgInDir = 0;
                                
                                int cnt = 0;
                                for (int j = 0; j < 5; j++) {
                                    for (int k = -j; k < j; k++) {
                                        heightValArray[cnt] = tileMap.getFM().getVal((int)((o.getXPos()/4) + ((j) * Math.sin(organismDir)) +((k) * Math.sin(organismDir+(Math.PI/2)))), (int) ((o.getYPos()/4) + ((j)*Math.cos(organismDir)) + ((k)*Math.cos(organismDir+(Math.PI/2)))));
                                        //foodAvgInDir += tileMap.getFM().getVal((int)((o.getXPos()/4) + ((j) * Math.sin(organismDir)) +((k) * Math.sin(organismDir+(Math.PI/2)))), (int) ((o.getYPos()/4) + ((j)*Math.cos(organismDir)) + ((k)*Math.cos(organismDir+(Math.PI/2)))))/(Math.sqrt(k*k +j*j + 1));
                                        cnt += 1;
                                        
                                    }
                                }
                                
                                
                                o.perceiveEnvironment(new double[]{h, food, v, a, heightValArray[0], heightValArray[1], heightValArray[2], heightValArray[3], heightValArray[4],
                                    heightValArray[5], heightValArray[6], heightValArray[7], heightValArray[9], heightValArray[10], heightValArray[11],
                                    heightValArray[12], heightValArray[13], heightValArray[14],heightValArray[15],heightValArray[16], heightValArray[17],
                                    heightValArray[18],heightValArray[19], foodVal, o.getAge(), o.getXPos(), o.getYPos()}, tm, organismList);
                                
                                //o.perceiveEnvironment(new double[]{h, food, v, a, h, foodVal,foodAvgInDir/cnt, o.getAge()/10000, o.getXPos(), o.getYPos()}, tm, organismList);
                                o.updatePos(tm);

                            } else {
                                toBeRemovedIDs.add(o);
                            }

                        }
                        for (Organism o : toBeRemovedIDs) {
                            organismList.remove(o);
                        }

                        tileMap.regrowFood();
                        updateDone = true;
                    }

                    if (elapsedTime < 1000 / TARGET_FPS) {

                        try {
                            //System.out.println(1000/ TARGET_FPS - elapsedTime);
                            this.sleep(1000 /(TARGET_FPS - elapsedTime));
                            startTime = System.currentTimeMillis();

                        } catch (Exception e) {

                        }
                    }

                    if (totalElapsed >= 1000) {

                        referenceTime = System.currentTimeMillis();
                        System.out.println("FPS: " + milliCnt);

                        milliCnt = 0;

                    }

                }
            }
        }.start();

    }

    private void checkPresses() {

        if (w_pressed && paintDone) {
            c.shiftByIncr(0, -1);
        }
        if (a_pressed && paintDone) {
            c.shiftByIncr(-1, 0);
        }
        if (s_pressed && paintDone) {
            c.shiftByIncr(0, 1);
        }
        if (d_pressed && paintDone) {
            c.shiftByIncr(1, 0);
        }
        if (zoomInRequest && z < 7 && paintDone) {
            zoomInRequest = false;
            z = z + 1;
            c.alterZoom(z);
        } else if (zoomOutRequest && z > 1 && paintDone) {
            zoomOutRequest = false;
            z = z - 1;
            c.alterZoom(z);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        paintDone = false;
        super.paintComponent(g);
        
        if (updateDone) {
            
            Graphics2D g2d = (Graphics2D) g;

            int xmin = c.getXmin();
            int xmax = c.getXmax();
            int ymin = c.getYmin();
            int ymax = c.getYmax();

            int currZ = z;

            Color[][] currentColorMap = tileMap.getColorMap();

            for (int i = xmin; i < xmax; i++) {

                for (int j = ymin; j < ymax; j++) {

                    g2d.setColor(currentColorMap[i][j]);

                    g2d.fillRect((i - xmin) * currZ * 4, (j - ymin) * currZ * 4, currZ * 4, currZ * 4);

                }
            }

            g2d.setColor(Color.RED);

            for (Organism o : organismList) {
                
                
                g2d.fillOval(((int) o.getXPos() - xmin * 4) * currZ, ((int) o.getYPos() - ymin * 4) * currZ, currZ * 4, currZ * 4);
            }
            
            
        }
        paintDone = true;

    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
        if(System.getProperty("os.name").contains("Windows")) {

            switch (ke.getKeyCode()) {
                case KeyEvent.VK_A ->
                    a_pressed = true;
                case KeyEvent.VK_D ->
                    d_pressed = true;
                case KeyEvent.VK_W ->
                    w_pressed = true;
                case KeyEvent.VK_S ->
                    s_pressed = true;
                case KeyEvent.VK_PLUS ->
                    zoomInRequest = true;
                case KeyEvent.VK_MINUS ->
                    zoomOutRequest = true;
            }
        }
        else if(System.getProperty("os.name").contains("Mac")) {
            switch (ke.getKeyCode()) {
                case 65 ->                      // A - key
                    a_pressed = true;
                case 68 ->                      // D - key
                    d_pressed = true;
                case 87 ->                      // W - key
                    w_pressed = true;
                case 83 ->                      // S - key
                    s_pressed = true;
                case 93 ->                      // PLUS - key
                    zoomInRequest = true;
                case 47 ->                      // MINUS - key
                    zoomOutRequest = true;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {

        if(System.getProperty("os.name").contains("Windows")) {
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_A ->
                    a_pressed = false;
                case KeyEvent.VK_D ->
                    d_pressed = false;
                case KeyEvent.VK_W ->
                    w_pressed = false;
                case KeyEvent.VK_S ->
                    s_pressed = false;
            }
        }
        
        else if(System.getProperty("os.name").contains("Mac")) {
            switch (ke.getKeyCode()) {
                case 65 ->                      // A - key
                    a_pressed = false;
                case 68 ->                      // D - key
                    d_pressed = false;
                case 87 ->                      // W - key
                    w_pressed = false;
                case 83 ->                      // S - key
                    s_pressed = false;
            }
        }
    }

}
