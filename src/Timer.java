
public class Timer {
	
	private static boolean counting;
	private static long start;
	private static final double b = Math.pow(10, 9);
	
	public Timer() {
		counting=true;
		start=System.nanoTime();
		new Thread(()-> {
			while(counting) {
				calcDif(System.nanoTime());
			}
		}).start();
	}
	
	private static int calcDif(long d1) {
		return (int)Math.round(d1-start/b);
	}

	public void stop() {
		counting=false;
	}
}
