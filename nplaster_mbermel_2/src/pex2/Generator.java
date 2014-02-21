package pex2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class for power source generators for locking and unlocking
 */
public class Generator {
	private Lock locker;
	private boolean generating;

	/**
	 * creates a reentrant lock and sets the status of the generator to not
	 * generating
	 */
	public Generator() {
		locker = new ReentrantLock();
		generating = false;
	}

	/**
	 * tries to obtain the lock returns true if lock obtained
	 */
	public boolean startGenerator() {
		return locker.tryLock();
	}

	/**
	 * releases the lock on the generator and changes the status to not
	 * generating
	 */
	public void stopGenerator() {
		locker.unlock();
		generating = false;
	}

	/**
	 * sets the status of the generator to generating
	 */
	public void currentlyGenerating() {
		generating = true;
	}

	/**
	 * getter for generating variable
	 */
	public boolean isGenerating() {
		return generating;
	}

}
