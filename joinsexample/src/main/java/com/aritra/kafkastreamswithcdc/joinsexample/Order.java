package com.aritra.kafkastreamswithcdc.joinsexample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order implements JSONSerdeCompatible {
    private Integer orderid;
    private Integer userid;
    private String itemName;
}
