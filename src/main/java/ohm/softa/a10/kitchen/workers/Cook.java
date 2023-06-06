package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.util.NameGenerator;

public class Cook implements Runnable {
	private final String name;
	private final KitchenHatch kitchenHatch;
	private final ProgressReporter progressReporter;
	private final NameGenerator nameGenerator;

	public Cook(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter, NameGenerator nameGenerator) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
		this.nameGenerator = nameGenerator;
	}

	@Override
	public void run() {
		while(kitchenHatch.getOrderCount() != 0) {
			synchronized (this) {
				var order = kitchenHatch.dequeueOrder();
				var dish = new Dish(nameGenerator.getRandomDish());

				try {
					Thread.sleep(dish.getCookingTime());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				System.out.println(name + " is enqueing a new dish");

				kitchenHatch.enqueueDish(dish);
				progressReporter.updateProgress();
			}
		}

		System.out.println(name + " is leaving");

		progressReporter.notifyCookLeaving();
	}
}
