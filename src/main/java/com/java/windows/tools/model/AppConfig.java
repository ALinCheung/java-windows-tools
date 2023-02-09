package com.java.windows.tools.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AppConfig {

    private List<BackupConfig> backup;
}
