package com.recruitsystem.myapplication.data.Bean;

public class JobCollectedBean {
	private Integer id;
	private String userPhone;
	private Integer collectedId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public Integer getCollectedId() {
		return collectedId;
	}
	public void setCollectedId(Integer collectedId) {
		this.collectedId = collectedId;
	}
	@Override
	public String toString() {
		return "JobCollectedBean [id=" + id + ", userPhone=" + userPhone + ", collectedId=" + collectedId + "]";
	}
	
}
