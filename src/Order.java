public class Order {
    private String node;
    private double timeElapsed;

    public Order(String node, double timeElapsed) {
        this.node = node;
        this.timeElapsed = timeElapsed;
    }

    public Order() {
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(double timeElapsed) {
        this.timeElapsed = timeElapsed;
    }
}
