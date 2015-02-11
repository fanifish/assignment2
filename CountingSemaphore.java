public class CountingSemaphore {
	int value;
	int origValue;

	public CountingSemaphore(int initValue) {
		value = initValue;
		origValue = initValue;
	}

	public synchronized void P() throws InterruptedException {
		value--;
		while (value != 0)
			this.wait();
	}

	public synchronized void V() {
		 if(value == 0)
			this.notifyAll();
		    //value = origValue; //reset semaphore
	}
}
