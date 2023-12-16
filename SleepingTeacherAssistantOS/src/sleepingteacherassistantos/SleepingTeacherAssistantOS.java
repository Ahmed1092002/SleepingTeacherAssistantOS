///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sleepingteacherassistantos;
//
//import java.util.Random;
//import java.util.Scanner;
//import java.util.concurrent.Semaphore;
//public class SleepingTeacherAssistantOS {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//         Scanner input  = new Scanner(System.in);
//        // Number of students.
//        System.out.println("Number of students: ");
//        int numberofStudents = input.nextInt();
//        System.out.println("Number of TAs: ");
//        int numberofTeachersAvailable = input.nextInt();
//        System.out.println("Number of chairs: ");
//        int numberofChairs = input.nextInt();
//        
//        // Create semaphores.
//        SignalSemaphore wakeup = new SignalSemaphore();
//        Semaphore chairs = new Semaphore(numberofChairs);
//        Semaphore TAsAvailable = new Semaphore(numberofTeachersAvailable);
//        
//        
//        // Used for randomly generating program time.
//        Random studentWait = new Random();
//        
//        // Create student threads and start them.
//        for (int i = 0; i < numberofStudents; i++)
//        {
//            Thread student = new Thread(new Student(studentWait.nextInt(20), wakeup, chairs, TAsAvailable, i+1));
//            student.start();
//        }
//        
//        // Create and start TA Threads.
//        for (int i = 0; i <numberofTeachersAvailable; i++)
//        {
//            Thread ta = new Thread(new TeachingAssistant(wakeup, chairs, TAsAvailable, i+1,numberofChairs));
//            ta.start();
//        }
//        
//    }
//    
//}
