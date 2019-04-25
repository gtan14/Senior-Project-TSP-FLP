import java.util.ArrayList;
import java.util.List;

public class SphereOfInfluence{
    private double totalCost;
    private int numNodes;
    private String facilityPosition;
    private List<String> nodesInSphere;

    public SphereOfInfluence(){

    }

    public SphereOfInfluence(double totalCost, int numNodes, String facilityPosition) {
        this.totalCost = totalCost;
        this.numNodes = numNodes;
        this.facilityPosition = facilityPosition;
        nodesInSphere = new ArrayList<>();
    }

    public void addNode(String node){
        nodesInSphere.add(node);
    }

    public List<String> getNodesInSphere(){
        return nodesInSphere;
    }

    public String getFacilityPosition() {
        return facilityPosition;
    }

    public void setFacilityPosition(String facilityPosition) {
        this.facilityPosition = facilityPosition;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void addCost(double cost){
        totalCost += cost;
    }

    public void incrementNumNodes(){
        numNodes++;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public void setNumNodes(int numNodes) {
        this.numNodes = numNodes;
    }
}