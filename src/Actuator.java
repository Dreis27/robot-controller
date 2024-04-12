
// Moves the robot and prints logs
public class Actuator implements Runnable{

    private double robotPosition;
    private BufferQueue resultQueue;
    private Task result;
    private boolean facingRight;
    private double oldPosition;
    private int numberOfTasksCompleted=0;

    public Actuator(double robotStartPosition, BufferQueue resultQueue){
        this.robotPosition = robotStartPosition;
        this.resultQueue = resultQueue;
        this.facingRight = true; // at the start robot is facing right
    }

    public void run() {
        while(true){
            try{
                // move the robot and print the information to the console
                result = resultQueue.dequeue();
                moveRobot(result.getY());
                System.out.printf("%n     Robot moving. Task id %d, task complexity %.3f, result %.3f,%n     old position: %.3f, new position: %.3f, sensorID: %d, Robot facing %s%n%n",
                                    result.getID(), result.getC(), result.getY(), oldPosition, robotPosition, result.getSensorID(), getFacingDirection());
                numberOfTasksCompleted++;
            } catch (InterruptedException e){
                System.out.println("Total tasks completed: "+numberOfTasksCompleted); // print total number of executed tasks after thread is interrupted
                break;
            }
        }
    }

    public void setRobotPosition(double newPosition){
        this.robotPosition = newPosition;
    }
    public void setOldPosition(double position){
        this.oldPosition = position;
    }
    public String getFacingDirection() {
        return (facingRight) ? "right" : "left";
    }

    // function for calculating robot's new position
    public void moveRobot(double moveAmount){
        double remainingMoveAmount = moveAmount;
        setOldPosition(robotPosition);
        double distanceFromWall = (facingRight) ? (1 - robotPosition) : robotPosition;

        while((distanceFromWall - remainingMoveAmount) <= (-1)){
            remainingMoveAmount = remainingMoveAmount -1;
            this.facingRight = !this.facingRight;
        }
        if((distanceFromWall - remainingMoveAmount) <=0) {
            double goBack = distanceFromWall - remainingMoveAmount;
            this.facingRight = !this.facingRight;
            if (facingRight) setRobotPosition((0-goBack));
            else setRobotPosition(1-(0-goBack));

        } else {setRobotPosition(distanceFromWall - remainingMoveAmount);}
    }
}

