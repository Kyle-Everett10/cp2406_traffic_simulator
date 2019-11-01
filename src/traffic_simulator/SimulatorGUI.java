package traffic_simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SimulatorGUI extends JFrame implements ActionListener {
    Road road1Test = new Road("Road1", 0, 250, "west-east");
    Road[] roads = {road1Test};
    TIntersection[] tIntersections = new TIntersection[10];
    CrossIntersection[] cross = new CrossIntersection[10];
    int spawnRate = 4;
    Vehicle[] vehicles = new Vehicle[spawnRate];
    JSimulatorGrid map = new JSimulatorGrid(roads, tIntersections, cross, vehicles);
    JPanel fill = new JPanel();
    JButton newRoad = new JButton("Create a new road");
    NewRoadConstructor testing1 = new NewRoadConstructor(roads, tIntersections, cross);
    JLabel statusBar = new JLabel("");
    JButton repainter = new JButton("Repaint map");
    JButton startSimulation = new JButton("Start Simulation");
    JButton saveMap = new JButton("Save current map");
    JButton loadMap = new JButton("Load map");
    JButton simulationOptions = new JButton("Simulation options");
    JButton viewRoadEnds = new JButton("View the road ends by co-ordinate");
    Random rand = new Random();
    Timer timer = new Timer(100, this);
    ArrayList<Integer> xEnds = new ArrayList<>();
    ArrayList<Integer> yEnds = new ArrayList<>();
    final double ONE_DAY = 50;
    double time = 0.0;
    int WIDTH = 1000;
    int HEIGHT = 500;

    public SimulatorGUI() {
        super("Simulator");
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        add(fill);
        fill.setLayout(new GridLayout(4, 2));
        fill.add(newRoad);
        fill.add(repainter);
        fill.add(startSimulation);
        fill.add(simulationOptions);
        fill.add(saveMap);
        fill.add(loadMap);
        fill.add(viewRoadEnds);
        fill.add(statusBar);
        add(map);
        newRoad.addActionListener(this);
        repainter.addActionListener(this);
        startSimulation.addActionListener(this);
        loadMap.addActionListener(this);
        viewRoadEnds.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newRoad) {
            testing1.setVisible(true);
        } else if (e.getSource() == repainter) {
            roads = testing1.currentRoads;
            tIntersections = testing1.currentTIntersections;
            cross = testing1.currentCrossIntersections;
            remove(map);
            map = new JSimulatorGrid(roads, tIntersections, cross, vehicles);
            add(map);
            validate();
            repaint();
        } else if (e.getSource() == startSimulation) {
            time = 0.0;
            runSimulation();
        } else if (e.getSource() == timer) {
            vehicles = spawnCars(vehicles);
            driveCars();
            time += 0.1;
            remove(map);
            map = new JSimulatorGrid(roads, tIntersections, cross, vehicles);
            add(map);
            validate();
            repaint();
            time += 0.001;
            if (time >= ONE_DAY) {
                timer.stop();
            }
        } else if (e.getSource() == loadMap) {
            loadFile();
        } else if (e.getSource() == viewRoadEnds) {
            roadEnder();
        }
    }

    public void roadEnder() {
        StringBuilder message = new StringBuilder("These roads are available to be added onto:\n");
        xEnds = new ArrayList<>();
        yEnds = new ArrayList<>();
        for (int i = 0; i < roads.length; i++) {
            boolean roadEnd1Empty = true;
            boolean roadEnd2Empty = true;
            for (int j = 0; j < roads.length; j++) {
                if (roads[j] != roads[i] && roads[j] != null) {
                    if (roads[j].getRoadEnd1X() == roads[i].getRoadEnd2X() && roads[j].getRoadEnd1Y() == roads[i].getRoadEnd2Y()) {
                        roadEnd2Empty = false;
                    }
                    if (roads[j].getRoadEnd2X() == roads[i].getRoadEnd1X() && roads[j].getRoadEnd2Y() == roads[i].getRoadEnd1Y()) {
                        roadEnd1Empty = false;
                    }
                }
            }
            for (int j = 0; j < cross.length; j++) {
                if (cross[j] != null) {
                    if (roadEnd1Empty && ((cross[j].road1EndX == roads[i].getRoadEnd1X() && cross[j].road1EndY == roads[i].getRoadEnd1Y()) || (cross[j].road2EndX == roads[i].getRoadEnd1X() && cross[j].road2EndY == roads[i].getRoadEnd1Y()))) {
                        roadEnd1Empty = false;
                    }
                    if (roadEnd2Empty && ((cross[j].road1StartX == roads[i].getRoadEnd2X() && cross[j].road1StartY == roads[i].getRoadEnd2Y()) || (cross[j].road2StartX == roads[i].getRoadEnd2X() && cross[j].road2StartY == roads[i].getRoadEnd2Y()))) {
                        roadEnd2Empty = false;
                    }
                }
            }
            for (int j = 0; j < tIntersections.length; j++) {
                if (tIntersections[j] != null) {
                    if (roadEnd1Empty && ((tIntersections[j].getSection1StartX() == roads[i].getRoadEnd1X() && tIntersections[j].getSection1StartY() == roads[i].getRoadEnd1Y()) || (tIntersections[j].getSection2StartX() == roads[i].getRoadEnd1X() && tIntersections[j].getSection2StartY() == roads[i].getRoadEnd1Y()) || (tIntersections[j].section2EndX == roads[i].getRoadEnd1X() && tIntersections[j].section2EndY == roads[i].getRoadEnd1Y()))) {
                        roadEnd1Empty = false;
                    }
                    if (roadEnd2Empty && ((tIntersections[j].getSection1StartX() == roads[i].getRoadEnd2X() && tIntersections[j].getSection1StartY() == roads[i].getRoadEnd2Y()) || (tIntersections[j].getSection2StartX() == roads[i].getRoadEnd2X() && tIntersections[j].getSection2StartY() == roads[j].getRoadEnd2Y()) || (tIntersections[j].section2EndX == roads[i].getRoadEnd2X() && tIntersections[j].section2EndY == roads[i].getRoadEnd2Y()))) {
                        roadEnd2Empty = false;
                    }
                }
            }
            if (roadEnd1Empty) {
                int xBuilder;
                int yBuilder;
                if (roads[i].direction.equals("north-south")) {
                    xBuilder = roads[i].getRoadEnd1X();
                    yBuilder = roads[i].getRoadEnd1Y() - 36;
                } else {
                    xBuilder = roads[i].getRoadEnd1X() - 36;
                    yBuilder = roads[i].getRoadEnd1Y();
                }
                xEnds.add(xBuilder);
                yEnds.add(yBuilder);
                message.append("Road name: ").append(roads[i].roadName).append(", x: ").append(roads[i].getRoadEnd1X()).append(", y: ").append(roads[i].getRoadEnd1Y()).append("\n To build on this part of road, enter x: ").append(xBuilder).append(", y: ").append(yBuilder).append(" with direction: ").append(roads[i].direction).append("\n");
            }
            if (roadEnd2Empty) {
                xEnds.add(roads[i].getRoadEnd2X());
                yEnds.add(roads[i].getRoadEnd2Y());
                message.append("Road name: ").append(roads[i].roadName).append(", x: ").append(roads[i].getRoadEnd1X()).append(", y: ").append(roads[i].getRoadEnd1Y()).append("\n To build on this part of road, enter x: ").append(roads[i].getRoadEnd2X()).append(", y: ").append(roads[i].getRoadEnd2Y()).append(" with direction: ").append(roads[i].direction).append("\n");
            }
        }
        for (int i = 0; i < cross.length; i++) {
            if (cross[i] != null) {
                boolean northFree = true;
                boolean southFree = true;
                boolean eastFree = true;
                boolean westFree = true;
                for (int j = 0; j < roads.length; j++) {
                    if (cross[i].road2StartX == roads[j].getRoadEnd2X() && cross[i].road2StartY == roads[j].getRoadEnd2Y()) {
                        northFree = false;
                    }
                    if (cross[i].road2EndX == roads[j].getRoadEnd1X() && cross[i].road2EndY == roads[j].getRoadEnd1Y()) {
                        southFree = false;
                    }
                    if (cross[i].road1StartX == roads[j].getRoadEnd2X() && cross[i].road1StartY == roads[j].getRoadEnd2Y()) {
                        westFree = false;
                    }
                    if (cross[i].road1EndX == roads[j].getRoadEnd1X() && cross[i].road1EndY == roads[j].getRoadEnd1Y()) {
                        eastFree = false;
                    }
                }
                for (int j = 0; j < cross.length; j++) {
                    if (cross[j] != cross[i] && cross[j] != null) {
                        if (northFree && cross[j].road2EndX == cross[i].road2StartX && cross[j].road2EndY == cross[i].road2StartY) {
                            northFree = false;
                        }
                        if (southFree && cross[j].road2StartX == cross[i].road2EndX && cross[j].road2StartY == cross[i].road2EndY) {
                            southFree = false;
                        }
                        if (westFree && cross[j].road1EndX == cross[i].road1StartX && cross[j].road1EndY == cross[i].road1StartY) {
                            westFree = false;
                        }
                        if (eastFree && cross[j].road1StartX == cross[i].road1EndX && cross[j].road1StartY == cross[i].road1EndY) {
                            eastFree = false;
                        }
                    }
                }
                for (int j = 0; j < tIntersections.length; j++) {
                    if (tIntersections[j] != null) {
                        if (northFree && ((tIntersections[j].section1StartX == cross[i].road2StartX && tIntersections[j].section1StartY == cross[i].road2StartY) || (tIntersections[j].section2StartX == cross[i].road2StartX && tIntersections[j].section2StartY == cross[i].road2StartY) || (tIntersections[j].section2EndX == cross[i].road2StartX && tIntersections[j].section2EndY == cross[i].road2StartY))) {
                            northFree = false;
                        }
                        if (southFree && ((tIntersections[j].section1StartX == cross[i].road2EndX && tIntersections[j].section1StartY == cross[i].road2EndY) || (tIntersections[j].section2StartX == cross[i].road2EndX && tIntersections[j].section2StartY == cross[i].road2EndY) || (tIntersections[j].section2EndX == cross[i].road2EndX && tIntersections[j].section2EndY == cross[i].road2EndY))) {
                            southFree = false;
                        }
                        if (westFree && ((tIntersections[j].section1StartX == cross[i].road1StartX && tIntersections[j].section1StartY == cross[i].road1StartY) || (tIntersections[j].section2StartX == cross[i].road1StartX && tIntersections[j].section2StartY == cross[i].road1StartY) || (tIntersections[j].section2EndX == cross[i].road1StartX && tIntersections[j].section2EndY == cross[i].road1StartY))) {
                            westFree = false;
                        }
                        if (eastFree && ((tIntersections[j].section1StartX == cross[i].road1EndX && tIntersections[j].section1StartY == cross[i].road1EndY) || (tIntersections[j].section2StartX == cross[i].road1EndX && tIntersections[j].section2StartY == cross[i].road1EndY) || (tIntersections[j].section2EndX == cross[i].road1EndX && tIntersections[j].section2EndY == cross[i].road1EndY))) {
                            eastFree = false;
                        }
                    }
                }
                if (northFree) {
                    xEnds.add(cross[i].road2StartX);
                    yEnds.add(cross[i].road2StartY);
                    message.append("Road name: ").append(cross[i].roadName).append(" north, x: ").append(cross[i].road2StartX).append(", y: ").append(cross[i].road2StartY).append("\n To build, make sure direction is north\n");
                }
                if (southFree) {
                    xEnds.add(cross[i].road2EndX);
                    yEnds.add(cross[i].road2EndY);
                    message.append("Road name: ").append(cross[i].roadName).append(" south, x: ").append(cross[i].road2EndX).append(", y: ").append(cross[i].road2EndY).append("\n To build, make sure direction is south\n");
                }
                if (westFree) {
                    xEnds.add(cross[i].road1StartX);
                    yEnds.add(cross[i].road1StartY);
                    message.append("Road name: ").append(cross[i].roadName).append(" west, x: ").append(cross[i].road1StartX).append(", y: ").append(cross[i].road1StartY).append("\n To build, make sure direction is west\n");
                }
                if (eastFree) {
                    xEnds.add(cross[i].road1EndX);
                    yEnds.add(cross[i].road1EndY);
                    message.append("Road name: ").append(cross[i].roadName).append(" east, x: ").append(cross[i].road1EndX).append(", y: ").append(cross[i].road1EndY).append("\n To build, make sure direction is east\n");
                }
            }
        }
        for (int i = 0; i < tIntersections.length; i++) {
            if (tIntersections[i] != null) {
                boolean road1EndEmpty = true;
                boolean road2StartEmpty = true;
                boolean road2EndEmpty = true;
                for (int j = 0; j < roads.length; j++) {
                    if ((tIntersections[i].section1StartX == roads[j].roadEnd1X && tIntersections[i].section1StartY == roads[j].roadEnd1Y) || (tIntersections[i].section1StartX == roads[j].roadEnd2X && tIntersections[i].section1StartY == roads[j].roadEnd2Y)) {
                        road1EndEmpty = false;
                    }
                    if ((tIntersections[i].section2StartX == roads[j].roadEnd1X && tIntersections[i].section2StartY == roads[j].roadEnd1Y) || (tIntersections[i].section2StartX == roads[j].roadEnd2X && tIntersections[i].section2StartY == roads[j].roadEnd2Y)) {
                        road2StartEmpty = false;
                    }
                    if ((tIntersections[i].section2EndX == roads[j].roadEnd1X && tIntersections[i].section2EndY == roads[j].roadEnd1Y) || (tIntersections[i].section2EndX == roads[j].roadEnd2X && tIntersections[i].section2EndY == roads[j].roadEnd2Y)) {
                        road2EndEmpty = false;
                    }
                }
                for (int j = 0; j < cross.length; j++) {
                    if (cross[j] != null) {
                        if ((tIntersections[i].section1StartX == cross[j].road1StartX && tIntersections[i].section1StartY == cross[j].road1StartY) || (tIntersections[i].section1StartX == cross[j].road1EndX && tIntersections[i].section1StartY == cross[j].road1EndY) || (tIntersections[i].section1StartX == cross[j].road2StartX && tIntersections[i].section1StartY == cross[j].road2StartY) || (tIntersections[i].section1StartX == cross[j].road2EndX && tIntersections[i].section1StartY == cross[j].road2EndY)) {
                            road1EndEmpty = false;
                        }
                        if ((tIntersections[i].section2StartX == cross[j].road1StartX && tIntersections[i].section2StartY == cross[j].road1StartY) || (tIntersections[i].section2StartX == cross[j].road1EndX && tIntersections[i].section2StartY == cross[j].road1EndY) || (tIntersections[i].section2StartX == cross[j].road2StartX && tIntersections[i].section2StartY == cross[j].road2StartY) || (tIntersections[i].section2StartX == cross[j].road2EndX && tIntersections[i].section2StartY == cross[j].road2EndY)) {
                            road2StartEmpty = false;
                        }
                        if ((tIntersections[i].section2EndX == cross[j].road1StartX && tIntersections[i].section2EndY == cross[j].road1StartY) || (tIntersections[i].section2EndX == cross[j].road1EndX && tIntersections[i].section2EndY == cross[j].road1EndY) || (tIntersections[i].section2EndX == cross[j].road2StartX && tIntersections[i].section2EndY == cross[j].road2StartY) || (tIntersections[i].section2EndX == cross[j].road2EndX && tIntersections[i].section2EndY == cross[j].road2EndY)) {
                            road2EndEmpty = false;
                        }
                    }
                }
                for (int j = 0; j < tIntersections.length; j++) {
                    if (tIntersections[j] != tIntersections[i] && tIntersections[j] != null) {
                        if ((tIntersections[i].section1StartX == tIntersections[j].section1StartX && tIntersections[i].section1StartY == tIntersections[j].section1StartY) || (tIntersections[i].section1StartX == tIntersections[j].section2StartX && tIntersections[i].section1StartY == tIntersections[j].section2StartY) || (tIntersections[i].section1StartX == tIntersections[j].section2EndX && tIntersections[i].section1StartY == tIntersections[j].section2EndY)) {
                            road1EndEmpty = false;
                        }
                        if ((tIntersections[i].section2StartX == tIntersections[j].section1StartX && tIntersections[i].section2StartY == tIntersections[j].section1StartY) || (tIntersections[i].section2StartX == tIntersections[j].section2StartX && tIntersections[i].section2StartY == tIntersections[j].section2StartY) || (tIntersections[i].section2StartX == tIntersections[j].section2EndX && tIntersections[i].section2StartY == tIntersections[j].section2EndY)) {
                            road2StartEmpty = false;
                        }
                        if ((tIntersections[i].section2EndX == tIntersections[j].section1StartX && tIntersections[i].section2EndY == tIntersections[j].section1StartY) || (tIntersections[i].section2EndX == tIntersections[j].section2StartX && tIntersections[i].section2EndY == tIntersections[j].section2StartY) || (tIntersections[i].section2EndX == tIntersections[j].section2EndX && tIntersections[i].section2EndY == tIntersections[j].section2EndY)) {
                            road2EndEmpty = false;
                        }
                    }
                }
                String left;
                String right;
                if (tIntersections[i].direction.equals("north")) {
                    left = "west";
                    right = "east";
                } else if (tIntersections[i].direction.equals("south")) {
                    left = "east";
                    right = "west";
                } else if (tIntersections[i].direction.equals("east")) {
                    left = "south";
                    right = "north";
                } else {
                    left = "north";
                    right = "south";
                }
                if (road1EndEmpty) {
                    xEnds.add(tIntersections[i].section1StartX);
                    yEnds.add(tIntersections[i].section1StartY);
                    message.append("Road name: ").append(tIntersections[i].intersectionName).append(" middle, x: ").append(tIntersections[i].section1StartX).append(", y: ").append(tIntersections[i].section1StartY).append("\n To build, make sure direction is ").append(tIntersections[i].direction).append("\n");
                }
                if (road2StartEmpty) {
                    xEnds.add(tIntersections[i].middle1X+1);
                    yEnds.add(tIntersections[i].section2StartY);
                    message.append("Road name: ").append(tIntersections[i].intersectionName).append(" right, x: ").append(tIntersections[i].section2EndX+1).append(", y: ").append(tIntersections[i].section2StartY).append("\n To build, make sure direction is ").append(left).append("\n");
                }
                if (road2EndEmpty) {
                    xEnds.add(tIntersections[i].section2EndX);
                    yEnds.add(tIntersections[i].section2EndY);
                    message.append("Road name: ").append(tIntersections[i].intersectionName).append(" left, x: ").append(tIntersections[i].section2EndX+1).append(", y: ").append(tIntersections[i].section2EndY).append("\n To build, make sure direction is ").append(right).append("\n");
                }
            }
        }
        JOptionPane.showMessageDialog(null, message.toString(), "message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void runSimulation() {
        timer.start();
    }

    public void driveCars() {
        for (int i = 0; i < vehicles.length; i++) {
            if (vehicles[i] != null) {
                boolean noCollision = true;
                boolean onIntersection = false;
                boolean onTIntersection = false;
                CrossIntersection crossCarIsOn = new CrossIntersection("blank", 0, 0, "north");
                TIntersection intersectionCarIsOn = new TIntersection("blank2", 0, 0, "north");
                for (int j = 0; j < vehicles.length; j++) {
                    if (vehicles[j] != vehicles[i] && vehicles[j] != null) {
                        if (!vehicles[i].collisionDetection(vehicles[j])) {
                            noCollision = false;
                        }
                    }
                }
                for (int j = 0; j < cross.length; j++) {
                    if (cross[j] != null) {
                        if (vehicles[i].getDirection().equals("north")) {
                            if (vehicles[i].getCurrentXFront() == cross[j].southRoadX && vehicles[i].getCurrentYFront() == cross[j].southRoadY) {
                                onIntersection = true;
                                crossCarIsOn = cross[j];
                            }
                        } else if (vehicles[i].getDirection().equals("south")) {
                            if (vehicles[i].getCurrentXFront() == cross[j].northRoadX && vehicles[i].getCurrentYFront() == cross[j].northRoadY) {
                                onIntersection = true;
                                crossCarIsOn = cross[j];
                            }
                        } else if (vehicles[i].getDirection().equals("east")) {
                            if (vehicles[i].getCurrentXFront() == cross[j].westRoadX && vehicles[i].getCurrentYFront() == cross[j].westRoadY) {
                                onIntersection = true;
                                crossCarIsOn = cross[j];
                            }
                        } else {
                            if (vehicles[i].getCurrentXFront() == cross[j].eastRoadX && vehicles[i].getCurrentYFront() == cross[j].eastRoadY) {
                                onIntersection = true;
                                crossCarIsOn = cross[j];
                            }
                        }
                    }
                }
                if (!onIntersection) {
                    for (int j = 0; j < tIntersections.length; j++) {
                        if (tIntersections[j] != null) {
                            if (vehicles[i].getDirection().equals(tIntersections[j].direction)) {
                                switch (vehicles[i].getDirection()) {
                                    case "north":
                                        if (vehicles[i].getCurrentXFront() == tIntersections[j].getSection1EndX() - 2 && vehicles[i].getCurrentYFront() == tIntersections[j].getSection1EndY()) {
                                            onTIntersection = true;
                                            intersectionCarIsOn = tIntersections[j];
                                        }
                                        break;
                                    case "south":
                                        if (vehicles[i].getCurrentXFront() == tIntersections[j].getSection1EndX() + 2 && vehicles[i].getCurrentYFront() == tIntersections[j].getSection1EndY()) {
                                            onTIntersection = true;
                                            intersectionCarIsOn = tIntersections[j];
                                        }
                                        break;
                                    case "east":
                                        if (vehicles[i].getCurrentYFront() == tIntersections[j].getSection1EndY() + 2 && vehicles[i].getCurrentXFront() == tIntersections[j].getSection1EndX()) {
                                            onTIntersection = true;
                                            intersectionCarIsOn = tIntersections[j];
                                        }
                                        break;
                                    default:
                                        if (vehicles[i].getCurrentYFront() == tIntersections[j].getSection1EndY() - 2 && vehicles[i].getCurrentXFront() == tIntersections[j].getSection1EndX()) {
                                            onTIntersection = true;
                                            intersectionCarIsOn = tIntersections[j];
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
                if (noCollision) {
                    if (!onIntersection && !onTIntersection) {
                        vehicles[i].drive();
                    } else {
                        if (onTIntersection) {
                            int decision = rand.nextInt(2);
                            boolean freeIntersection = true;
                            for (int j = 0; j < vehicles.length; j++) {
                                if (!intersectionCarIsOn.isIntersectionFree(vehicles[j])) {
                                    freeIntersection = false;
                                }
                            }
                            if (freeIntersection) {
                                if (decision == 1) {
                                    switch (vehicles[i].getDirection()) {
                                        case "north":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle2Y, "west");
                                            break;
                                        case "south":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle2Y, "east");
                                            break;
                                        case "east":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle2Y, "north");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle2Y, "south");
                                    }
                                } else {
                                    switch (vehicles[i].getDirection()) {
                                        case "north":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle1Y, "east");
                                            break;
                                        case "south":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle1Y, "west");
                                            break;
                                        case "east":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle1Y, "south");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle1Y, "north");
                                    }
                                }
                            }
                        } else {
                            int decision = rand.nextInt(3);
                            if (crossCarIsOn.light.equals(vehicles[i].getDirection())) {
                                if (decision == 1) {
                                    switch (vehicles[i].getDirection()) {
                                        case "north":
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getWestSnapX(), crossCarIsOn.getWestSnapY(), "west");
                                            break;
                                        case "south":
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getEastSnapX(), crossCarIsOn.getEastSnapY(), "east");
                                            break;
                                        case "east":
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getNorthSnapX(), crossCarIsOn.getNorthSnapY(), "north");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getSouthSnapX(), crossCarIsOn.getSouthSnapY(), "south");
                                    }
                                } else if (decision == 2) {
                                    switch (vehicles[i].getDirection()) {
                                        case "north":
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getEastSnapX(), crossCarIsOn.getEastSnapY(), "east");
                                            break;
                                        case "south":
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getWestSnapX(), crossCarIsOn.getWestSnapY(), "west");
                                            break;
                                        case "east":
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getSouthSnapX(), crossCarIsOn.getSouthSnapY(), "south");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getNorthSnapX(), crossCarIsOn.getNorthSnapY(), "north");
                                    }
                                } else {
                                    vehicles[i].drive();
                                }
                            }
                        }
                    }
                }
                if (vehicles[i].getCurrentYFront() < 0 || vehicles[i].getCurrentYFront() > 500 || vehicles[i].getCurrentXFront() < 0 || vehicles[i].getCurrentXFront() > 500) {
                    vehicles[i] = null;
                }
                for(int j = 0; j < xEnds.toArray().length; j++){
                    if(xEnds.get(j) != null && vehicles[i] != null){
                        if(vehicles[i].getDirection().equals("east")){
                            if(vehicles[i].getCurrentXFront() == xEnds.get(j) && vehicles[i].getCurrentYFront()+2 == yEnds.get(j) && xEnds.get(j) != 0){
                                vehicles[i] = null;
                            }
                        } else if (vehicles[i].getDirection().equals("west")){
                            if(vehicles[i].getCurrentXFront() == xEnds.get(j) && vehicles[i].getCurrentYFront()+2 == yEnds.get(j) && xEnds.get(j) != 0){
                                vehicles[i] = null;
                            }
                        } else if (vehicles[i].getDirection().equals("north")){
                            if(vehicles[i].getCurrentXFront()+2 == xEnds.get(j) && vehicles[i].getCurrentYFront() == yEnds.get(j) && xEnds.get(j) != 0){
                                vehicles[i] = null;
                            }
                        } else {
                            if(vehicles[i].getCurrentXFront()-2 == xEnds.get(j) && vehicles[i].getCurrentYFront() == yEnds.get(j) && xEnds.get(j) != 0){
                                vehicles[i] = null;
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveFile() {

    }

    public void loadFile() {
        File folder = new File("savedMaps");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles.length > 0) {
            JOptionPane.showMessageDialog(null, "Test failed", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No files to load", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Vehicle[] spawnCars(Vehicle[] car) {
        for (int i = 0; i < car.length; i++) {
            if (car[i] == null) {
                int spawnX = 0;
                int spawnY = 0;
                String spawnDirection = "east";
                boolean spawningRoad = false;
                boolean canSpawn = true;
                Road foundRoad = null;
                for (int j = 0; j < roads.length; j++) {
                    if (foundRoad == null) {
                        if (roads[j].roadEnd1X == 0 || roads[j].roadEnd1Y == 0) {
                            for (int k = 0; k < car.length; k++) {
                                if (car[k] != null) {
                                    if  (car[k].getCurrentXBack() - 3 <= roads[j].getRoadEnd1X() && roads[j].direction.equals("west-east")){
                                        canSpawn = false;
                                    }
                                } else if (i == 0) {
                                    if (roads[j].direction.equals("north-south")) {
                                        spawnDirection = "south";
                                        spawnX = roads[j].getRoadEnd1X() + 2;
                                        spawnY = roads[j].getRoadEnd1Y();
                                    } else {
                                        spawnDirection = "east";
                                        spawnX = roads[j].getRoadEnd1X();
                                        spawnY = roads[j].getRoadEnd1Y() - 2;
                                    }
                                    spawningRoad = true;
                                    foundRoad = roads[j];
                                    canSpawn = false;
                                }
                            }
                            if (canSpawn) {
                                if (roads[j].direction.equals("north-south")) {
                                    spawnDirection = "south";
                                    spawnX = roads[j].getRoadEnd1X() + 2;
                                    spawnY = roads[j].getRoadEnd1Y();
                                } else {
                                    spawnDirection = "east";
                                    spawnX = roads[j].getRoadEnd1X();
                                    spawnY = roads[j].getRoadEnd1Y() - 2;
                                }
                                spawningRoad = true;
                                foundRoad = roads[j];
                            }
                        }
                    }
                    if (spawningRoad) {
                        car[i] = new Vehicle("Car", "car" + (i + 1), spawnX, spawnY, spawnDirection);
                    }
                }
            }
        }
        return car;
    }

    public static void main(String[] args) {
        SimulatorGUI test = new SimulatorGUI();
        test.setVisible(true);
    }
}
