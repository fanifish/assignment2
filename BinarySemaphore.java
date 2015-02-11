public class BinarySemaphore {
	boolean value;

	public BinarySemaphore(boolean initValue) {
		value = initValue;
	}

	public synchronized void P() throws InterruptedException {
		while (value == false)
			this.wait();// in the queue of blocked processes
		value = false;
	}

	public synchronized void V() {
		value = true;
		notify();
	}
}
