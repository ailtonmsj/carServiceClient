package br.com.amsj.spring.oauth.client.model;

import java.io.Serializable;

import br.com.amsj.spring.oauth.client.enums.Gender;
import br.com.amsj.spring.oauth.client.enums.Status;

public class Client implements Serializable {
	
	private static final long serialVersionUID = -9203433758819169303L;	
			
	private Long id;
	private String firstName;
	private String lastName;
	private Gender gender;
	private Status status;
	private Car car;
	
	public Client() {}
	
	public Client(Long id, String firstName, String lastName, Gender gender, Status status, Car car) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.status = status;
		this.car = car;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
