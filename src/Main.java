import elevator.Elevator;
import elevator.ElevatorConst;
import ui.ElevatorUI;

public class Main {
    public static void main(String[] args) {
        ElevatorUI elevatorUI = new ElevatorUI();
        Elevator elevator = new Elevator(ElevatorConst.INIT_FLOOR, ElevatorConst.RUN_TIME,
                ElevatorConst.MAX_WAITING_TIME);
        //下面两个set方法是互相调用
        elevatorUI.setElevMachine(elevator);
        elevator.setPanel(elevatorUI);
        Thread t = new Thread(elevator);
        t.start();
    }
}
