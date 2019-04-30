public class FLPResult {
    private SphereOfInfluence sphereOfInfluence;
    private String destinationNode;
    private double cost;

    public FLPResult() {
    }

    public FLPResult(SphereOfInfluence sphereOfInfluence, String destinationNode, double cost) {
        this.sphereOfInfluence = sphereOfInfluence;
        this.destinationNode = destinationNode;
        this.cost = cost;
    }

    public SphereOfInfluence getSphereOfInfluence() {
        return sphereOfInfluence;
    }

    public void setSphereOfInfluence(SphereOfInfluence sphereOfInfluence) {
        this.sphereOfInfluence = sphereOfInfluence;
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
