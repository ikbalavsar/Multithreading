/*
Student Name : Gülsüm İkbal Avşar
Student ID : 64180006
Department : CoE - EEE
Programming Assignment-1
 */

import java.io.FileWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.io.IOException;

public class main_file_odev {
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        long total_time = 0;
        long total_time2 = 0;
        long total_time3 = 0;
        long total_time4 = 0;
        //Creating 8 Threads
        MyThread t1 = new MyThread("T1");
        MyThread t2 = new MyThread("T2");
        MyThread t3 = new MyThread("T3");
        MyThread t4 = new MyThread("T4");
        MyThread t5 = new MyThread("T5");
        MyThread t6 = new MyThread("T6");
        MyThread t7 = new MyThread("T7");
        MyThread t8 = new MyThread("T8");

        // Creating Four Merge Threads

        MergeThread m12 = new MergeThread("M12",t1.array,t2.array);
        MergeThread m34 = new MergeThread("M34", t3.array,t4.array);
        MergeThread m56 = new MergeThread("M56",t5.array,t6.array);
        MergeThread m78 = new MergeThread("M78",t7.array,t8.array);

        // Creating Two Merge Threads

        MergeThread m1 = new MergeThread("M1",m12.result_array, m34.result_array);
        MergeThread m2 = new MergeThread("M2", m56.result_array, m78.result_array);

        // CReating Final Merge Threads
        MergeThread m0 = new MergeThread("M0",m1.result_array,m2.result_array);


        //Creating Sorted File
        FileWriter myObj = new FileWriter(" SortedUsingMultipleThreads.txt");
        myObj.write(String.valueOf(m0));
        myObj.close();
        System.out.println("**************** Successfully wrote to the file. ************");


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        System.out.println("Main thread exiting.");

        // Calculating timing of threads
        System.out.println("\n***** Total Times of Threads and Merges *******\n");

        total_time = t1.timingg + t2.timingg + t3.timingg + t4.timingg + t5.timingg + t6.timingg + t7.timingg + t8.timingg;
        System.out.println("Total time of threads : " + total_time);

        total_time2 = m12.timingg + m34.timingg+ m56.timingg + m78.timingg;
        System.out.println("Total time of merge T1,T2,T3,T4,T5,T6,T7,T8 threads: " +total_time2);


        total_time3 = m1.timingg + m2.timingg;
        System.out.println("Total time of merge M12,M34 = [M1] & M56,M78 = [M2] threads: " +total_time3);

        total_time4 = m0.timingg;
        System.out.println("Total time of merge M1,M2 = [MO] threads: " +total_time4);

    }

}
class MergeThread implements  Runnable{
    String name;
    Thread t;
    int[] array1;
    int[] array2;
    int[] result_array;
    long timingg;
    Instant start_time;
    Instant end_time;

    MergeThread (String threadname,int[] array_1, int[] array_2){
        name = threadname;
        array1 = array_1;
        array2 = array_2;
        result_array = new int[array_1.length+array_2.length];
        t = new Thread(this, name);
        System.out.println("New thread: " + t);
        timingg= 0;
        t.setPriority(1);
        t.start();

    }

    public void run() {
        start_time = Instant.now();
        try {
            mergeArrays(array1,array2,array1.length,array2.length,result_array);
            Thread.sleep(1000);
            end_time = Instant.now();
            timingg += Duration.between(start_time, end_time).toMillis();

        }catch (InterruptedException e) {
            System.out.println(name + "Interrupted");
        }
        System.out.println(name + " exiting.");
        System.out.println(name+"\tStart Time: " + LocalDateTime.ofInstant(start_time, ZoneOffset.UTC) + "\tEnd Time: " + LocalDateTime.ofInstant(end_time, ZoneOffset.UTC) +"\tTime taken: "+ timingg +" milliseconds");

        System.out.println(name + " exiting.");
    }

    public static void mergeArrays(int[] arr1, int[] arr2, int n1,
                                   int n2, int[] arr3)
    {
        int i = 0, j = 0, k = 0;

        // Traverse both array
        while (i<n1 && j <n2)
        {
            if (arr1[i] < arr2[j])
                arr3[k++] = arr1[i++];
            else
                arr3[k++] = arr2[j++];
        }
        while (i < n1)
            arr3[k++] = arr1[i++];

        while (j < n2)
            arr3[k++] = arr2[j++];
    }
}
class MyThread implements Runnable {
    String name;
    Thread t;
    long timingg;
    Instant start_time;
    Instant end_time;
    int array_size;
    int [] array;

    MyThread(String thread_name) {
        name = thread_name;
        t = new Thread(this, name);
        System.out.println("New thread: " + t);
        timingg= 0;
        t.setPriority(10);
        t.start();
    }

    @Override
    public void run() {
        start_time = Instant.now();
        try {
            array_size = (int)(Math.random() * 10001) + 10000;
            array = new int[array_size];
            for(int ii=0 ; ii < array_size ; ii++){
                array[ii] = (int)(Math.random() * 40000) + 1;
            }
            quickSort(array,0,array_size-1);
            //end_time belki sleep in altına gelebilir i dont know
            end_time = Instant.now();
            timingg += Duration.between(start_time, end_time).toMillis();
            Thread.sleep(1000);

        }catch (InterruptedException e) {
            System.out.println(name + "Interrupted");
        }
        System.out.println(name + " exiting.");
        System.out.println(name+"\tStart Time: " + LocalDateTime.ofInstant(start_time, ZoneOffset.UTC) +
                "\tEnd Time: " + LocalDateTime.ofInstant(end_time, ZoneOffset.UTC) +"\tTime taken: "+ timingg +" milliseconds");
    }
    static void quickSort(int[] arr, int low, int high)
    {
        if (low < high)
        {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    static int partition(int[] arr, int low, int high)
    {

        // pivot
        int pivot = arr[high];

        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {
            if (arr[j] < pivot)
            {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }
    static void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}