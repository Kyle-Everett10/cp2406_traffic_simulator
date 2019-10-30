package traffic_simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewRoadConstructor extends JFrame implements ActionListener {
    final int WIDTH = 250;
    final int HEIGHT = 250;
    String initialDirection;
    int roadType;
    Road[] currentRoads;
    TIntersection[] currentTIntersections;
    CrossIntersection[] currentCrossIntersections;
    JLabel test = new JLabel("Test complete");
    JTextField newRoadName = new JTextField("Road name");
    JTextField xCoOrdinate = new JTextField("X co-ordinate");
    JTextField yCoOrdinate = new JTextField("y co-ordinate");
    ButtonGroup group = new ButtonGroup();
    JCheckBox north = new JCheckBox("North");
    JCheckBox south = new JCheckBox("South");
    JCheckBox east = new JCheckBox("East");
    JCheckBox west = new JCheckBox("West");
    JButton creation = new JButton("Create road");
    JLabel blank = new JLabel("");
    String[] roadTypes = {"Road", "T Intersection", "Cross intersection"};
    JComboBox<String> cb = new JComboBox<String>(roadTypes);
    boolean creatingRoad = true;

    public NewRoadConstructor(Road[] roads, TIntersection[] tIntersections, CrossIntersection[] crossIntersections) {
        super("ROAD CONSTRUCTOR");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1));
        currentRoads = roads;
        currentTIntersections = tIntersections;
        currentCrossIntersections = crossIntersections;
        add(newRoadName);
        add(xCoOrdinate);
        add(yCoOrdinate);
        add(cb);
        group.add(north);
        group.add(south);
        group.add(east);
        group.add(west);
        add(north);
        add(south);
        add(east);
        add(west);
        add(creation);
        creation.addActionListener(this);
        north.addActionListener(this);
        south.addActionListener(this);
        east.addActionListener(this);
        west.addActionListener(this);
        cb.addActionListener(this);
    }

    public void addCross(String newDirection){
        CrossIntersection crossToAdd = new CrossIntersection(newRoadName.getText(), Integer.parseInt(xCoOrdinate.getText()), Integer.parseInt(yCoOrdinate.getText()), newDirection);
        if(validateCrossEnds(crossToAdd)){
            CrossIntersection[] tempArray = new CrossIntersection[currentCrossIntersections.length+1];
            for(int i = 0; i < currentCrossIntersections.length; i++){
                tempArray[i] = currentCrossIntersections[i];
            }
            tempArray[currentCrossIntersections.length] = crossToAdd;
            currentCrossIntersections = tempArray;
        }
    }

    public boolean validateCrossEnds(CrossIntersection judgingCross){
        boolean crossValidated = true;
        for(int i = 0; i < currentRoads.length; i++){
            for(int j = 1; j < 36 ; j++){
                if(inBetween(judgingCross.road1StartX+j, currentRoads[i].getRoadEnd1X(), currentRoads[i].getRoadEnd2X()) && inBetween(judgingCross.road1StartY+j, currentRoads[i].getRoadEnd1Y(), currentRoads[i].getRoadEnd2Y())){
                    crossValidated = false;
                }
            }
            if(crossValidated){
                if(currentRoads[i].direction.equals("north-south")){
                    if((judgingCross.eastRoadX == currentRoads[i].getRoadEnd1X() && judgingCross.eastRoadY == currentRoads[i].getRoadEnd1Y()) || (judgingCross.eastRoadX == currentRoads[i].getRoadEnd2X() && judgingCross.eastRoadY == currentRoads[i].getRoadEnd2Y()) || (judgingCross.westRoadX == currentRoads[i].getRoadEnd1X() && judgingCross.westRoadY == currentRoads[i].getRoadEnd1Y()) || (judgingCross.westRoadX == currentRoads[i].getRoadEnd2X() && judgingCross.westRoadY == currentRoads[i].getRoadEnd2Y())){
                        crossValidated = false;
                    }
                }
            }
        }
        if(crossValidated){
            for(int i = 0; i < currentCrossIntersections.length ; i++){
                if(currentCrossIntersections[i] != null){
                    for(int j = 1; j < 36; j++){
                        if(inBetween(judgingCross.road1StartX+j, currentCrossIntersections[i].road1StartX, currentCrossIntersections[i].road1EndX) && inBetween(judgingCross.road2StartY + j, currentCrossIntersections[i].road2StartY, currentCrossIntersections[i].road2EndY)){
                            crossValidated = false;
                        }
                    }
                }
            }
        }
        return crossValidated;
    }

    public void addRoad(String newDirection) {
        String direction;
        if (newDirection.equals("north") || newDirection.equals("south")) {
            direction = "north-south";
        } else {
            direction = "west-east";
        }
        Road roadToAdd = new Road(newRoadName.getText(), Integer.parseInt(xCoOrdinate.getText()), Integer.parseInt(yCoOrdinate.getText()), direction);
        if (validateRoadEnds(roadToAdd)) {
            Road[] tempArray = new Road[currentRoads.length + 1];
            for (int i = 0; i < currentRoads.length; i++) {
                tempArray[i] = currentRoads[i];
            }
            tempArray[currentRoads.length] = roadToAdd;
            currentRoads = tempArray;
            creatingRoad = false;
            creatingRoad = true;
        }
    }

    public boolean validateRoadEnds(Road judgingRoad) {
        boolean roadValidated = true;
        for (int i = 1; i < currentRoads.length; i++) {
            for (int j = 0; j < 36; j++) {
                if ((inBetween(judgingRoad.getRoadEnd1X() + j, currentRoads[i].getRoadEnd1X(),
                        currentRoads[i].getRoadEnd2X()) && inBetween(judgingRoad.getRoadEnd1Y(),
                        currentRoads[i].getRoadEnd1Y(), currentRoads[i].getRoadEnd2Y())) ||
                        (inBetween(judgingRoad.getRoadEnd1X(), currentRoads[i].getRoadEnd1X(),
                                currentRoads[i].getRoadEnd2X()) && inBetween(judgingRoad.getRoadEnd1Y() + j,
                                currentRoads[i].getRoadEnd1Y(), currentRoads[i].getRoadEnd2Y()))) {
                    roadValidated = false;
                }
            }
            if (roadValidated && ((judgingRoad.getRoadEnd1X() == currentRoads[i].getRoadEnd1X() ||
                    judgingRoad.getRoadEnd1X() == currentRoads[i].getRoadEnd2X() ||
                    judgingRoad.getRoadEnd2X() == currentRoads[i].getRoadEnd1X() ||
                    judgingRoad.getRoadEnd2X() == currentRoads[i].getRoadEnd2X()) &&
                    !judgingRoad.direction.equals(currentRoads[i].direction) &&
                    (judgingRoad.getRoadEnd1Y() == currentRoads[i].getRoadEnd1Y() ||
                            judgingRoad.getRoadEnd1Y() == currentRoads[i].getRoadEnd2Y() ||
                            judgingRoad.getRoadEnd2Y() == currentRoads[i].getRoadEnd1Y() ||
                            judgingRoad.getRoadEnd2Y() == currentRoads[i].getRoadEnd2Y()))) {
                roadValidated = false;
            } else if (roadValidated && ((judgingRoad.getRoadEnd1Y() == currentRoads[i].getRoadEnd1Y() ||
                    judgingRoad.getRoadEnd1Y() == currentRoads[i].getRoadEnd2Y() ||
                    judgingRoad.getRoadEnd2Y() == currentRoads[i].getRoadEnd1Y() ||
                    judgingRoad.getRoadEnd2Y() == currentRoads[i].getRoadEnd2X()) &&
                    !judgingRoad.direction.equals(currentRoads[i].direction) &&
                    (judgingRoad.getRoadEnd1X() == currentRoads[i].getRoadEnd1X() ||
                            judgingRoad.getRoadEnd1X() == currentRoads[i].getRoadEnd2X() ||
                            judgingRoad.getRoadEnd2X() == currentRoads[i].getRoadEnd1X() ||
                            judgingRoad.getRoadEnd2X() == currentRoads[i].getRoadEnd2X()))) {
                roadValidated = false;
            }
        }
        if (roadValidated) {
            for (int i = 0; i < currentCrossIntersections.length; i++) {
                for (int j = 0; j < 36; j++) {
                    if (currentCrossIntersections[i] != null) {
                        if ((inBetween(judgingRoad.getRoadEnd1X() + j, currentCrossIntersections[i].road1StartX, currentCrossIntersections[i].road1EndX) && inBetween(judgingRoad.getRoadEnd1Y(), currentCrossIntersections[i].road2StartY, currentCrossIntersections[i].road2EndY)) || (inBetween(judgingRoad.getRoadEnd1X(), currentCrossIntersections[i].road1StartX, currentCrossIntersections[i].road1EndX) && inBetween(judgingRoad.getRoadEnd1Y() + j, currentCrossIntersections[i].road2StartY, currentCrossIntersections[i].road2EndY))) {
                            roadValidated = false;
                        }
                    }
                }
                if (currentCrossIntersections[i] != null) {
                    if (roadValidated && ((judgingRoad.getRoadEnd2X() == currentCrossIntersections[i].road1StartX || judgingRoad.getRoadEnd1X() == currentCrossIntersections[i].road1EndX) && !judgingRoad.direction.equals("west-east") && (judgingRoad.getRoadEnd1Y() == currentCrossIntersections[i].road2StartY))) {
                        roadValidated = false;
                    } else if (roadValidated && ((judgingRoad.getRoadEnd2Y() == currentCrossIntersections[i].road1StartY || judgingRoad.getRoadEnd1Y() == currentCrossIntersections[i].road1EndY) && !judgingRoad.direction.equals("north-south") && (judgingRoad.getRoadEnd1X() == currentCrossIntersections[i].road2StartX))) {
                        roadValidated = false;
                    }

                }
            }
        }
        return roadValidated;
    }

    public boolean inBetween(int value, int min, int max) {
        boolean isInBetween = true;
        if (value < min || value > max) {
            isInBetween = false;
        }
        return isInBetween;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == creation) {
            if (roadType == 0) {
                addRoad(initialDirection);
            } else if(roadType == 2){
                addCross(initialDirection);
            }
        } else if (e.getSource() == north) {
            initialDirection = "north";
        } else if (e.getSource() == south) {
            initialDirection = "south";
        } else if (e.getSource() == east) {
            initialDirection = "east";
        } else if (e.getSource() == west) {
            initialDirection = "west";
        } else {
            roadType = cb.getSelectedIndex();
        }
    }
}
