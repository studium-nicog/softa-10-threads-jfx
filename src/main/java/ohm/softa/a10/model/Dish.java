package ohm.softa.a10.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Dish instance prepared by a cook
 * @author Peter Kurfer
 */

public final class Dish {

	/* Multiplier to calculate the cooking time for the dish */
	private static final double MULTIPLIER = 60d;

	/* Minimum value to add to the calculated cooking time */
	private static final int MINIMUM_COOK_TIME = 600;

	private final int cookingTime;
	private final String mealName;

	public Dish(String mealDescription) {
		this.mealName = mealDescription;

		/* calculate a random cooking time between 600 and round about 2500 */
		this.cookingTime = (int)(Math.random() * MULTIPLIER * (double) mealDescription.length() + MINIMUM_COOK_TIME);
	}

	/**
	 * Get the time required to cook this dish
	 * @return time in ms
	 */
	public final int getCookingTime() {
		return cookingTime;
	}

	/**
	 * Get the name of the dish
	 * @return name
	 */
	public final String getMealName() {
		return mealName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Dish dish = (Dish) o;

		return new EqualsBuilder()
			.append(getCookingTime(), dish.getCookingTime())
			.append(getMealName(), dish.getMealName())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getCookingTime())
			.append(getMealName())
			.toHashCode();
	}
}
