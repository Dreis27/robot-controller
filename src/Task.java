public class Task {
    private double c;
    private int sensorID;
    private int taskID;

    public Task(double complexity, int sensorID, int taskId){
        this.c = complexity;
        this.sensorID = sensorID;
        this.taskID = taskId;
    }

    public int getID(){
        return this.taskID;
    }
    public double getC(){
        return this.c;
    }
    public double getY(){
        return Math.sqrt(1/this.getC());
    }
    public int getSensorID(){
        return sensorID;
    }
}