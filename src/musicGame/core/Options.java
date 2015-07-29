package musicGame.core;

public class Options {
	
	private static final double DEFAULT_FLOW_SPEED = 1.5;
	
	private static Options INSTANCE;
	
	public double flowSpeed;
	
	private Options() {
		flowSpeed = DEFAULT_FLOW_SPEED;
	}
	
	public static Options getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Options();
		}
		
		return INSTANCE;
	}
}
