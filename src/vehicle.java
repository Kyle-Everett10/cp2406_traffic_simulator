import java.lang.reflect.Array;

public class vehicle
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
    public vehicle(String type, String name, int startingX, int startingY, String defaultDirection){
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
        changeDirection(defaultDirection);
    }

    public void changeDirection(String newDirection){
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

    public boolean collisionDetection(vehicle otherCar){
        boolean decision = true;
        if((otherCar.getCurrentXFront() != this.getCurrentXFront() && otherCar.getCurrentYFront() != this.getCurrentYFront())&& otherCar.getDirection().equals(this.getDirection())) {
            if(this.getDirection().equals("north")&& (otherCar.getCurrentYFront()-otherCar.getLength()-1) == this.getCurrentYFront()){
                decision = false;
            } else if (this.getDirection().equals("south") && (otherCar.getCurrentYFront()+otherCar.getLength()+1) == this.getCurrentYFront()){
                decision = false;
            } else if(this.getDirection().equals("east") && (otherCar.getCurrentXFront()-otherCar.getLength()-1)== this.getCurrentXFront()){
                decision = false;
            } else if(this.getDirection().equals("west") && (otherCar.getCurrentXFront()+otherCar.getLength()+1) == this.getCurrentXFront()){
                decision = false;
            }

        }
        return decision;
    }
}
