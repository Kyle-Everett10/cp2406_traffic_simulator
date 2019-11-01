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
    String light;

    public CrossIntersection(String newName, int eastX, int eastY, String newLight){
        roadName = newName;
        road1StartX = eastX;
        road1StartY = eastY;
        road1EndX = road1StartX + ROAD_LENGTH;
        road1EndY = road1StartY;
        road2StartX = road1StartX + (ROAD_LENGTH/2);
        road2StartY = road1StartY - (ROAD_LENGTH/2);
        road2EndX = road2StartX;
        road2EndY = road2StartY + ROAD_LENGTH;
        westRoadX = road1StartX + 21;
        westRoadY = road1StartY + 2;
        eastRoadX = westRoadX + 5;
        eastRoadY = road1StartY - 2;
        northRoadX = road2StartX + 2;
        northRoadY = road2StartY + 21;
        southRoadX = road2StartX - 2;
        southRoadY = northRoadY + 5;
        light = newLight;
    }

    public int getSouthSnapX(){
        return northRoadX;
    }

    public int getSouthSnapY(){
        return southRoadY;
    }

    public int getNorthSnapX(){
        return southRoadX;
    }

    public int getNorthSnapY(){
        return northRoadY;
    }

    public int getEastSnapX(){
        return eastRoadX;
    }

    public int getEastSnapY(){
        return westRoadY;
    }

    public int getWestSnapX(){
        return westRoadX;
    }

    public int getWestSnapY(){
        return eastRoadY;
    }

    public String getLight(){
        return light;
    }

    public void changeLight(String newLight){
        light = newLight;
    }

}
