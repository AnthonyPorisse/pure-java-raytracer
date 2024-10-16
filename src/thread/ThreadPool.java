package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadPool {
	private ArrayBlockingQueue<Runnable> queue;
	private List<Thread> threads = new ArrayList<>();
	
	private class Worker implements Runnable{

		@Override
		public void run() {
			try {
				while(!Thread.interrupted()) {
					Runnable r = queue.take();
					r.run();
				}
			}catch(InterruptedException e) { return; }
		}
	}

	public ThreadPool(int qsize, int nbThreads) {
		queue = new ArrayBlockingQueue<Runnable>(qsize);
		for(int i=0; i<nbThreads; i++) {
			Thread t = new Thread(new Worker());
			t.start();
			threads.add(t);
		}
	}
	
	public void submit(Runnable r) throws InterruptedException{
		queue.add(r);
	}
	
	public void shutdown() {
		for(Thread t : threads) {
			try {
				t.interrupt(); t.join();
			} catch(InterruptedException e) { e.printStackTrace();}
		}
		threads.clear();
		queue.clear();
	}
}
