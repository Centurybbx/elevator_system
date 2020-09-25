package elevator;

import ui.ElevatorUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class Elevator implements Runnable {
    private HashSet<Integer> targetFloors = new HashSet<>();
    private boolean doorState = false;
    private int direction = 0;
    private int currentFloor = 0;
    private ElevatorUI elevatorUI;
    private int runTime = ElevatorConst.RUN_TIME;
    private int maxWaitingTime = ElevatorConst.MAX_WAITING_TIME;

    public Elevator(int initFloor, int runTime, int maxWaitingTime) {
        this.runTime = runTime;
        this.maxWaitingTime = maxWaitingTime;
        this.currentFloor = initFloor;
    }

    //设置当前的面板
    public void setPanel(ElevatorUI elevatorUI) {
        this.elevatorUI = elevatorUI;
    }

    public void upGoing() {
        this.currentFloor += 1;
        // TODO: 通知面板类更新
        elevatorUI.getLed().setText(String.valueOf(this.currentFloor));
    }

    public void downGoing() {
        this.currentFloor -= 1;
        // TODO: 通知面板更新
        elevatorUI.getLed().setText(String.valueOf(this.currentFloor));
    }

    public void openDoor() {
        this.doorState = true;
    }

    public void closeDoor() {
        this.doorState = false;
    }

    //被UI层所调用
    public void addNewFloor(int floor) {
        if (floor == this.currentFloor) {
            return;
        }
        if (this.targetFloors.isEmpty()) {
            this.direction = ((floor - this.currentFloor) / Math.abs(floor - this.currentFloor));
        }
        this.targetFloors.add(floor);
    }

    @Override
    public void run() {
        int accTimes = 0;
        while (true) {
            System.out.println("当前人数：" + elevatorUI.getNum());
            // 判断当前楼层是否在目标楼层中
            boolean remove = false;
            for (int floor : this.targetFloors) {
                if (floor == this.currentFloor) {
                    remove = true;
                    //把等待总时间归零，以便多次复用
                    accTimes = 0;
                    break;
                }
            }
            //表示已经到达目标楼层的条件
            if (remove) {
                this.targetFloors.remove(this.currentFloor);
                openDoor();
            }
            try {
                // 休眠三秒
                Thread.sleep(this.runTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 判断是否开门状态，开门则等待
            if (this.doorState) {
                continue;
            }
            // 当前目标楼层非空则再次判断方向
            if (!this.targetFloors.isEmpty()) {
                boolean upOrDown = false;
                // 判断电梯行进方向
                for (int floor : this.targetFloors) {
                    // 当前方向上是否有比当前楼层高的楼层
                    if ((floor - this.currentFloor) * this.direction > 0) {
                        upOrDown = true;
                        break;
                    }
                }
                // 翻转方向
                if (!upOrDown) {
                    this.direction = -this.direction;
                }
                if (this.direction == 1) {
                    this.upGoing();
                } else {
                    this.downGoing();
                }
            } else {
                // 否则是空闲状态
                accTimes += this.runTime;
                if (accTimes >= this.maxWaitingTime) {
                    if (this.currentFloor > 1) {
                        this.downGoing();
                    }
                    if (this.currentFloor == 0) {
                        this.upGoing();
                    }
                }
            }
            //按钮颜色变换，按下后恢复为原始的灰色
            JButton[] floorBtns = elevatorUI.getFloorBtns();
            for (int i = 0; i <= 20 ; i++) {
                if (this.currentFloor == i) {
                    floorBtns[i].setBackground(Color.GRAY);
                }
            }
        }
    }
}