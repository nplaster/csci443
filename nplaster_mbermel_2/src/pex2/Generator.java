package pex2;

public class Generator {
	private boolean generating;
	
	public Generator() {
		generating = false;
	}

	public synchronized boolean generating(){
		return generating;
	}
	
	public synchronized void startGenerator(){
		generating = true;
	}

	public synchronized void stopGenerator(){
		generating = false;
		this.notify();
	}

}
