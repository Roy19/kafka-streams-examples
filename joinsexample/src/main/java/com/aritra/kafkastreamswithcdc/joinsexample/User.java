package com.aritra.kafkastreamswithcdc.joinsexample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements JSONSerdeCompatible {
    private Integer id;
    private String username;    
}
