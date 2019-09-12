package traffic_simulator;

import java.util.Random;

public class SimulatorConsole {
    public static void main(String[] args) throws InterruptedException {
        final double ONE_DAY = 24;
        int spawnRate = 2;
        Random rand = new Random();
        double time = 0.0;
        Vehicle[] car = new Vehicle[spawnRate];
        System.out.println("Road is being constructed");
        Road[] roads = new Road[2];
        for (int i = 0; i < roads.length; i++) {
            roads[i] = new Road("Road " + (i + 1), (i * 36), 50, "east-west");
        }
        CrossIntersection intersection1 = new CrossIntersection("Cross Intersection", roads[1].getRoadEnd2X(), 50, "north");
        System.out.println("Roads constructed");
        System.out.println("Spawning cars");
        car = spawnCars(car, roads);
        System.out.println("Car(s) spawned");
        while (time < ONE_DAY) {
            long milliSeconds = System.currentTimeMillis();
            for (int i = 0; i < car.length; i++) {
                if (car[i] != null) {
                    boolean noCollision = true;
                    boolean onIntersection = false;
                    for (int j = 0; j < car.length; j++) {
                        if (car[j] != car[i] && car[j] != null) {
                            if (!car[i].collisionDetection(car[j])) {
                                noCollision = false;
                            }
                        }
                    }
                    switch (car[i].getDirection()) {
                        case "north":
                            if (car[i].getCurrentXFront() == intersection1.southRoadX && car[i].getCurrentYFront() == intersection1.southRoadY) {
                                onIntersection = true;
                            }
                            break;
                        case "east":
                            if (car[i].getCurrentXFront() == intersection1.westRoadX && car[i].getCurrentYFront() == intersection1.westRoadY) {
                                onIntersection = true;
                            }
                            break;
                        case "south":
                            if (car[i].getCurrentXFront() == intersection1.northRoadX && car[i].getCurrentYFront() == intersection1.northRoadY) {
                                onIntersection = true;
                            }
                            break;
                        default:
                            if (car[i].getCurrentXFront() == intersection1.eastRoadX && car[i].getCurrentYFront() == intersection1.eastRoadY) {
                                onIntersection = true;
                            }
                            break;
                    }
                    if (noCollision) {
                        if (!onIntersection) {
                            car[i].drive();
                        } else {
                            if (intersection1.light.equals(car[i].getDirection())) {
                                int decision = rand.nextInt(5);
                                if (decision == 1) {
                                    System.out.println(car[i].getVehicleName() + " has turned left");
                                    switch (car[i].getDirection()) {
                                        case "north":
                                            car[i].snapOnIntersection(intersection1.getWestSnapX(), intersection1.getWestSnapY(), "west");
                                            break;
                                        case "east":
                                            car[i].snapOnIntersection(intersection1.getNorthSnapX(), intersection1.getNorthSnapY(), "north");
                                            break;
                                        case "south":
                                            car[i].snapOnIntersection(intersection1.getEastSnapX(), intersection1.getEastSnapY(), "east");
                                            break;
                                        default:
                                            car[i].snapOnIntersection(intersection1.getSouthSnapX(), intersection1.getSouthSnapY(), "south");
                                            break;
                                    }
                                } else if (decision == 2) {
                                    System.out.println(car[i].getVehicleName() + " has turned right");
                                    switch (car[i].getDirection()) {
                                        case "north":
                                            car[i].snapOnIntersection(intersection1.getEastSnapX(), intersection1.getEastSnapY(), "east");
                                            break;
                                        case "east":
                                            car[i].snapOnIntersection(intersection1.getSouthSnapX(), intersection1.getSouthSnapY(), "south");
                                            break;
                                        case "south":
                                            car[i].snapOnIntersection(intersection1.getWestSnapX(), intersection1.getWestSnapY(), "west");
                                            break;
                                        default:
                                            car[i].snapOnIntersection(intersection1.getNorthSnapX(), intersection1.getNorthSnapY(), "north");
                                            break;
                                    }
                                } else {
                                    car[i].drive();
                                }
                            } else {
                                System.out.println(car[i].getVehicleName() + " is waiting at intersection");
                            }
                        }
                    }
                    System.out.println(car[i].getVehicleName() + " x: " + car[i].getCurrentXFront());
                    System.out.println(car[i].getVehicleName() + " y: " + car[i].getCurrentYFront());
                }
            }
            int changingLight = rand.nextInt(20);
            if (changingLight == 16) {
                intersection1.changeLight("north");
            } else if (changingLight == 17) {
                intersection1.changeLight("south");
            } else if (changingLight == 18) {
                intersection1.changeLight("east");
            } else if (changingLight == 19) {
                intersection1.changeLight("west");
            }
            time += 0.1;
            car = spawnCars(car, roads);
            Thread.sleep(1000 - milliSeconds % 100);
        }
    }

