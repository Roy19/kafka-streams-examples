package com.aritra.kafkastreamswithcdc.joinsexample;

public class Order implements JSONSerdeCompatible {
    public Integer orderid;
    public Integer userid;
    public String itemName;
    
    public Order() {
    }

	public Order(Integer orderid, Integer userid, String itemName) {
		super();
		this.orderid = orderid;
		this.userid = userid;
		this.itemName = itemName;
	}

	public int getOrderid() {
		return orderid;
	}

	public int getUserid() {
		return userid;
	}

	public String getItemName() {
		return itemName;
	}
}
