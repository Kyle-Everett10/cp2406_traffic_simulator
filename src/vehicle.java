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

    public String getOppositeDirection(){
        if(direction == "north"){
            return "south";
        } else if (direction == "east"){
            return "west";
        } else if (direction == "south"){
            return "north";
        }
    }

    public void collisionDetection(Vehicles[] otherCars){
        for(int i = 0; i < otherCars.length; i++){
            if(otherCars[i].getCurrentXFront == this.currentXFront && otherCars[i].getCurrentYFront == this.currentXFront){
                this.drive();
            } else(())
        }
    }
}
