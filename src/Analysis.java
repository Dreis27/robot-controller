
// Analyses the tasks generated by Sensor, and passes results to Actuator
public class Analysis implements Runnable{
    private BufferQueue taskQueue;
    private BufferQueue resultQueue;

    public Analysis(BufferQueue taskQueue, BufferQueue resultQueue){
        this.taskQueue = taskQueue;
        this.resultQueue = resultQueue;
    }

    public void run(){
        while(true) {
            try{
                Task randomTask = taskQueue.dequeue();
                double sleepTime = getSleepTime(randomTask);
                try{
                    // thread sleeps for the amount of time represented by the task complexity
                    Thread.sleep((long)sleepTime);
                } catch (InterruptedException e){break;}

                try{
                    // pass tasks to the Actuator
                    resultQueue.enqueue(randomTask);
                } catch (InterruptedException e) {break;}
                
            } catch (InterruptedException e){
                break;
            }
        }
    }
    public double getSleepTime(Task task) {
        return task.getC()*1000;
    }

}
