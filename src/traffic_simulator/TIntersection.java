package traffic_simulator;

public class TIntersection {
    final int SECTION_1_LENGTH = 36;
    final int SECTION_2_LENGTH = 36;
    String intersectionName;
    int section1StartX;
    int section1StartY;
    int section1EndY;
    int section1EndX;
    int section2StartX;
    int section2StartY;
    int section2EndX;
    int section2EndY;
    int middle1X;
    int middle1Y;
    int middle2X;
    int middle2Y;
    String direction;

    public TIntersection(String newName, int section1BeginX, int section1BeginY, String newDirection){
        intersectionName = newName;
        section1StartX = section1BeginX;
        section1StartY = section1BeginY;
        direction = newDirection;
        if(direction.equals("north")){
            section1EndX = section1StartX;
            section1EndY = section1StartY + SECTION_1_LENGTH;
            section2StartX = section1EndX - 18;
            section2StartY = section1EndY + 3;
            section2EndX = section1EndX + 18;
            section2EndY = section1EndY + 3;
            middle1X = section1EndX - 2;
            middle1Y = section1EndY + 1;
            middle2X = section2EndX + 2;
            middle2Y = section2EndY + 4;
        } else if (direction.equals("south")){
            section1EndX = section1StartX;
            section1EndY = section1StartY - SECTION_1_LENGTH;
            section2StartX = section1EndX + 18;
            section2StartY = section1EndY - 3;
            section2EndX = section1EndX - 18;
            section2EndY = section1EndY - 3;
            middle1X = section1EndX + 2;
            middle1Y = section1EndY - 1;
            middle2X = section2EndX - 2;
            middle2Y = section2EndY - 4;
        } else if (direction.equals("east")){
            section1EndX = section1StartX + SECTION_1_LENGTH;
            section1EndY = section1StartY;
            section2StartX = section1EndX + 3;
            section2StartY = section1EndY - 18;
            section2EndX = section1EndX + 3;
            section2EndY = section1EndY + 18;
            middle1X = section1EndX + 1;
            middle1Y = section1EndY - 2;
            middle2X = section1EndX + 4;
            middle2Y = section1EndY + 2;
        } else {
            section1EndX = section1StartX - SECTION_1_LENGTH;
            section1EndY = section1StartY;
            section2StartX = section1EndX - 3;
            section2StartY = section1EndY + 18;
            section2EndX = section1EndX - 3;
            section2EndY = section1EndY - 18;
            middle1X = section1EndX - 1;
            middle1Y = section1EndY + 2;
            middle2X = section1EndX - 4;
            middle2Y = section1EndY - 2;
        }
    }

    public boolean isIntersectionFree(Vehicle car){
        
    }
}
