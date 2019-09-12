package traffic_simulator;

public class Vehicle
{
    private final int DEFAULT_LENGTH = 4;
    private final int BREADTH = DEFAULT_LENGTH/2;
    private String vehicleType;
    private String vehicleName;
    private int currentXFront;
    private int currentYFront;
    private int currentXBack;
    private int currentYBack;
    private int length;
    private String direction;
    boolean onIntersection = false;
    public Vehicle(String type, String name, int startingX, int startingY, String defaultDirection){
        vehicleType = type;
        vehicleName = name;
        currentXFront = startingX; //Vehicles will have one x/y value for their front and another x/y value for their back
        currentYFront = startingY;
        if(vehicleType.equals("Car")){
            length = DEFAULT_LENGTH;
        } else if (vehicleType.equals("Bus")){
            length = DEFAULT_LENGTH*3;
        } else {
            length = DEFAULT_LENGTH/2;
        }
        updateBackCoordinate(defaultDirection);
    }

    public void updateBackCoordinate(String newDirection){
        direction = newDirection;
        if(direction.equals("north")){
            currentXBack = currentXFront;
            currentYBack = currentYFront - length;
        } else if (direction.equals("east")){
            currentXBack = currentXFront - length;
            currentYBack = currentYFront;
        } else if (direction.equals("south")){
            currentXBack = currentXFront;
            currentYBack = currentYFront + length;
        } else {
            currentXBack = currentXFront + length;
            currentYBack = currentYFront;
        }
    }

    public int getCurrentXFront() {
        return currentXFront;
    }

    public int getCurrentYFront() {
        return currentYFront;
    }

    public String getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public int getCurrentXBack(){
        return currentXBack;
    }

    public int getCurrentYBack() {
        return currentYBack;
    }

    public boolean collisionDetection(Vehicle otherCar){
        boolean decision = true;
        if((otherCar.getCurrentXFront() != this.getCurrentXFront() && otherCar.getCurrentYFront() != this.getCurrentYFront())&& otherCar.getDirection().equals(this.getDirection())) {
            if(this.getDirection().equals("north")&& (otherCar.getCurrentYBack()-1) == this.getCurrentYFront()){
                decision = false;
            } else if (this.getDirection().equals("south") && (otherCar.getCurrentYBack()+1) == this.getCurrentYFront()){
                decision = false;
            } else if(this.getDirection().equals("east") && (otherCar.getCurrentXBack()-1)== this.getCurrentXFront()){
                decision = false;
            } else if(this.getDirection().equals("west") && (otherCar.getCurrentXBack()+1) == this.getCurrentXFront()){
                decision = false;
            }

        }
        return decision;
    }

    public void drive(){
        if(this.getDirection().equals("north")){
            this.currentYFront += 1;
            this.updateBackCoordinate("north");
        } else if(this.getDirection().equals("east")){
            this.currentXFront += 1;
            this.updateBackCoordinate("east");
        } else if(this.getDirection().equals("south")){
            this.currentYFront -= 1;
            this.updateBackCoordinate("south");
        } else {
            this.currentXFront -= 1;
            this.updateBackCoordinate("west");
        }
    }


    public void snapOnIntersection(int currentXFront, int currentYFront, String direction) { //This handles turning on intersections
        this.currentXFront = currentXFront;
        this.currentYFront = currentYFront;
        this.updateBackCoordinate(direction);
    }
}
