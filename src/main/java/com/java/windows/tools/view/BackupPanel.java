package com.java.windows.tools.view;

import com.java.windows.tools.Application;
import com.java.windows.tools.component.AppUtils;
import com.java.windows.tools.component.BackupUtils;
import com.java.windows.tools.model.BackupConfig;
import com.java.windows.tools.view.common.MessageDialog;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BackupPanel extends JPanel {

    public BackupPanel() {
        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        this.add(new JScrollPane(panel), BorderLayout.CENTER);

        updatePanel(panel);

        JPanel spanel = new JPanel();
        spanel.setLayout(new GridLayout(1, 2));
        JButton addBtn = new JButton("新增");
        addBtn.addActionListener(e -> {
            configDialog(null, panel);
        });
        spanel.add(addBtn);
        JButton backupBtn = new JButton("备份");
        backupBtn.addActionListener(e -> {
            String msg = BackupUtils.execute(Application.config.getBackup());
            if (StringUtils.isBlank(msg)) {
                new MessageDialog("备份成功!");
            } else {
                new MessageDialog(msg);
            }
        });
        spanel.add(backupBtn);
        this.add(spanel, BorderLayout.SOUTH);
    }

    private static void updatePanel(JPanel panel) {
        panel.setLayout(new GridLayout(Application.config.getBackup().size(), 1));
        panel.removeAll();
        for (BackupConfig config : Application.config.getBackup()) {
            JPanel cpanel = new JPanel();
            cpanel.setLayout(null);

            JLabel label = new JLabel(config.getName());
            label.setSize(200, 20);
            label.setLocation(0, 0);
            cpanel.add(label);
            JButton cBtn = new JButton("配置");
            cBtn.setSize(60, 20);
            cBtn.setLocation(170, 0);
            cBtn.addActionListener(e -> {
                configDialog(config, panel);
            });
            cpanel.add(cBtn);

            JButton dBtn = new JButton("删除");
            dBtn.setSize(60, 20);
            dBtn.setLocation(220, 0);
            dBtn.addActionListener(e -> {
                deleteDialog(config, (o) -> {
                    panel.remove(cpanel);
                    panel.updateUI();
                });
            });
            cpanel.add(dBtn);

            panel.add(cpanel);
        }
        panel.updateUI();
    }

    private static void configDialog(BackupConfig config, JPanel panel) {
        String name = config != null ? config.getName() : null;
        String source = config != null ? config.getSource() : null;
        String target = config != null ? config.getTarget() : null;

        JDialog jDialog = new JDialog();
        jDialog.setTitle("配置");
        jDialog.setVisible(true);
        jDialog.setSize(600, 250);
        jDialog.setLocationRelativeTo(null);// 设置窗体居中
        jDialog.setLayout(new GridLayout(4, 1));
        Container contentPane = jDialog.getContentPane();

        JPanel namePanel = new JPanel();
        namePanel.setLayout(null);
        JLabel nameLabel = new JLabel("备份名称: ");
        nameLabel.setSize(100, 25);
        nameLabel.setLocation(0, 5);
        namePanel.add(nameLabel);
        JTextField nameText = new JTextField(name);
        nameText.setSize(400, 25);
        nameText.setLocation(70, 5);
        namePanel.add(nameText);
        contentPane.add(namePanel);

        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(null);
        JLabel sourceLabel = new JLabel("原地址: ");
        sourceLabel.setSize(100, 25);
        sourceLabel.setLocation(0, 5);
        sourcePanel.add(sourceLabel);
        JTextField sourceText = new JTextField(source);
        sourceText.setSize(400, 25);
        sourceText.setLocation(70, 5);
        sourcePanel.add(sourceText);
        JButton sourceBtn = new JButton("选择");
        sourceBtn.setSize(100, 25);
        sourceBtn.setLocation(480, 5);
        sourceBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // fileChooser.setMultiSelectionEnabled(true); //选择多个
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int option = fileChooser.showOpenDialog(jDialog);
            if (option == JFileChooser.APPROVE_OPTION) {
                sourceText.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        sourcePanel.add(sourceBtn);
        contentPane.add(sourcePanel);

        JPanel targetPanel = new JPanel();
        targetPanel.setLayout(null);
        JLabel targetLabel = new JLabel("目标地址: ");
        targetLabel.setSize(100, 25);
        nameLabel.setLocation(0, 5);
        targetPanel.add(targetLabel);
        JTextField targetText = new JTextField(target);
        targetText.setSize(400, 25);
        targetText.setLocation(70, 5);
        targetPanel.add(targetText);
        JButton targetBtn = new JButton("选择");
        targetBtn.setSize(100, 25);
        targetBtn.setLocation(480, 5);
        targetBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // fileChooser.setMultiSelectionEnabled(true); //选择多个
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int option = fileChooser.showOpenDialog(jDialog);
            if (option == JFileChooser.APPROVE_OPTION) {
                targetText.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        targetPanel.add(targetBtn);
        contentPane.add(targetPanel);

        JPanel savePanel = new JPanel();
        savePanel.setLayout(null);
        JButton saveBtn = new JButton("保存");
        saveBtn.setSize(150, 25);
        saveBtn.setLocation(70, 5);
        saveBtn.addActionListener(e -> {
            if (config != null) {
                config.setName(nameText.getText());
                config.setSource(sourceText.getText());
                config.setTarget(targetText.getText());
            } else {
                Application.config.getBackup().add(new BackupConfig(nameText.getText(), sourceText.getText(), targetText.getText()));
                updatePanel(panel);
            }
            // 保存至配置文件
            AppUtils.setConfig(Application.config, Application.appConfigPath);
            jDialog.dispose();
        });
        savePanel.add(saveBtn);
        contentPane.add(savePanel);
    }

    private static void deleteDialog(BackupConfig config, Consumer consumer) {
        JDialog jDialog = new JDialog();
        jDialog.setTitle("删除配置");
        jDialog.setVisible(true);
        jDialog.setSize(300, 100);
        jDialog.setLocationRelativeTo(null);// 设置窗体居中
        jDialog.setLayout(new GridLayout(2, 1));
        Container contentPane = jDialog.getContentPane();

        JLabel label = new JLabel("确定要删除配置吗?");
        contentPane.add(label);

        JButton confirmBtn = new JButton("确定");
        confirmBtn.setSize(150, 25);
        confirmBtn.addActionListener(e -> {
            Application.config.getBackup().remove(config);
            // 保存至配置文件
            AppUtils.setConfig(Application.config, Application.appConfigPath);
            consumer.accept(null);
            jDialog.dispose();
        });
        contentPane.add(confirmBtn);
    }
}
