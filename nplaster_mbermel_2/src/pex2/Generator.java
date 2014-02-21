package pex2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Generator {
	private Lock locker;
	private boolean generating;
	
	public Generator(){
		locker = new ReentrantLock();
		generating = false;
	}
	
	public boolean startGenerator(){
		return locker.tryLock();
	}

	public void stopGenerator(){
		locker.unlock();
		generating = false;
	}
	
	public void currentlyGenerating(){
		generating = true;
	}

	public boolean isGenerating() {
		return generating;
	}

}
