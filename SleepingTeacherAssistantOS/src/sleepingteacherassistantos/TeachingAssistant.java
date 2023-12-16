/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleepingteacherassistantos;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Zeko
 */
public class TeachingAssistant implements Runnable{
    // Semaphore used to wakeup TA.
    private SignalController signalTrigger;

    // Semaphore used to wait in chairs outside office.
    private Semaphore chairs;

    // Mutex lock (binary semaphore) used to determine if TA is available.
    private Semaphore TeacherAvailable;

    // A reference to the current thread.
    private Thread t;
    private int numberOfTeacher;
    private int numberofchairs;
    public TeachingAssistant(SignalController w, Semaphore c, Semaphore a, int numberOfTeacher, int numberofchairs)
    {
        t = Thread.currentThread();
        signalTrigger = w;
        chairs = c;
        TeacherAvailable = a;
        this.numberOfTeacher = numberOfTeacher;
        this.numberofchairs=numberofchairs;
    }

    
    
    @Override
    public  synchronized void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            System.out.println("No students left.  The TA "+numberOfTeacher+ " is going to nap.");

            try
            {
                signalTrigger.waitForSignal();
                System.out.println("The TA "+numberOfTeacher+ " was awoke by a student.");

                int permitsAcquired = numberofchairs - chairs.availablePermits();
                while (permitsAcquired > 0) {
                    t.sleep(5000);
                    if (chairs.availablePermits() < numberofchairs) {
                        chairs.release();
                        permitsAcquired--;
                    }
                }

                // If there are other students waiting.
//                if (chairs.availablePermits() != numberofchairs)
//                {
//                    int permitsAcquired = numberofchairs - chairs.availablePermits();
//                    System.out.println("There are "+permitsAcquired);
//                    do
//                    {
//                        t.sleep(5000);
//                        chairs.waitForSignal();
//                 permitsAcquired--;
//                    }
//                    while (permitsAcquired > 0 && chairs.availablePermits() < numberofchairs);
//                }
//
//                else {
//                    t.interrupt();
//                }

            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                System.err.println("TeachingAssistant thread interrupted: " + e.getMessage());
                break;
            }
        }
    }
}
