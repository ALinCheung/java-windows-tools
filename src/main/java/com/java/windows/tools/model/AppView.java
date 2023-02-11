package com.java.windows.tools.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppView {

    private String name;

    private String clz;
}
