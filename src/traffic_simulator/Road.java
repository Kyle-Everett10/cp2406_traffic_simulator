package traffic_simulator;

public class Road {
    final int ROAD_LENGTH = 36;
    String roadName;
    int roadEnd1X;//traffic_simulator.Road end X-Y coordinates measured from middle of road to keep vehicles on either side
    int roadEnd1Y;
    int roadEnd2X;
    int roadEnd2Y;
    String direction;
    public Road(String newName, int coordinateX, int coordinateY, String newDirection){
        roadName = newName;
        roadEnd1X = coordinateX;
        roadEnd1Y = coordinateY;
        direction = newDirection;
        if(direction.equals("north-south")){
            roadEnd2X = roadEnd1X;
            roadEnd2Y = roadEnd1Y + ROAD_LENGTH;
        } else {
            roadEnd2X = roadEnd1X + ROAD_LENGTH;
            roadEnd2Y = roadEnd1Y;
        }
    }

    public int getRoadEnd1X() {
        return roadEnd1X;
    }

    public int getRoadEnd1Y() {
        return roadEnd1Y;
    }

    public int getRoadEnd2X() {
        return roadEnd2X;
    }

    public int getRoadEnd2Y() {
        return roadEnd2Y;
    }

}
