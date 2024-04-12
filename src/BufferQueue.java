/*
 * Implementation of methods in this class inspired from Dr James Stovold's lecture on the topic of Seamphores
 * Lancaster University Leipzig, 30/10/2023
 */

import java.util.Queue;
import java.util.LinkedList;
import java.util.Objects;

public class BufferQueue {
    
    private int taskID = 0; // shared variable 
    private Object idLock = 0;
    private Queue<Task> taskQueue = new LinkedList<>();
    private int capacity;
    private Integer lastTaskIdProcessedByAnalyzer = null;
    private Integer lastTaskIdProcessedByActuator = null;
    private Integer lastTaskIdAddedBySensor = null;
    private Integer lastTaskIdAddedByAnalyzer = null;
    private Integer lastTaskIdProcessed = null;
    private Integer lastTaskIdAdded = null;
    private Object enqueueLock = 0;
    private Object dequeueLock = 0;


    public BufferQueue(int capacity) {
        this.capacity = capacity;
    }

    public void enqueue(Task task) throws InterruptedException{
        synchronized(enqueueLock) {
            while(this.taskQueue.size() >= this.capacity){
                // Check which thread is waiting (for error messages)
                if(Objects.equals(Thread.currentThread().getName(), "Sensor")) lastTaskIdAdded = lastTaskIdAddedBySensor;
                else if(Objects.equals(Thread.currentThread().getName(), "Analyze")) lastTaskIdAdded = lastTaskIdAddedByAnalyzer;
                else lastTaskIdAdded = null;

                System.out.println(Thread.currentThread().getName()+ " error: no more space on the buffer. Last task added to the queue: "+ lastTaskIdAdded+".");
                enqueueLock.wait(); //block the thread until it receives a signal
            }
            this.taskQueue.add(task);

            //Save the id of last added task (for error messages)
            if(Objects.equals(Thread.currentThread().getName(), "Sensor")) lastTaskIdAddedBySensor = task.getID();
            if(Objects.equals(Thread.currentThread().getName(), "Analyze")) lastTaskIdAddedByAnalyzer = task.getID();
        }

        synchronized(dequeueLock){ // lock this section to ensure that notify() won't get called before a dequeueing thread calls wait()
            dequeueLock.notify(); //wake up a waiting thread (if there are any)
        }
        
    }


    public Task dequeue() throws InterruptedException{
        Task removedTask;
        synchronized (dequeueLock){
            while(this.taskQueue.size() == 0){
                // Check which thread is waiting (for error messages)
                if(Objects.equals(Thread.currentThread().getName(), "Analyze")) lastTaskIdProcessed = lastTaskIdProcessedByAnalyzer;
                else if(Objects.equals(Thread.currentThread().getName(), "Actuate")) lastTaskIdProcessed = lastTaskIdProcessedByActuator;
                else lastTaskIdProcessed = null;
                
                System.out.println(Thread.currentThread().getName()+ " error: no results to process. Last task processed "+ lastTaskIdProcessed+".");
                dequeueLock.wait(); //block the thread until it receives a signal
            }
            removedTask = this.taskQueue.remove();

            //Save the id of the last removed task (for error messages)
            if(Objects.equals(Thread.currentThread().getName(), "Analyze")) lastTaskIdProcessedByAnalyzer = removedTask.getID();
            if(Objects.equals(Thread.currentThread().getName(), "Actuate")) lastTaskIdProcessedByActuator = removedTask.getID();
        }
            
        synchronized(enqueueLock){ // lock this section to ensure that notify() won't get called before an enqueueing thread calls wait()
            enqueueLock.notify(); //wake up a waiting thread (if there are any)
        }

        return removedTask;     
    }

    // Method allows to keep task IDs unique
    public int getNewTaskId() {
        synchronized (idLock) {
            taskID++;
        }
        return taskID;
    }
    
    public int getCapacity() {
        return this.capacity;
    }

}
