package popcorndemo;
import java.util.ArrayList;

class OutputBin {

    private static final ArrayList<Popcorn> popcorn = new ArrayList<>();

    public synchronized void get() {
        while (popcorn.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        notifyAll();
        popcorn.remove(0);
    }

    public synchronized void put(Popcorn p) {
        while (popcorn.size() == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        popcorn.add(p);
        notifyAll();
    }
}

class RoboArm extends Thread {

    private final OutputBin outputbin;
    private final int number;

    public RoboArm(OutputBin b, int number) {
        outputbin = b;
        this.number = number;
    }

    @Override
    public void run() {
        int value = 0;
        for (int i = 1; i <= number; i++) {
            outputbin.get();
            System.out.println("Robo arm got popcorn : " + i);
        }
    }
}

class PopcornMachine extends Thread {

    private final OutputBin outputbin;
    private final int number;

    public PopcornMachine(OutputBin b, int number) {
        outputbin = b;
        this.number = number;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        for (int i = 1; i <= number; i++) {
            outputbin.put(new Popcorn());
            System.out.println("Popcorn machine put popcorn : " + i);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}

class Popcorn {
}

public class PopcornDemo {

    public static void main(String[] args) {
        OutputBin b = new OutputBin();
        PopcornMachine m = new PopcornMachine(b, 10);
        RoboArm r = new RoboArm(b, 10);
        m.start();
        r.start();
    }
}
