package br.com.amsj.spring.oauth.client.model;

import java.io.Serializable;

public class Car implements Serializable {
	
	private static final long serialVersionUID = 3723556615748671100L;
	
	private Long id;
	private String model;
	private String brand;
	private String version;
	
	public Car() {}
	
	public Car(Long id, String model, String brand, String version) {
		this.id = id;
		this.model = model;
		this.brand = brand;
		this.version = version;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
