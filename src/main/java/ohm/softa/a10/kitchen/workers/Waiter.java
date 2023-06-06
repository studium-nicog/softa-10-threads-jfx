package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;

import java.util.Random;

public class Waiter implements Runnable {
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
		while (kitchenHatch.getDishesCount() > 0 || kitchenHatch.getOrderCount() > 0) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			System.out.println(name + " is dequeing a dish");

			kitchenHatch.dequeueDish();
			progressReporter.updateProgress();
		}

		System.out.println(name + " is leaving");
		progressReporter.notifyWaiterLeaving();
	}
}
