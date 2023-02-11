package com.java.windows.tools.view.common;

import javax.swing.*;
import java.awt.*;

public class MessageDialog extends JDialog {

    public MessageDialog(String msg) {
        JDialog jDialog = new JDialog();
        jDialog.setTitle("消息");
        jDialog.setVisible(true);
        jDialog.setSize(300, 200);
        jDialog.setLocationRelativeTo(null);// 设置窗体居中
        jDialog.setLayout(new GridLayout(1, 1));
        Container contentPane = jDialog.getContentPane();

        JLabel label = new JLabel("<html>" + msg + "</html>");
        label.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(label);
    }
}
