package lab1;

import java.util.*;
import java.util.Scanner;

public class ThreadedPrime {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of threads: ");
        int numberOfThreads = Integer.parseInt(scanner.next());

        System.out.print("Enter range:  ");
        int maxNumber = Integer.parseInt(scanner.next());


        ThreadedPrime threadedPrime = new ThreadedPrime();
        List<Integer> primes = threadedPrime.getListOfPrimes(numberOfThreads,maxNumber);
        threadedPrime.printListOfPrimes(primes);


    }

    private List<Integer> getListOfPrimes(int numberOfThreads, Integer maxNumber) {
        boolean[] primes1 = new boolean[maxNumber + 1];
        int blockSize = (maxNumber + numberOfThreads - 1)/numberOfThreads;
        List<Thread> threads = new ArrayList<>();

        for (int i = 2; i <= maxNumber; i++) {
            primes1[i] = true;
        }

        for (int i = 0; i < numberOfThreads; i++) {
            int start = i * blockSize + 2;
            int end = Math.min((i + 1) * blockSize + 1, maxNumber);

            Thread thread = new Thread(() -> {
                for (int j = 2; j * j <= end; j++) {
                    if (primes1[j]) {
                        int startMultiple = (start / j) * j;
                        if (startMultiple < j * j) {
                            startMultiple = j * j;
                        }
                        for (int k = startMultiple; k <= end; k += j) {
                            primes1[k] = false;
                        }
                    }
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<Integer> primeNumbers = new ArrayList<>();
        for (int i = 2; i <= maxNumber; i++) {
            if (primes1[i]) {
                primeNumbers.add(i);
            }
        }

        return primeNumbers;
    }

    private void printListOfPrimes(List<Integer> primes) {

        System.out.println(" ");
        System.out.println("List of prime numbers: ");
        int count = 0;
        for (int prime : primes) {
            System.out.print(prime + ", ");
            count++;
            if (count == 10) {
                System.out.println();
                count = 0;
            }
        }
    }}