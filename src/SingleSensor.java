// Robot controller with a single Sensor
public class SingleSensor implements Problem {

    private BufferQueue taskQueue;
    private BufferQueue resultQueue;

    private Sensor sensor;
    private Analysis analyzer;
    private Actuator actuator;

    private Thread sensorThread;
    private Thread analyzerThread;
    private Thread actuatorThread;

    private double lambda;
    private double robotStartPos;

    public SingleSensor(double lambda, double robotStartPos) {
        this.lambda = lambda;
        this.robotStartPos = robotStartPos;
    }

    public String name() {
        return("Robot controller with a single sensor");
    }

    public void init() {
        taskQueue = new BufferQueue(10);
        resultQueue = new BufferQueue(10);

        sensor = new Sensor(taskQueue, lambda, 1);
        analyzer = new Analysis(taskQueue, resultQueue);
        actuator = new Actuator(robotStartPos, resultQueue);

        sensorThread = new Thread(sensor, "Sensor");
        analyzerThread = new Thread(analyzer, "Analyze");
        actuatorThread = new Thread(actuator, "Actuate");

    }

    public void go() {
        sensorThread.start();
        analyzerThread.start();
        actuatorThread.start();  

        try {Thread.sleep(100000); } catch (InterruptedException e) { }// run for 100 seconds

        sensorThread.interrupt();
        analyzerThread.interrupt();
        actuatorThread.interrupt();
    
        System.out.println("Finished");
    }
}
