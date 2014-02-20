package pex2;

public class RequestPowerThread implements Runnable {
	private Pump currentpump;

	public RequestPowerThread(Pump pump) {
		currentpump = pump;
	}

	@Override
	public void run() {
		
		for(int i = 0; i < 10; i++){
			currentpump.setStatus(Status.READY);
			//check if left and right generator available
			if(!currentpump.getLeftGenerator().generating()){
				currentpump.getLeftGenerator().startGenerator();
				currentpump.setStatus(Status.WAITING);
			}
			else if(!currentpump.getRightGenerator().generating()){
				currentpump.getRightGenerator().startGenerator();
				currentpump.setStatus(Status.WAITING);
			}
			else{
				currentpump.getLeftGenerator().startGenerator();
				currentpump.getRightGenerator().startGenerator();
				currentpump.pumpWater();
			}
		}
		currentpump.getLeftGenerator().stopGenerator();
		currentpump.getRightGenerator().stopGenerator();
		currentpump.pumpCleaning();
	}

}
