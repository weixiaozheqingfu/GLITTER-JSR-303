package org.glitter.jsr303.model;

import org.glitter.jsr303.constraint.Price;
import org.hibernate.validator.constraints.NotBlank;

public class ProductChild {
	@NotBlank
	private String productChildName;
	@Price
	private float productChildPrice;
	
	
	public String getProductChildName() {
		return productChildName;
	}
	public void setProductChildName(String productChildName) {
		this.productChildName = productChildName;
	}
	public float getProductChildPrice() {
		return productChildPrice;
	}
	public void setProductChildPrice(float productChildPrice) {
		this.productChildPrice = productChildPrice;
	}


}
