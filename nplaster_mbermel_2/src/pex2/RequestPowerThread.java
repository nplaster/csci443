package pex2;

/**
 * a thread which requests the lock on the generator until it gets the lock or
 * reaches ten tries
 */
public class RequestPowerThread implements Runnable {
	private Pump currentpump;
	private Generator generator;
	private boolean startPumping;

	/**
	 * constructor to set up the thread for requesting power
	 * 
	 * @param pump
	 *            - current pump thread executing
	 * @param generator
	 *            - current generator either left or right
	 */
	public RequestPowerThread(Pump pump, Generator generator) {
		currentpump = pump;
		this.generator = generator;
		// set status of pump to not pumping
		startPumping = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// set status of pump to waiting
		currentpump.setStatus(Status.WAITING);
		// repeat check for lock 10 times
		for (int i = 0; i < 10; i++) {
			// if the pump gets the lock on the generator break out and set the
			// status of the generator to generating
			if (generator.startGenerator()) {
				startPumping = true;
				break;
			}
			// pause for 1/10 seconds every loop
			currentpump.pause(100);
		}
	}

	/**
	 * getter for the status of the generator
	 */
	public boolean isStartPumping() {
		generator.currentlyGenerating();
		return startPumping;
	}

}
