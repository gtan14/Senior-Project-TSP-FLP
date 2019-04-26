import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderFunction {
    private SphereOfInfluence sphereOfInfluence;
    private Graph<String, DefaultWeightedEdge> completeGraph;
    private List<Order> orders;
    private DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath;

    public OrderFunction(SphereOfInfluence sphereOfInfluence, Graph<String, DefaultWeightedEdge> completeGraph, List<Order> orders){
        this.sphereOfInfluence = sphereOfInfluence;
        this.completeGraph = completeGraph;
        this.orders = orders;
        dijkstraShortestPath = new DijkstraShortestPath<>(completeGraph);
    }

    public OrderFunction(Graph<String, DefaultWeightedEdge> completeGraph){
        this.completeGraph = completeGraph;
        dijkstraShortestPath = new DijkstraShortestPath<>(completeGraph);
    }

    public OrderFunctionResult GetNextMove(String secondFacility){
        List<String> nodesInSphere = new ArrayList<>(sphereOfInfluence.getNodesInSphere());
        List<String> orderNodes = new ArrayList<>();
        List<OrderFunctionResult> nextMoves = new ArrayList<>();
        List<String> neighborNodes = new ArrayList<>(Graphs.neighborListOf(completeGraph, sphereOfInfluence.getFacilityPosition()));

        //  remove the other facilities node from neighbor nodes
        neighborNodes.remove(secondFacility);

        //  get list with just order nodes
        for(int i = 0; i < orders.size(); i++){
            //  only add orders that haven't been serviced yet
            if(!orders.get(i).isServiced()) {
                orderNodes.add(orders.get(i).getNode());
            }
        }

        //  get a list of orders inside of the sphere of influence
        nodesInSphere.retainAll(orderNodes);

        for(int i = 0; i < neighborNodes.size(); i++){
            OrderFunctionResult orderFunctionResult = new OrderFunctionResult(neighborNodes.get(i), CalculateTotalCost(nodesInSphere, neighborNodes.get(i), sphereOfInfluence.getFacilityPosition()), nodesInSphere);
            nextMoves.add(orderFunctionResult);
        }

        Comparator<OrderFunctionResult> comparator = new Comparator<OrderFunctionResult>() {
            public int compare(OrderFunctionResult h1, OrderFunctionResult h2) {
                return Double.compare(h1.getCost(), (h2.getCost()));
            }
        };

        nextMoves.sort(comparator);
        return nextMoves.get(0);
    }

    private double CalculateTotalCost(List<String> ordersInSphere, String newFacilityPosition, String currentFacilityPosition){
        double cost = 0;
        for(int i = 0; i < ordersInSphere.size(); i++){
            cost += dijkstraShortestPath.getPathWeight(newFacilityPosition, ordersInSphere.get(i));
        }

        //  add the cost to travel to new facility location
        DefaultWeightedEdge defaultWeightedEdge = completeGraph.getEdge(newFacilityPosition, currentFacilityPosition);
        cost += Main.completeTruckGraph.getEdgeWeight(defaultWeightedEdge);

        return cost;
    }

    public SphereOfInfluence getSphereOfInfluence() {
        return sphereOfInfluence;
    }

    public void setSphereOfInfluence(SphereOfInfluence sphereOfInfluence) {
        this.sphereOfInfluence = sphereOfInfluence;
    }

    public Graph<String, DefaultWeightedEdge> getCompleteGraph() {
        return completeGraph;
    }

    public void setCompleteGraph(Graph<String, DefaultWeightedEdge> completeGraph) {
        this.completeGraph = completeGraph;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
