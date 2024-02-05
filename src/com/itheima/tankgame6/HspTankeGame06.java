package com.itheima.tankgame6;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * Frame窗口
 */
public class HspTankeGame06 extends JFrame {
    //定义一个MyPanels
    MyPanel mp = null;
    static Scanner scanner = new Scanner( System.in);

    public static void main(String[] args) {
        HspTankeGame06 hspTankeGame04 = new HspTankeGame06(); //调用构造器，完成初始化
    }

    public HspTankeGame06(){  //在构造器里设置Frame的初始化内容

        System.out.println("Please enter your selection, 1 represents a new game, 2 represents continuing the previous game");
        String key = scanner.next();

        mp = new MyPanel(key);  //首先调用构造器完成panel的初始化
        new Thread(mp).start();//将mp放入到Thread，并启动

        this.add(mp);//把panel（游戏的绘图区域）添加到frame
        this.setSize(1300,800); //设置Size
        this.addKeyListener(mp);  //让JFrame监听mp的键盘事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭模式
        this.setVisible(true);//设置显示模式

        //在JFrame增加响应关闭窗口的处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.saveRecord();
                System.exit(0);
            }
        });
    }
}
