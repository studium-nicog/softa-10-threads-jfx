package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.ArrayDeque;
import java.util.Deque;

public class KitchenHatchImpl implements KitchenHatch {
	private final int maxDishes;

	private final Deque<Order> orders;
	private final Deque<Dish> dishes = new ArrayDeque<>();

	public KitchenHatchImpl(int maxDishes, Deque<Order> orders) {
		this.maxDishes = maxDishes;
		this.orders = orders;
	}

	@Override
	public int getMaxDishes() {
		return maxDishes;
	}

	@Override
	public synchronized Order dequeueOrder(long timeout) {
		while (orders.isEmpty()) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		var order = orders.pop();
		notifyAll();
		return order;
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

	@Override
	public synchronized Dish dequeueDish(long timeout) {
		while (dishes.isEmpty()) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		return dishes.pop();
	}

	@Override
	public synchronized void enqueueDish(Dish m) {
		while (dishes.size() >= maxDishes) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		dishes.push(m);
	}

	@Override
	public int getDishesCount() {
		return dishes.size();
	}
}
