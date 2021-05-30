package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Peter Kurfer
 */

public final class Waiter implements Runnable {

	private static final Logger logger = LogManager.getLogger(Waiter.class);

	private final String name;
	private final KitchenHatch kitchenHatch;
	private final ProgressReporter progressReporter;

	public Waiter(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		Dish d;
		do {
			/* remove a prepared dish from the kitchen hatch */
			d = kitchenHatch.dequeueDish(5000);
			if(d != null){
				try {
					/* simulate serving of the dish */
					Thread.sleep((long)(Math.random() * 1000));
					logger.info("Waiter {} serviced meal {}", name, d.getMealName());

					/* update kitchen hitch fill level in the UI */
					progressReporter.updateProgress();
				}catch (InterruptedException e){
					logger.error("Failed to deliver meal {} by waiter {}", d.getMealName(), name);
				}
			}
		}while (kitchenHatch.getOrderCount() > 0 || d != null);

		/* notify the progress reporter that the waiter is leaving */
		progressReporter.notifyWaiterLeaving();
		logger.info("Seems there's nothing to do anymore - {} going home", name);
	}
}
