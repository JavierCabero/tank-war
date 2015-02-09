package ai;

public enum AIType {

	NORMAL(128, 64, 5),
	SMART(64, 64, 12), 
	SUPER_SMART(8, 8, -1);

	/* Variables */
	private int pathDelay = 128;
	private int maxWait = 64;
	private int maxPathUse = -1;

	/* Constructor */
	AIType(int delayFreq) {
		this.pathDelay = delayFreq;
	}

	AIType(int delayFreq, int maxWait, int maxPathUse) {
		this.pathDelay = delayFreq;
		this.maxWait = maxWait;
		this.maxPathUse = maxPathUse;
	}

	public int getPathDelay() {
		return pathDelay;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public int getMaxPathUse() {
		return maxPathUse;
	}
}
