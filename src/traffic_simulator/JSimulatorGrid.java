package traffic_simulator;

import javax.swing.*;
import java.awt.*;

public class JSimulatorGrid extends JPanel {
    int WIDTH = 500;
    int HEIGHT = 500;
    Road[] roadsOnGrid;
    TIntersection[] TIntersectionOnGrid;
    CrossIntersection[] crossIntersectionOnGrid;
    Vehicle[] drawnCars;


    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < roadsOnGrid.length; i++) {
            g.setColor(Color.BLACK);
            if (roadsOnGrid[i].direction.equals("north-south")) {
                g.fillRect(roadsOnGrid[i].getRoadEnd1X(), roadsOnGrid[i].getRoadEnd1Y(), 5, roadsOnGrid[i].ROAD_LENGTH);
            } else {
                g.fillRect(roadsOnGrid[i].getRoadEnd1X(), roadsOnGrid[i].getRoadEnd1Y(), roadsOnGrid[i].ROAD_LENGTH, 5);
            }
        }

        for (int i = 0; i < TIntersectionOnGrid.length; i++) {
            if (TIntersectionOnGrid[i] != null) {
                g.setColor(Color.BLACK);
                if (TIntersectionOnGrid[i].direction.equals("east")) {
                    g.fillRect(TIntersectionOnGrid[i].getSection1StartX(), TIntersectionOnGrid[i].getSection1StartY(), TIntersectionOnGrid[i].SECTION_1_LENGTH, 5);
                    g.fillRect(TIntersectionOnGrid[i].getSection2StartX(), TIntersectionOnGrid[i].getSection2StartY(), 5, TIntersectionOnGrid[i].getSection2EndY() - TIntersectionOnGrid[i].getSection2StartY());
                } else if (TIntersectionOnGrid[i].direction.equals("west")) {
                    g.fillRect(TIntersectionOnGrid[i].getSection1StartX(), TIntersectionOnGrid[i].getSection1StartY(), -TIntersectionOnGrid[i].SECTION_1_LENGTH, 5);
                    g.fillRect(TIntersectionOnGrid[i].getSection2StartX(), TIntersectionOnGrid[i].getSection2StartY(), 5, TIntersectionOnGrid[i].getSection2EndY() - TIntersectionOnGrid[i].getSection2StartY());
                } else if (TIntersectionOnGrid[i].direction.equals("south")) {
                    g.fillRect(TIntersectionOnGrid[i].getSection1StartX(), TIntersectionOnGrid[i].getSection1StartY(), 5, TIntersectionOnGrid[i].SECTION_1_LENGTH);
                    g.fillRect(TIntersectionOnGrid[i].getSection2StartX(), TIntersectionOnGrid[i].getSection2StartY()-2, TIntersectionOnGrid[i].getSection2EndX() - TIntersectionOnGrid[i].getSection2StartX(), 5);
                } else {
                    g.fillRect(TIntersectionOnGrid[i].getSection1StartX(), TIntersectionOnGrid[i].getSection1StartY(), 5, -TIntersectionOnGrid[i].SECTION_1_LENGTH);
                    g.fillRect(TIntersectionOnGrid[i].getSection2StartX(), TIntersectionOnGrid[i].getSection2StartY()+2, TIntersectionOnGrid[i].getSection2EndX() - TIntersectionOnGrid[i].getSection2StartX(), 5);
                }
            }

        }
        for (int i = 0; i < crossIntersectionOnGrid.length; i++) {
            if(crossIntersectionOnGrid[i] != null){
                g.fillRect(crossIntersectionOnGrid[i].road1StartX, crossIntersectionOnGrid[i].road1StartY, crossIntersectionOnGrid[i].ROAD_LENGTH, 5);
                g.fillRect(crossIntersectionOnGrid[i].road2StartX-4, crossIntersectionOnGrid[i].road2StartY+6, 5, crossIntersectionOnGrid[i].ROAD_LENGTH);
            }
        }

        for (int i = 0; i < drawnCars.length; i++) {
            g.setColor(Color.RED);
            if (drawnCars[i] != null) {
                if (drawnCars[i].getDirection().equals("north")) {
                    g.fillRect(drawnCars[i].getCurrentXFront(), drawnCars[i].getCurrentYFront(), -2, (drawnCars[i].getCurrentYBack() - drawnCars[i].getCurrentYFront()));
                } else if (drawnCars[i].getDirection().equals("south")) {
                    g.fillRect(drawnCars[i].getCurrentXFront()-2, drawnCars[i].getCurrentYBack(), 2, (drawnCars[i].getCurrentYFront() - drawnCars[i].getCurrentYBack()));
                } else if (drawnCars[i].getDirection().equals("east")) {
                    g.fillRect(drawnCars[i].getCurrentXBack(), drawnCars[i].getCurrentYFront()+2, (drawnCars[i].getCurrentXFront() - drawnCars[i].getCurrentXBack()), 2);
                } else {
                    g.fillRect(drawnCars[i].getCurrentXFront(), drawnCars[i].getCurrentYFront()-2, (drawnCars[i].getCurrentXBack() - drawnCars[i].getCurrentXFront()), -2);
                }
            }
        }
    }

    public JSimulatorGrid(Road[] roadsToDraw, TIntersection[] TIntersections, CrossIntersection[] crossIntersections, Vehicle[] cars) {
        setSize(WIDTH, HEIGHT);
        roadsOnGrid = roadsToDraw;
        TIntersectionOnGrid = TIntersections;
        crossIntersectionOnGrid = crossIntersections;
        drawnCars = cars;
    }
}
