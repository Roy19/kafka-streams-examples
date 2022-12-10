package com.aritra.kafkastreamswithcdc.joinsexample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserOrder implements JSONSerdeCompatible {
	private Integer userid;
	private Integer orderid;
	private String username;
	private String itemName;
}
