package traffic_simulator;

public class CrossIntersection {
    final int ROAD_LENGTH = 36;
    String roadName;
    int road1StartX;
    int road1StartY;
    int road1EndX;
    int road1EndY;
    int road2StartX;
    int road2StartY;
    int road2EndX;
    int road2EndY;
    int westRoadX;
    int westRoadY;
    int eastRoadX;
    int eastRoadY;
    int northRoadX;
    int northRoadY;
    int southRoadX;
    int southRoadY;

    public CrossIntersection(int eastX, int eastY){
        road1StartX = eastX;
        road1StartY = eastY;
        road1EndX = road1StartX + ROAD_LENGTH;
        road1EndY = road1StartY;
        road2StartX = road1StartX + (ROAD_LENGTH/2);
        road2StartY = road1StartY + (ROAD_LENGTH/2);
        road2EndX = road2StartX;
        road2EndY = road2StartY - ROAD_LENGTH;
    }
}
