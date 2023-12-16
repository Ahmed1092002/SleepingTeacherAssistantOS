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
public class Student implements Runnable{
    // Time to program before asking for help (in seconds).
    private int waitToAsk;
    
    // Student number.
    private int studentNum;

    // Semaphore used to wakeup TA.
    private SignalController wakeup;

    // Semaphore used to wait in chairs outside office.
    private Semaphore chairs;

    // Mutex lock (binary semaphore) used to determine if TA is available.
    private Semaphore TeacherAvailable;

    // A reference to the current thread.
    private Thread t;
    
    
    
    // Non-default constructor.
    public Student(int waitToAsk, SignalController w, Semaphore c, Semaphore a, int studentNum)
    {
        this.waitToAsk = waitToAsk;    
        wakeup = w;
        chairs = c;
        TeacherAvailable = a;
        this.studentNum = studentNum;
        t = Thread.currentThread();
        
    }

    /**
     * The run method will infinitely loop between programming and
     * asking for help until the thread is interrupted.
     */
    @Override
    public synchronized void run()
    {
        // Infinite loop.
        while(!Thread.currentThread().isInterrupted())
        {
            try
            {
               // Program first.
               System.out.println("Student " + studentNum + " has started programming for " + waitToAsk + " seconds.");
               t.sleep(waitToAsk * 1000);
                
               // Check to see if TA is available first.
               System.out.println("Student " + studentNum + " is checking to see if TA is available.");
               if (TeacherAvailable.tryAcquire())//1->0
               {
                   try
                   {
                       // Wakeup the TA.
                       wakeup.sendSignal();
                       System.out.println("Student " + studentNum + " has woke up the TA. ");
                       System.out.println("Student " + studentNum + " has started working with the TA." );
                       t.sleep(5000);
                       System.out.println("Student " + studentNum + " has stopped working with the TA. ");
                   }
                   catch (InterruptedException e)
                   {
                       // Something bad happened.
                       Thread.currentThread().interrupt();
                       System.err.println("Student thread interrupted: " + e.getMessage());
                       continue;
                   }
                   finally
                   {

                       TeacherAvailable.release();


                       break;
                   }
               }
               else
               {
                   // Check to see if any chairs are available.
                   System.out.println("Student " + studentNum + " could not see the TA.  Checking for available chairs.");
                   if (chairs.tryAcquire())//3->2   2->1    1->0 
                   {
                       try
                       {
                           // Wait for TA to finish with other student.
                           System.out.println("Student " + studentNum + " is sitting outside the office.  "
                                   + "He is #" + ((3 - chairs.availablePermits())) + " in line.");
                           TeacherAvailable.acquire();
                           System.out.println("Student " + studentNum + " has started working with the TA. ");
                           t.sleep(5000);
                           System.out.println("Student " + studentNum + " has stopped working with the TA. ");
                           TeacherAvailable.release();
                           break;
                       }
                       catch (InterruptedException e)
                       {
                           // Something bad happened.
                           Thread.currentThread().interrupt();
                           System.err.println("Student thread interrupted: " + e.getMessage());
                           continue;
                       }
                   }
                   else
                   {
                       System.out.println("Student " + studentNum + " could not see the TA and all chairs were taken.  Back to programming!");
                   }
               }

            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                System.err.println("Student thread interrupted: " + e.getMessage());

                break;
            }
            finally {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Student thread interrupted. Exiting loop.");
                    break;
                }
            }
        }
    }
}
