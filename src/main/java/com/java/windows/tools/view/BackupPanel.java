package com.java.windows.tools.view;

import com.java.windows.tools.Application;
import com.java.windows.tools.component.BackupUtils;

import javax.swing.*;

public class BackupPanel extends JPanel {

    public BackupPanel() {
        JButton backupBtn = new JButton("备份");
        backupBtn.addActionListener(e -> BackupUtils.execute(Application.config.getBackup()));
        this.add(backupBtn);
    }
}
