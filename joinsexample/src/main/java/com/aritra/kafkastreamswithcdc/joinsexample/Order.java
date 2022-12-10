package com.aritra.kafkastreamswithcdc.joinsexample;

public class Order implements JSONSerdeCompatible {
    public int orderid;
    public int userid;
    public String itemName;
    
    public Order() {
    }

	public Order(int orderid, int userid, String itemName) {
		super();
		this.orderid = orderid;
		this.userid = userid;
		this.itemName = itemName;
	}
}
