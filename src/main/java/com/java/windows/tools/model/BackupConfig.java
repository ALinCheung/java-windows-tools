package com.java.windows.tools.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BackupConfig {

    private String source;

    private String target;
}
