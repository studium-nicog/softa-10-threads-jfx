package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Peter Kurfer
 */

public final class Cook implements Runnable {

	private static final Logger logger = LogManager.getLogger(Cook.class);
	private final String name;
	private final KitchenHatch kitchenHatch;
	private final ProgressReporter progressReporter;

	public Cook(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		Order o;
		do {
			/* try to deque an order */
			o = kitchenHatch.dequeueOrder(2000);
			if(o != null){
				Dish d = new Dish(o.getMealName());
				try {
					/* 'prepare' the dish */
					Thread.sleep(d.getCookingTime());
					logger.info("Cook {} prepared meal {}", name, d.getMealName());
				} catch (InterruptedException e) {
					logger.error("Failed to cook meal", e);
				}
				/* pass the dish to the kitchen hatch */
				kitchenHatch.enqueueDish(d);
				logger.info( "Cook {} put meal {} into the kitchen hatch", name, d.getMealName());

				/* update progress - reduce orders progress and optionally enlarge kitchen hatch fill level */
				progressReporter.updateProgress();
			}
		}while (o != null);

		/* notify the progress reporter that the cook is leaving */
		progressReporter.notifyCookLeaving();
	}
}
