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
    }

    public void robotSent(double cost){
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
        );
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
