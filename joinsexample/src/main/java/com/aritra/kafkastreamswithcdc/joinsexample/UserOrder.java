package com.aritra.kafkastreamswithcdc.joinsexample;

public class UserOrder implements JSONSerdeCompatible {
	public Integer userid;
	public Integer orderid;
	public String username;
	public String itemName;
	
	public UserOrder() {
	}

	public UserOrder(Integer userId, Integer orderId, String userName, String itemName) {
		super();
		this.userid = userId;
		this.orderid = orderId;
		this.username = userName;
		this.itemName = itemName;
	}

	public Integer getUserId() {
		return userid;
	}

	public void setUserId(Integer userId) {
		this.userid = userId;
	}

	public Integer getOrderId() {
		return orderid;
	}

	public String getUserName() {
		return username;
	}

	public String getItemName() {
		return itemName;
	}
}
