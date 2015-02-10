/**
 *  synchronization aid that allows a set of threads to wait for each other
 *  @author faniel ghirmay ffg92 & jode garcia jag7235
 *
 */
public class CyclicBarrier {
	int numOfThreads;
	
	public CyclicBarrier(int parties){
		numOfThreads = parties;
	}
	/**
	 * waits until all the threads enter the wait queue before releasing them 
	 * @return
	 * @throws InterruptedException
	 */
	int await() throws InterruptedException{
		while(numOfThreads != 0)
			Util.myWait();
		return 0;
		
	}
}

