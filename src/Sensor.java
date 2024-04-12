
// Sensor generates the tasks and passes them to Analysis
public class Sensor implements Runnable{
    private Task randomTask;
    private BufferQueue taskQueue;
    private double[] probabilities;
    private double lambda;
    private int sensorID;

    public Sensor(BufferQueue taskQueue, double lambda, int sensorID){
        this.taskQueue = taskQueue;
        this.lambda = lambda;
        this.sensorID = sensorID;
        this.probabilities = new double[20];
    }

    // Each second generate the amount of tasks determined by the Poisson function
    public void run(){
        while(true){
            int taskNumber = poisson(lambda);
            for(int i = 0; i<taskNumber; i++){
                try {
                    generateTask();
                } catch (InterruptedException e){break;}
            }
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){break;}
        }
    }

    // Function to choose a number based on Poisson distribution
    public int poisson(double lambda) {
        double sum = 0;
        // get probability for each number
        for (int i=0; i<probabilities.length; i++){
            probabilities[i] = (Math.pow(lambda, i)*Math.pow(Math.exp(1), (-lambda)))/factorial(i);
            sum = sum + probabilities[i];
        }
        // add up the probabilities to represent them as ranges between 0 and the sum;
        double[] probabilityRanges = new double[probabilities.length];
        probabilityRanges[0] = probabilities[0];
        for(int i = 1; i<probabilities.length; i++){
            probabilityRanges[i] = probabilityRanges[i-1] + probabilities[i];
        }
        // choose one number using a randomly generated number
        double random = Math.random() * sum; //limit the randomly generated number not to exceed the sum of our probabilities
        for(int i = 0; i<probabilities.length; i++){
            if(random <= probabilityRanges[i]) {
                return i;
            }
        }
        // the function should never reach this
        return -1;

    }
    
    // Method creates the task and passes it to the queue
    public void generateTask() throws InterruptedException{
        int taskID = taskQueue.getNewTaskId();
        double randomComplexity = (Math.random()*0.4) + 0.1;
        randomTask = new Task(randomComplexity, sensorID, taskID);
        taskQueue.enqueue(randomTask);
    }

    // Calculate the factorial
    public long factorial(int number) {
        long result = 1;
        for (int factor = 2; factor < number + 1; factor++) {
            result = result * factor;
        }

        return result;
    }

    public int getSensorID(){
        return this.sensorID;
    }
}
