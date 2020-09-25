package ui;

import elevator.Elevator;
import elevator.ElevatorConst;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorUI {
    private JButton[] floorBtns;
    private JTextField led;
    private JButton openBtn;
    private JButton closeBtn;
    private JButton addBtn;
    private JButton subBtn;
    private Elevator elevator;
    private int peopleNum = 0;

    public ElevatorUI() {
        //初始化窗口
        JFrame frame = new JFrame("电梯内部");
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //网格布局用来表示电梯按钮
        GridLayout layout = new GridLayout(10, 2, 10, 10);
        JPanel btnPanel = new JPanel(layout);

        //流式布局表示LED屏，居中显示
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //设置文本框
        led = new JTextField(3);                      //为文本框设置长度
        led.setEditable(false);
        led.setText(String.valueOf(ElevatorConst.INIT_FLOOR));      //初始化电梯
        led.setBounds(20, 20, 100, 60);
        led.setBackground(Color.BLACK);
        led.setFont(new Font(null, Font.BOLD, 50));
        led.setForeground(Color.RED);
        led.setHorizontalAlignment(JTextField.CENTER);          //字体居中显示
        textPanel.add(led);

//        初始化并给按钮赋值
        floorBtns = new JButton[21];
        for (int i = 20; i >= 0; i--) {
            floorBtns[i] = new JButton(String.valueOf(i));
            floorBtns[i].setForeground(Color.GREEN);
            floorBtns[i].setBackground(Color.GRAY);
            btnPanel.add(floorBtns[i]);
        }

        //0层楼按钮
        JPanel zeroBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        zeroBtnPanel.add(floorBtns[0]);

        //开关门按钮
        JPanel doorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        openBtn = new JButton("◀|▶");
        closeBtn = new JButton("▶|◀");
        openBtn.setBackground(Color.GRAY);
        closeBtn.setBackground(Color.GRAY);
        openBtn.setForeground(Color.BLUE);
        closeBtn.setForeground(Color.BLUE);
        doorPanel.add(openBtn);
        doorPanel.add(closeBtn);

        //加减人数按钮
        JPanel addSubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addBtn = new JButton("+");
        subBtn = new JButton("-");
        addBtn.setBackground(Color.GRAY);
        subBtn.setBackground(Color.GRAY);
        addSubPanel.add(addBtn);
        addSubPanel.add(subBtn);

        //对加减按钮增加监听器
        addOrSubListener addOrSubListener = new addOrSubListener();
        addBtn.addActionListener(addOrSubListener);
        subBtn.addActionListener(addOrSubListener);

        //创建一个垂直盒子来存储panel
        Box box = Box.createVerticalBox();
        box.add(textPanel);
        box.add(btnPanel);
        box.add(zeroBtnPanel);
        box.add(doorPanel);
        box.add(addSubPanel);

        frame.setContentPane(box);
        frame.setVisible(true);
    }

    public JTextField getLed() {
        return led;
    }

    public JButton[] getFloorBtns() {
        return floorBtns;
    }

    //得到人数
    public int getNum() {
        return peopleNum;
    }

    //传入电梯类,解耦
    public void setElevMachine(Elevator elevator) {
        this.elevator = elevator;
        int i = 0;
        for (JButton jb : this.floorBtns) {
            ButtonListener buttonListener = new ButtonListener(elevator, i);
            jb.addActionListener(buttonListener);
            i++;
        }
        DoorListener openDoorListener = new DoorListener(elevator, 1);
        DoorListener closeDoorListener = new DoorListener(elevator, -1);
        openBtn.addActionListener(openDoorListener);
        closeBtn.addActionListener(closeDoorListener);
    }

    //按钮监听器，用来对led进行显示
    class ButtonListener implements ActionListener {
        private Elevator ele;
        private int floor;

        public ButtonListener(Elevator ele, int floor) {
            this.ele = ele;
            this.floor = floor;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ele.addNewFloor(this.floor);
            for (int i = ElevatorConst.MIN_FLOOR; i <= ElevatorConst.MAX_FLOOR ; i++) {
                if (e.getSource() == floorBtns[i]) {
                    floorBtns[i].setBackground(Color.RED);
                }
            }
        }
    }

    //门监听器，用来控制开关门状态
    class DoorListener implements ActionListener {
        private Elevator ele;
        private int actionState = 0;

        public DoorListener(Elevator ele, int actionState) {
            this.ele = ele;
            this.actionState = actionState;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (this.actionState == -1) {
                this.ele.closeDoor();
                System.out.println("关门，电梯运行");
            } else {
                this.ele.openDoor();
                System.out.println("开门，电梯停止");
            }
        }
    }

    //增加或减少人数的监听器
    class addOrSubListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("+")) {
                peopleNum += 1;
            } else {
                peopleNum -= 1;
            }
            //如果电梯人数超最大限制人数，那么就使电梯关闭按钮失效
            if (peopleNum > ElevatorConst.MAX_PEOPLE) {
                closeBtn.setEnabled(false);
            } else {
                closeBtn.setEnabled(true);
            }
        }
    }
}
