package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Peter Kurfer
 */

public final class KitchenHatchImpl implements KitchenHatch {

	private static final Logger logger = LogManager.getLogger(KitchenHatchImpl.class);
	private final int maxMeals;
	private final Deque<Order> orders;
	private final Deque<Dish> dishes;

	public KitchenHatchImpl(int maxMeals, Deque<Order> orders) {
		this.maxMeals = maxMeals;
		this.orders = orders;
		dishes = new LinkedList<>();
	}

	@Override
	public int getMaxDishes() {
		return maxMeals;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		Order o = null;
		/* synchronize to avoid unpredictable behavior when multiple cooks are dequeuing orders at the same time */
		synchronized (orders) {
			if (orders.size() >= 1) {
				o = orders.pop();
			}
		}
		return o;
	}

	@Override
	public int getOrderCount() {
		/* synchronize to avoid unpredictable behavior when multiple waiters are checking if there are still orders available */
		synchronized (orders) {
			return orders.size();
		}
	}

	@Override
	public Dish dequeueDish(long timeout) {
		long currentTimeStamp = System.nanoTime();
		/* synchronize to avoid unpredictable behavior when multiple waiters are dequeuing dishes at the same time */
		synchronized (dishes) {
			while (dishes.size() == 0) {
				try {
					logger.info( "Kitchen hatch is empty. I can wait");
					/* wait until new dishes are enqueued or timeout is reached - blocks the thread */
					dishes.wait(timeout);
				} catch (InterruptedException e) {
					logger.error("Error when waiting for new dishes", e);
				}

				/* break condition as a waiter is leaving if it is getting a null result and waited for a long time */
				if (timeout > 0 && dishes.size() == 0 && System.nanoTime() - currentTimeStamp > timeout * 1000) {
					logger.info("Kitchen hatch still empty. Going home now");
					/* notify all waiters to re-enable them */
					dishes.notifyAll();
					return null;
				}
			}
			/* dequeue a completed dish */
			Dish result = dishes.pop();
			logger.info("Taking {} out of the kitchen hatch", result);

			/* re-enable all waiting waiters */
			dishes.notifyAll();
			return result;
		}
	}

	@Override
	public void enqueueDish(Dish d) {
		/* synchronize to avoid unpredictable behavior when multiple cooks are enqueuing dishes at the same time */
		synchronized (dishes) {
			while (dishes.size() >= maxMeals) {
				try {
					/* wait if the kitchen hatch is full at the moment */
					logger.info("Kitchen hatch is full, waiting...");
					dishes.wait();
				} catch (InterruptedException e) {
					logger.error("Error while waiting for enough place to place meal");
				}
			}

			logger.info("Putting {} into the kitchen hatch", d);

			/* enqueue the prepared dish */
			dishes.push(d);

			/* re-enable all waiting cooks */
			dishes.notifyAll();
		}
	}

	@Override
	public int getDishesCount() {
		synchronized (dishes) {
			return dishes.size();
		}
	}
}
