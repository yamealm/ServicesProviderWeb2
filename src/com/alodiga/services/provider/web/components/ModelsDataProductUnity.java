package com.alodiga.services.provider.web.components;

import com.alodiga.services.provider.commons.models.Product;

public class ModelsDataProductUnity {
	private Product product;
    private String partNumber;
    private String description;
    private float amount;

	
	
	public Product getProduct(){
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}
