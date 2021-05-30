package ohm.softa.a10.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Order containing the name of the dish a cook has to prepare
 * @author Peter Kurfer
 */

public final class Order {

	private final String mealName;

	public Order(String mealName) {
		this.mealName = mealName;
	}

	public final String getMealName() {
		return mealName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Order order = (Order) o;

		return new EqualsBuilder()
			.append(getMealName(), order.getMealName())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getMealName())
			.toHashCode();
	}
}
