import java.util.Timer;

public class Order {
    private String node;
    private boolean serviced;
    private long startTime;

    public Order(String node) {
        this.node = node;
        serviced = false;
        startTime = System.nanoTime();
    }

    public Order() {
        startTime = System.nanoTime();
    }

    public void robotSent(double cost){
        //  speed of robot is .05 cm per second
        /*
        System.out.println("COST: " + cost);
        System.out.println("START TIME: " + startTime);
        System.out.println("NANO: " + System.nanoTime());*/
        long distanceTime = (long) (cost / .05);
        long time = (System.nanoTime() - startTime) / 1000000000;
        long newTime = time + distanceTime;
        int hours = (int) newTime / 3600;
        int minutes = ((int)newTime % 3600) / 60;
        long seconds = newTime % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        //Main.orders.remove(Order.this);

        /*
        for(int i = Main.orders.size() - 1; i >= 0; i--){
            System.out.println();
            if(Main.orders.get(i).equals(Order.this)){
                //System.out.println("i: " + i);
                Main.orders.remove(i);
            }
        }*/

        System.out.println("TIME: " + timeString);
        /*
        long delay = (long) (cost / 1) * 1000;
        Timer timer = new Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("NUMBER OF ORDERS: " + Main.orders.size());
                        for(int i = Main.orders.size() - 1; i >= 0; i--){
                            if(Main.orders.get(i).equals(Order.this)){
                                System.out.println("Order: " + Main.orders.get(i).getNode() + " delivered. Time Elapsed: " + ((System.nanoTime() - Main.orders.get(i).startTime) / 1000000000));
                                Main.orders.remove(i);
                            }
                        }
                        timer.cancel();
                    }
                },
                delay
        );*/


    }

    public boolean isServiced() {
        return serviced;
    }

    public void setServiced(boolean serviced) {
        this.serviced = serviced;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

}
