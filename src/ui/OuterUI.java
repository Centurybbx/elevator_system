package ui;

import elevator.Elevator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OuterUI {
    private JButton[] floorBtns;
    private JButton[] lightBtns;
    //private Panel panel;
    private int[] panelArray = new int[21];
    private Elevator elevator;


    public OuterUI() {
        JFrame frame = new JFrame("外电梯");
        frame.setSize(400, 680);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //创建绝对布局
        JPanel jPanel = new JPanel(null);
        //创建按钮
        int leftFlag = -20, rightFlag = -5;
        //两个xxx
        floorBtns = new JButton[21];
        //对楼梯进行初始化
        for (int i = 20; i >= 0; i--) {
            floorBtns[i] = new JButton(String.valueOf(i));
            leftFlag += 30;
            floorBtns[i].setBounds(20,leftFlag,180,30);
            jPanel.add(floorBtns[i]);
        }

        lightBtns = new JButton[42];
        //对上下按钮进行初始化
        for (int i = 1; i <= 41; i++) {
            if (i % 2 == 1) {
                lightBtns[i] = new JButton("↑");
                rightFlag += 15;
                lightBtns[i].setBounds(200,rightFlag,100,15);
                jPanel.add(lightBtns[i]);
                //为按钮增加监听器
                lightBtns[i].addActionListener(new LightListener(0));
            } else {
                lightBtns[i] = new JButton("↓");
                rightFlag += 15;
                lightBtns[i].setBounds(200,rightFlag,100,15);
                jPanel.add(lightBtns[i]);
                lightBtns[i].addActionListener(new LightListener(0));
            }
        }

        //加人数
        JButton addBtn = new JButton("+");
        addBtn.setBounds(320,150,60,60);
        jPanel.add(addBtn);
        //减人数
        JButton subBtn = new JButton("-");
        subBtn.setBounds(320,450,60,60);
        jPanel.add(subBtn);

        Box box = Box.createVerticalBox();
        box.add(jPanel);

        frame.setContentPane(box);
        frame.setVisible(true);
    }

    public void setElevMachine(Elevator elevMachinev){
        this.elevator = elevMachinev;
    }

    private void clearBtn(int currentfloor, int moveflag) {

    }

    public boolean stopornot(int currentfloor,int moveflag) {
        if(panelArray[currentfloor] == moveflag || panelArray[currentfloor] == 2) {
            // 清空上下按钮当前层的红色 传入参数为当前层数 清除的状态根据moveflag的不同而不同
            this.clearBtn(currentfloor, moveflag);
            if (panelArray[currentfloor] == 2) {
                panelArray[currentfloor] = -moveflag;
            } else {
                panelArray[currentfloor] = 0;
            }
            return true;
        }
        return false;
    }

    class LightListener implements ActionListener {
        private int code = 0;

        public LightListener(int code) {
            this.code = code;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 按钮变色
            for (int i = 0; i < 41; i++) {
                if (lightBtns[i] == e.getSource()) {
                    lightBtns[i].setBackground(Color.RED);
                }
            }
        }
    }

    public static void main(String[] args) {
        new OuterUI();
    }
}