    private static Vehicle[] spawnCars(Vehicle[] car, Road[] roads) {
        for (int i = 0; i < car.length; i++) {
            if (car[i] == null) {
                int spawnX = 0;
                int spawnY = 0;
                String spawnDirection = "east";
                boolean spawningRoad = false;
                boolean canSpawn = true;
                Road foundRoad = null;
                for (int j = 0; j < roads.length; j++) {
                    if (foundRoad == null) {
                        if (roads[j].roadEnd1X == 0 || roads[j].roadEnd1Y == 0) {
                            for (int k = 0; k < car.length; k++) {
                                if (car[k] != null) {
                                    if ((car[k].getCurrentYBack() - 3 <= roads[j].getRoadEnd1Y() && roads[j].direction.equals("north-south")) || (car[k].getCurrentXBack() - 3 <= roads[j].getRoadEnd1X() && roads[j].direction.equals("east-west"))) {
                                        canSpawn = false;
                                    }
                                } else if (i == 0) {
                                    if (roads[j].direction.equals("north-south")) {
                                        spawnDirection = "north";
                                        spawnX = roads[j].getRoadEnd1X() - 2;
                                        spawnY = roads[j].getRoadEnd1Y();
                                    } else {
                                        spawnDirection = "east";
                                        spawnX = roads[j].getRoadEnd1X();
                                        spawnY = roads[j].getRoadEnd1Y() + 2;
                                    }
                                    spawningRoad = true;
                                    foundRoad = roads[j];
                                    canSpawn = false;
                                }
                                if (canSpawn) {
                                    if (roads[j].direction.equals("north-south")) {
                                        spawnDirection = "north";
                                        spawnX = roads[j].getRoadEnd1X() - 2;
                                        spawnY = roads[j].getRoadEnd1Y();
                                    } else {
                                        spawnDirection = "east";
                                        spawnX = roads[j].getRoadEnd1X();
                                        spawnY = roads[j].getRoadEnd1Y() + 2;
                                    }
                                    spawningRoad = true;
                                    foundRoad = roads[j];
                                }
                            }

                        } else if (roads[j].roadEnd2X == 0 || roads[j].roadEnd2Y == 0) {
                            for (int k = 0; k < car.length; k++) {
                                if (car[k] != null) {
                                    if ((car[k].getCurrentXBack() >= roads[j].getRoadEnd2X() && roads[j].direction.equals("east-west")) || (car[k].getCurrentYBack() >= roads[j].getRoadEnd2Y() && roads[j].direction.equals("north-south"))) {
                                        canSpawn = false;
                                    }
                                } else if (i == 0) {
                                    if (roads[j].direction.equals("north-south")) {
                                        spawnDirection = "south";
                                        spawnX = roads[j].roadEnd2X + 2;
                                        spawnY = roads[j].roadEnd2Y;
                                    } else {
                                        spawnDirection = "west";
                                        spawnX = roads[j].roadEnd2X;
                                        spawnY = roads[j].roadEnd2Y - 2;
                                    }
                                    spawningRoad = true;
                                    foundRoad = roads[j];
                                    canSpawn = false;
                                }
                            }
                            if (canSpawn) {
                                if (roads[j].direction.equals("north-south")) {
                                    spawnDirection = "south";
                                    spawnX = roads[j].roadEnd2X + 2;
                                    spawnY = roads[j].roadEnd2Y;
                                } else {
                                    spawnDirection = "west";
                                    spawnX = roads[j].roadEnd2X;
                                    spawnY = roads[j].roadEnd2Y - 2;
                                }
                                spawningRoad = true;
                                foundRoad = roads[j];
                                canSpawn = false;
                            }
                        }
                    }
                    if (spawningRoad) {
                        car[i] = new Vehicle("car", "car" + (i + 1), spawnX, spawnY, spawnDirection);
                        System.out.println("Created " + car[i].getVehicleName());
                    }
                }
            }
        }
        return car;
    }
}
