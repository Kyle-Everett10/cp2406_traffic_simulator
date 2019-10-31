package traffic_simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SimulatorGUI extends JFrame implements ActionListener {
    Road road1Test = new Road("Road1", 0, 50, "west-east");
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
                    if (roadEnd1Empty && ((tIntersections[j].getSection1StartX() == roads[i].getRoadEnd1X() && tIntersections[j].getSection1StartY() == roads[i].getRoadEnd1Y()) || (tIntersections[j].getSection2StartX() == roads[i].getRoadEnd1X() && tIntersections[j].getSection2StartY() == roads[j].getRoadEnd1Y()) || (tIntersections[j].section2EndX == roads[i].getRoadEnd1X() && tIntersections[j].section2EndY == roads[i].getRoadEnd1Y()))) {
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
                message.append("Road name: ").append(roads[i].roadName).append(", x: ").append(roads[i].getRoadEnd1X()).append(", y: ").append(roads[i].getRoadEnd1Y()).append("\n To build on this part of road, enter x: ").append(xBuilder).append(", y: ").append(yBuilder).append(" with direction: ").append(roads[i].direction).append("\n");
            }
            if(roadEnd2Empty){
                message.append("Road name: ").append(roads[i].roadName).append(", x: ").append(roads[i].getRoadEnd1X()).append(", y: ").append(roads[i].getRoadEnd1Y()).append("\n To build on this part of road, enter x: ").append(roads[i].getRoadEnd2X()).append(", y: ").append(roads[i].getRoadEnd2Y()).append(" with direction: ").append(roads[i].direction).append("\n");
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
                            if (vehicles[i].getCurrentXFront() == cross[j].northRoadX && vehicles[i].getCurrentYFront() == cross[j].northRoadX) {
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
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle2Y, "west");
                                            break;
                                        case "south":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle2Y, "east");
                                            break;
                                        case "east":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle2Y, "north");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle2X, intersectionCarIsOn.middle2Y, "south");
                                    }
                                } else {
                                    switch (vehicles[i].getDirection()) {
                                        case "north":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle1Y, "east");
                                            break;
                                        case "south":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle1Y, "west");
                                            break;
                                        case "east":
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle1Y, "south");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(intersectionCarIsOn.middle1X, intersectionCarIsOn.middle1Y, "north");
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
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getNorthSnapX() + 2, crossCarIsOn.getNorthSnapY(), "north");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getSouthSnapX() - 2, crossCarIsOn.getSouthSnapY(), "south");
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
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getSouthSnapX() - 2, crossCarIsOn.getSouthSnapY(), "south");
                                            break;
                                        default:
                                            vehicles[i].snapOnIntersection(crossCarIsOn.getNorthSnapX() + 2, crossCarIsOn.getNorthSnapY(), "north");
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
                                    if ((car[k].getCurrentYBack() - 3 <= roads[j].getRoadEnd1Y() && roads[j].direction.equals("north-south") && car[k].getCurrentXFront() - 2 == roads[j].getRoadEnd1X()) || (car[k].getCurrentXBack() - 3 <= roads[j].getRoadEnd1X() && roads[j].direction.equals("west-east") && car[k].getCurrentYFront() - 2 == roads[j].getRoadEnd1Y())) {
                                        canSpawn = false;
                                    }
                                } else if (i == 0) {
                                    if (roads[j].direction.equals("north-south")) {
                                        spawnDirection = "north";
                                        spawnX = roads[j].getRoadEnd1X() - 2;
                                        spawnY = roads[j].getRoadEnd1Y();
                                    } else {
                                        spawnDirection = "east";
                                        spawnX = roads[j].getRoadEnd1X();
                                        spawnY = roads[j].getRoadEnd1Y() + 2;
                                    }
                                    spawningRoad = true;
                                    foundRoad = roads[j];
                                    canSpawn = false;
                                }
                            }
                            if (canSpawn) {
                                if (roads[j].direction.equals("north-south")) {
                                    spawnDirection = "south";
                                    spawnX = roads[j].getRoadEnd1X() - 2;
                                    spawnY = roads[j].getRoadEnd1Y();
                                } else {
                                    spawnDirection = "east";
                                    spawnX = roads[j].getRoadEnd1X();
                                    spawnY = roads[j].getRoadEnd1Y() + 2;
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
