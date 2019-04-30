import java.util.List;

public class OrderFunctionResult {
    private String destinationNode;
    private List<String> ordersInInfluence;
    private double cost;

    public OrderFunctionResult(String destinationNode, double cost, List<String> ordersInInfluence) {
        this.destinationNode = destinationNode;
        this.cost = cost;
        this.ordersInInfluence = ordersInInfluence;
    }

    public OrderFunctionResult() {
    }

    public List<String> getOrdersInInfluence() {
        return ordersInInfluence;
    }

    public void setOrdersInInfluence(List<String> ordersInInfluence) {
        this.ordersInInfluence = ordersInInfluence;
    }

    public String getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(String destinationNode) {
        this.destinationNode = destinationNode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
