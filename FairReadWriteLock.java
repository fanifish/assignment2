import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implimentation of a fair read and write monitor
 * 
 * @author Faniel ghirmay and jose garcia
 *
 */

public class FairReadWriteLock {
	int numOfReaders;
	int numOfWriters;
	int numOfReadersWaiting;
	int numOfWritersWaiting;

	PriorityQueue<String> order = new PriorityQueue<String>(); // to help in
																// fairness

	final ReentrantLock monitorLock = new ReentrantLock();
	final Condition waitForWriter = monitorLock.newCondition();
	final Condition waitForReader = monitorLock.newCondition();
	final Condition writerWaitOrder = monitorLock.newCondition();
	final Condition readerWaitOrder = monitorLock.newCondition();
	final Condition wLock = monitorLock.newCondition();
	
	FairReadWriteLock() {
		numOfReaders = 0;
		numOfWriters = 0;
		numOfReadersWaiting = 0;
		numOfWritersWaiting = 0;
	}

	/**
	 * 
	 */
	void beginRead() {
		monitorLock.lock();
		order.add("reader");
		try {
			/*
			while (order.peek() == "writer")
				// if the writer was here first wait for your turn
				readerWaitOrder.await();
			*/	
			numOfReaders++;
			while (numOfWriters > 0)
				waitForWriter.await();
			
			System.out.println("Reader is in");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			monitorLock.unlock();
		}

	}

	/**
	 * 
	 */
	void endRead() {
		numOfReaders--;
		if (numOfReaders == 0) {
	//		order.poll();
	//		writerWaitOrder.signal();
			waitForReader.signal();
		}
	}

	/**
	 * 
	 */
	void beginWrite() {
		monitorLock.lock();
		order.add("writer");

		try {
			/*
			while (order.peek() == "reader")
				// if the writer was here first wait for your turn
				writerWaitOrder.await();
			*/	
			numOfWriters++;
			while (numOfReaders > 0)
				waitForReader.await();
		//	while(numOfWriters > 0)
			//	wLock.await();
			System.out.println("Writer is in");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		} finally {
			monitorLock.unlock();
		}
	}

	/**
	 * 
	 */
	void endWrite() {
		numOfWriters--;
		if (numOfWriters == 0) {
		//	order.poll();  // remove from top of queue
		//	readerWaitOrder.signal();
			wLock.signal();
			waitForWriter.signal();
		}
	}
	public static void main(String args[]){
		FairReadWriteLock doc  = new FairReadWriteLock();
		Thread w[] = new Thread[10];
		Thread r[] = new Thread[10];
		for(int i=0; i < 10; i+=1){
			Writer wr = new Writer(i, i+"@writer", doc);
			Reader rr = new Reader(i, i+"@reader", doc);
			w[i] = new Thread(wr);
			r[i] = new Thread(rr);
			w[i].start();
			r[i].start();
		}
	}
}


class Writer implements Runnable{
	int x;
	String name;
	FairReadWriteLock file;
	Writer(int x, String n, FairReadWriteLock f){
		this.x = x;
		name = n;
		file = f;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		file.beginWrite();
		System.out.println(name + " is  writing : num " + x );
		file.endWrite();
		System.out.println("Writer " + x + "out");
	}
}
class Reader implements Runnable{
	int x;
	String name;
	FairReadWriteLock file;
	Reader(int x, String n, FairReadWriteLock f){
		this.x = x;
		name = n;
		file = f;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		file.beginRead();
		System.out.println(name + " is  reading : num " + x );
		file.endRead();
		System.out.println("Reader " + x + "out");
	}
}


