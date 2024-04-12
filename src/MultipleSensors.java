public class MultipleSensors implements Problem {
    private BufferQueue taskQueue;
    private BufferQueue resultQueue;

    Sensor sensor1;
    Sensor sensor2;
    Sensor sensor3;
    Analysis analyzer;
    Actuator actuator;

    Thread sensorThread1;
    Thread sensorThread2;
    Thread sensorThread3;
    Thread analyzerThread;
    Thread actuatorThread;

    private double lambda;
    private double robotStartPos;

    public MultipleSensors(double lambda, double robotStartPos) {
        this.lambda = lambda;
        this.robotStartPos = robotStartPos;
    }
    
    public String name() {
        return("Robot controller with multiple sensors sensors");
    }

    public void init() {
        taskQueue = new BufferQueue(10);
        resultQueue = new BufferQueue(10);

        analyzer = new Analysis(taskQueue, resultQueue);
        actuator = new Actuator(robotStartPos, resultQueue);
        sensor1 = new Sensor(taskQueue, lambda, 1);
        sensor2 = new Sensor(taskQueue, lambda, 2);
        sensor3 = new Sensor(taskQueue, lambda, 3);

        analyzerThread = new Thread(analyzer, "Analyze");
        actuatorThread = new Thread(actuator, "Actuate");

        sensorThread1 = new Thread(sensor1, "Sensor");
        sensorThread2 = new Thread(sensor2, "Sensor");
        sensorThread3 = new Thread(sensor3, "Sensor");

    }

    public void go() {

        sensorThread1.start();
        sensorThread2.start();
        sensorThread3.start();
        analyzerThread.start();
        actuatorThread.start();

        try {Thread.sleep(100000); } catch (InterruptedException e) { }// run for 100 seconds
        
        sensorThread1.interrupt();
        sensorThread2.interrupt();
        sensorThread3.interrupt();
        analyzerThread.interrupt();
        actuatorThread.interrupt();
    
        System.out.println("Finished");

    }
}
