package com.java.windows.tools;

import com.java.windows.tools.component.AppUtils;
import com.java.windows.tools.model.AppConfig;
import com.java.windows.tools.model.AppDesc;
import com.java.windows.tools.model.AppView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class Application {

    public static AppDesc desc;
    public static AppConfig config;
    public static CardLayout cardLayout = new CardLayout();

    public static void main(String[] args) {
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (checkInit()) {
                createAndShowGUI();
            }
        });
    }

    /**
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static boolean checkInit() {
        boolean result = true;
        try {
            desc = AppUtils.getDesc();
            config = AppUtils.getConfig();
        } catch (Exception e) {
            log.error("获取配置信息失败, 原因: {}", e.getMessage(), e);
            result = false;
            JFrame frame = new JFrame("Java-Windows-Tools");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 100);// 设置窗体大小
            frame.setLocationRelativeTo(null);// 设置窗体居中
            JLabel label = new JLabel(e.getMessage());
            label.setHorizontalAlignment(JLabel.CENTER);//字体居中
            frame.getContentPane().add(label);
            frame.setVisible(true);
        }
        return result;
    }

    /**
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    @SneakyThrows
    private static void createAndShowGUI() {
        // 创建及设置窗口
        JFrame frame = new JFrame("Java-Windows-Tools");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);// 设置窗体大小
        frame.setLocationRelativeTo(null);// 设置窗体居中
        // 创建主布局
        JPanel appPanel = new JPanel();
        appPanel.setLayout(new BorderLayout());// 边框布局
        frame.setContentPane(appPanel);
        // 创建切换面板
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);
        appPanel.add(cardPanel, BorderLayout.CENTER);
        // 创建菜单布局
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 20));// 流式布局
        cardPanel.add(menuPanel, "main_menu_panel");
        // 添加标签
        JLabel label = new JLabel("功能列表");
        label.setHorizontalAlignment(JLabel.CENTER);//字体居中
        menuPanel.add(label);
        // 根据描述文件生成功能按钮
        AppDesc appDesc = AppUtils.getDesc();
        for (AppView view : appDesc.getViews()) {
            // 添加功能面板
            Object viewObject = Application.class.getClassLoader().loadClass(view.getClz()).getDeclaredConstructor().newInstance();
            cardPanel.add(view.getName(), (Component) viewObject);
            // 添加功能按钮
            JButton viewBtn = new JButton(view.getName());
            viewBtn.addActionListener(e -> {
                cardLayout.show(cardPanel, view.getName());
            });
            menuPanel.add(viewBtn);
        }
        // 添加主菜单按钮
        JButton menuBtn = new JButton("主菜单");
        menuBtn.addActionListener(e -> {
            cardLayout.show(cardPanel, "main_menu_panel");
        });
        appPanel.add(menuBtn, BorderLayout.SOUTH);
        // 显示窗口
        frame.setVisible(true);
    }
}
