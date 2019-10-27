package traffic_simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulatorGUI extends JFrame implements ActionListener {
    Road road1Test = new Road("Road1", 0, 50, "east");
    TIntersection tIntersection = new TIntersection("Intersection1", 36, 50, "east");
    CrossIntersection test2 = new CrossIntersection("Test2", 53, 85, "east");
    Road[] roads = {road1Test};
    TIntersection[] tIntersections = {tIntersection};
    CrossIntersection[] cross = {test2};
    JSimulatorGrid map = new JSimulatorGrid(roads, tIntersections, cross);
    JPanel fill = new JPanel();
    JButton newRoad = new JButton("Create a new road");
    NewRoadConstructor testing1 = new NewRoadConstructor(roads, tIntersections, cross);
    JLabel statusBar = new JLabel("");
    JButton repainter = new JButton("Repaint map");
    int WIDTH = 1000;
    int HEIGHT = 500;

    public SimulatorGUI(){
        super("Simulator");
        setLayout(new GridLayout(1, 2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        add(fill);
        fill.add(newRoad);
        fill.add(statusBar);
        fill.add(repainter);
        add(map);
        newRoad.addActionListener(this);
        repainter.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newRoad){
            testing1.setVisible(true);
        } else if (e.getSource() == repainter){
            roads = testing1.currentRoads;
            remove(map);
            map = new JSimulatorGrid(roads, tIntersections, cross);
            add(map);
            validate();
            repaint();
        }
    }

    public static void main(String[] args){
        SimulatorGUI test = new SimulatorGUI();
        test.setVisible(true);
    }
}
