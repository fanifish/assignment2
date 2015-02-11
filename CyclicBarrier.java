package com.utexas.fani;

import java.util.concurrent.Semaphore;
import java.lang.Object;
import java.util.Objects;
import java.util.concurrent.locks.*;
import java.util.function.*;

public class CyclicBarrier {
	int numOfThreads;
	int parties;
	int arrivalIndex;
	int x;
	BinarySemaphore mutex;
	CountingSemaphore sync;

	public CyclicBarrier(int parties) {
		numOfThreads = parties;
		this.parties = parties;
		mutex = new BinarySemaphore(true); // guarantees first in first out
		sync = new CountingSemaphore(parties);
	}

	/**
	 * waits until all threads have invoked await
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	int await() throws InterruptedException {
		System.out.println("num of threads calling " + numOfThreads);
		int arrivalIndex = 0;
		mutex.P();
		numOfThreads--;
		arrivalIndex = numOfThreads;
		mutex.V();
		sync.P();
		sync.V();
		if(numOfThreads == 0){
			numOfThreads = parties;
			sync = new CountingSemaphore(parties);   // makes the BarrierCyclic
		}
		return arrivalIndex;
	}

	public static void main(String args[]) throws InterruptedException {
		CyclicBarrier sync = new CyclicBarrier(5);
		Thread t[] = new Thread[5];
		for (int i = 0; i < 5; i += 1) {
			t[i] = new Thread(new po(i, sync));
			t[i].start();
		}
		
		System.out.println("Next phase");

		for (int i = 0; i < 1000000000; i += 1) {
			
		}

		System.out.println("all released");

	}

}

class po implements Runnable {
	int x;
    CyclicBarrier w;
	po(int n, CyclicBarrier wb) {
		x = n;
		w = wb;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("This is thread num " + x);
		try {
			System.out.println("The barries release num " + w.await());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	



