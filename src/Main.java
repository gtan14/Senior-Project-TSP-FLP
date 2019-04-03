import org.jgrapht.Graph;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class Main {

    private static Graph<String, DefaultWeightedEdge> completeGraph;
    private static LinkedList<Order> orders;

    public static void main(String[] args) {
        orders = new LinkedList<>();
        initializeGraph(5);

        TSP tsp = new TSP(completeGraph);
        BaseLocation baseLocation = tsp.getBaseLocation(completeGraph.vertexSet());
    }

    //  method that initializes a complete weighted undirected graph
    private static void initializeGraph(int numOfNodes){

        // Create the VertexFactory so the generator can create vertices
        Supplier<String> vSupplier = new Supplier<String>()
        {
            private int id = 0;

            @Override
            public String get()
            {
                return "v" + id++;
            }
        };

        completeGraph = new DefaultUndirectedWeightedGraph<>(vSupplier, SupplierUtil.createDefaultWeightedEdgeSupplier());

        // Create the CompleteGraphGenerator object
        CompleteGraphGenerator<String, DefaultWeightedEdge> completeGenerator =
                new CompleteGraphGenerator<>(numOfNodes);

        // Use the CompleteGraphGenerator object to make completeGraph a
        // complete graph with [size] number of vertices
        completeGenerator.generateGraph(completeGraph);
        // Print out the graph to be sure it's really complete
        Iterator<String> iter = new DepthFirstIterator<>(completeGraph);
        while (iter.hasNext()) {
            String vertex = iter.next();
            System.out.println(
                    "Vertex " + vertex + " is connected to: "
                            + completeGraph.edgesOf(vertex).toString());
            Set<DefaultWeightedEdge> set = completeGraph.edgesOf(vertex);
            for(DefaultWeightedEdge edge: set){
                //  set edge weight to random number between 1-10
                completeGraph.setEdgeWeight(edge, ThreadLocalRandom.current().nextInt(1, 11));
                System.out.println(completeGraph.getEdgeWeight(edge));
            }
        }
    }

    //  method that generates "orders" at random vertices
    private static void generateOrder(){
        Set<String> nodes = completeGraph.vertexSet();

        //  convert set to list so that a random index can be used to get a random node
        List<String> nodesList = new ArrayList<>(nodes);

        //  generate random number
        int randomNumber = ThreadLocalRandom.current().nextInt(0, nodes.size());

        //  create new order
        Order order = new Order();
        order.setNode(nodesList.get(randomNumber));

        orders.add(order);
    }
}
