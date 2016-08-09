package HeartBeat;

public class Heart_Beat_Listener extends Thread{
	private final long PERIOD = 5000;
	
	// -2 : init
	// 1  : alive
	// 0  : dead
	private int alive = -2;
	private boolean running = true;
	HeartBeatCallBack cb = null;
	public Heart_Beat_Listener(int port, final HeartBeatCallBack cb) throws Exception {
		this.cb = cb;
		final Heart_Beat_Listener self = this;
		UDPTransporter.server(port, new PacketServer() {
			public String serve(String in) {
				if (self.alive == -2) 
				cb.up(); 
				self.alive = 1;
				
				return null;
			}
		});
		this.start();
	}
	public void stopRunning() {
		this.running = false;
	}
	public void run() {
		while (running) {
			if (alive == -1) {
				alive = -2;
				cb.down();
			}else if (alive == 1) {
				alive = -1;
			}
			try {
				Thread.sleep(PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
