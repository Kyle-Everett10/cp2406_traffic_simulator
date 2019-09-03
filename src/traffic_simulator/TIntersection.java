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
        boolean isFree = true;
        if(direction.equals("north")){
            if(inBetween(car.getCurrentXFront(), this.middle2X, this.middle1X) && inBetween(car.getCurrentYFront(), this.middle2Y, this.middle1Y)){
                isFree = false;
            }
        } else if (direction.equals("south")){
            if(inBetween(car.getCurrentXFront(), this.middle1X, this.middle2X) && inBetween(car.getCurrentYFront(), this.middle1Y, this.middle2Y)){
                isFree = false;
            }
        } else if (direction.equals("east")){
            if(inBetween(car.getCurrentXFront(), this.middle2X, this.middle1X) && inBetween(car.getCurrentYFront(), this.middle1Y, this.middle2Y)){
                isFree = false;
            }
        } else {
            if(inBetween(car.getCurrentXFront(), this.middle1X, this.middle2X) && inBetween(car.getCurrentYFront(), this.middle2Y, this.middle1Y)){
                isFree = false;
            }
        }
        return isFree;
    }

    public int getMiddle1X() {
        return middle1X;
    }

    public int getMiddle1Y() {
        return middle1Y;
    }

    public int getMiddle2X() {
        return middle2X;
    }

    public int getMiddle2Y() {
        return middle2Y;
    }

    public int getSection1EndX() {
        return section1EndX;
    }

    public int getSection1EndY() {
        return section1EndY;
    }

    private boolean inBetween(int input, int max, int min){
        boolean decision = false;
        if(input >= min && input <= max) {
            decision = true;
        }
        return decision;
    }
}
