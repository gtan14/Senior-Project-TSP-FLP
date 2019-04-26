import javafx.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jgrapht.Graph;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.apache.commons.lang3.*;

public class Main {

    private static Graph<String, DefaultWeightedEdge> completeGraph;
    public static Graph<String, DefaultWeightedEdge> completeTruckGraph;
    public static volatile CopyOnWriteArrayList<Order> orders;
    public static double flpCoefficient, orderFunctionCoefficient;
    public static int numOrdersCap;
    public static OrderGenerator orderGenerator;
    private static Timer timer;

    public static void main(String[] args) {
        orders = new CopyOnWriteArrayList<>();
        initializeGraph(22);

        /*
        TODO: CREATE GUI WITH GRAPH OVERLAY
         */

        numOrdersCap = 0;
        flpCoefficient = 1;
        orderFunctionCoefficient = 1;
        timer = new Timer();
        boolean firstFacilityTurn = true;


        TSP tsp = new TSP(completeGraph);
        Map<String, BaseLocation> baseLocationMap = tsp.getBaseLocation(completeGraph.vertexSet());
        String firstFacilityLocation = "";
        String secondFacilityLocation = "";

        for(Map.Entry<String, BaseLocation> entry: baseLocationMap.entrySet()){
            BaseLocation baseLocation = entry.getValue();
            if(entry.getKey().equals("first")){
                firstFacilityLocation = baseLocation.getFirst();

                System.out.println("FIRST FACILITY: " + baseLocation.getFirst());
                System.out.println("FIRST FACILITY: " + baseLocation.getSecond());
                System.out.println("FIRST FACILITY: " + baseLocation.getThird());

                completeTruckGraph.setEdgeWeight(baseLocation.getFirst(), baseLocation.getSecond(), 1.0);
                completeTruckGraph.setEdgeWeight(baseLocation.getFirst(), baseLocation.getThird(), 1.0);
                completeTruckGraph.setEdgeWeight(baseLocation.getSecond(), baseLocation.getThird(), 1.0);

                /*
                TODO: MARK BASE LOCATIONS FOR FIRST FACILITY
                 */
            }

            else if(entry.getKey().equals("second")){
                secondFacilityLocation = baseLocation.getFirst();

                System.out.println("SECOND FACILITY: " + baseLocation.getFirst());
                System.out.println("SECOND FACILITY: " + baseLocation.getSecond());
                System.out.println("SECOND FACILITY: " + baseLocation.getThird());

                completeTruckGraph.setEdgeWeight(baseLocation.getFirst(), baseLocation.getSecond(), 1.0);
                completeTruckGraph.setEdgeWeight(baseLocation.getFirst(), baseLocation.getThird(), 1.0);
                completeTruckGraph.setEdgeWeight(baseLocation.getSecond(), baseLocation.getThird(), 1.0);

                /*
                TODO: MARK BASE LOCATIONS FOR SECOND FACILITY
                 */
            }

            System.out.println();
        }

        System.out.println();

        orderGenerator = new OrderGenerator();

        orderGenerator.run();

        try
        {
            Thread.sleep(2000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        FLP flp = new FLP(completeTruckGraph);
        OrderFunction orderFunction = new OrderFunction(completeGraph);

        while(orders.size() > 0){
            promptEnterKey();

            if(firstFacilityTurn){
                System.out.println("ORDER SIZE: " + orders.size());
                //  first facility movement
                flp.setFirstFacilityTurn();
                FLPResult flpResult = flp.GetNextBestMove(firstFacilityLocation, secondFacilityLocation);
                Pair<SphereOfInfluence, SphereOfInfluence> sphereOfInfluencePair = flp.CalculateSphereOfInfluence(firstFacilityLocation, secondFacilityLocation);
                orderFunction.setSphereOfInfluence(sphereOfInfluencePair.getKey());
                orderFunction.setOrders(orders);
                OrderFunctionResult orderFunctionResult = orderFunction.GetNextMove(secondFacilityLocation);

                double flpCost = flpResult.getCost() * flpCoefficient;
                double orderCost = orderFunctionResult.getCost() * orderFunctionCoefficient;
                String facilityLocationBeforeMove = firstFacilityLocation;

                //  move according to flp
                if(flpCost < orderCost){
                    //System.out.println("First FLP");
                    flpCoefficient += .1;
                    if(orderFunctionCoefficient >= .1) {
                        orderFunctionCoefficient -= .1;
                    }
                    firstFacilityLocation = flpResult.getDestinationNode();
                }

                //  move according to order
                else{
                    //System.out.println("First Order");
                    if(flpCoefficient >= .1) {
                        flpCoefficient -= .1;
                    }
                    orderFunctionCoefficient += .1;
                    firstFacilityLocation = orderFunctionResult.getDestinationNode();
                }

                MarkOrdersAsServiced(firstFacilityLocation);
                SendRobots(orderFunctionResult, facilityLocationBeforeMove);

                firstFacilityTurn = false;

                System.out.println("First Facility Turn");
                System.out.println("Before: " + facilityLocationBeforeMove);
                System.out.println("After: " + firstFacilityLocation);

                /*
                TODO: THIS IS THE FIRST FACILITY MOVEMENT
                TODO: facilityLocationBeforeMove IS THE NODE FACILITY WAS AT BEFORE MOVING
                TODO: firstFacilityLocation IS THE NODE FACILITY MOVES TO
                 */
            }

            else{
                System.out.println("ORDER SIZE: " + orders.size());

                flp.setSecondFacilityTurn();
                FLPResult flpResult = flp.GetNextBestMove(firstFacilityLocation, secondFacilityLocation);
                //  flip parameter order and have second facility in first parameter
                //  this is because CalculateSphereOfInfluence() uses the first parameter as the location that moves first, which in this case should be the second facility
                Pair<SphereOfInfluence, SphereOfInfluence> sphereOfInfluencePair = flp.CalculateSphereOfInfluence(secondFacilityLocation, firstFacilityLocation);
                orderFunction.setSphereOfInfluence(sphereOfInfluencePair.getKey());
                orderFunction.setOrders(orders);
                OrderFunctionResult orderFunctionResult = orderFunction.GetNextMove(firstFacilityLocation);

                double flpCost = flpResult.getCost() * flpCoefficient;
                double orderCost = orderFunctionResult.getCost() * orderFunctionCoefficient;
                String facilityLocationBeforeMove = secondFacilityLocation;

                //  move according to flp
                if(flpCost < orderCost){
                    //System.out.println("Second FLP");
                    //System.out.println();
                    flpCoefficient += .1;
                    if(orderFunctionCoefficient >= .1) {
                        orderFunctionCoefficient -= .1;
                    }
                    secondFacilityLocation = flpResult.getDestinationNode();
                }

                //  move according to order
                else{
                    //System.out.println("Second Order");
                    //System.out.println();
                    if(flpCoefficient >= .1) {
                        flpCoefficient -= .1;
                    }
                    orderFunctionCoefficient += .1;
                    secondFacilityLocation = orderFunctionResult.getDestinationNode();
                }

                MarkOrdersAsServiced(secondFacilityLocation);
                SendRobots(orderFunctionResult, facilityLocationBeforeMove);

                firstFacilityTurn = true;

                System.out.println("Second Facility Turn");
                System.out.println("Before: " + facilityLocationBeforeMove);
                System.out.println("After: " + secondFacilityLocation);

                /*
                TODO: THIS IS THE SECOND FACILITY MOVEMENT
                TODO: facilityLocationBeforeMove IS THE NODE FACILITY WAS AT BEFORE MOVING
                TODO: secondFacilityLocation IS THE NODE FACILITY MOVES TO
                 */
            }
        }

    }

    public static void SendRobots(OrderFunctionResult orderFunctionResult, String facilityLocation){
        for(int i = orders.size() - 1; i >= 0; i--){
            for(int j = orderFunctionResult.getOrdersInInfluence().size() - 1; j >= 0; j--){
                if(orders.get(i).getNode().equals(orderFunctionResult.getOrdersInInfluence().get(j))){
                    orders.get(i).setServiced(true);
                    orders.get(i).robotSent(new DijkstraShortestPath<>(completeGraph).getPathWeight(orders.get(i).getNode(), facilityLocation));
                    orders.remove(i);
                    /*
                    TODO: orders.get(i).getNode() IS THE ORDER THAT IS BEING TAKEN CARE OF BY THE ROBOT
                     */
                    break;
                }
            }
        }

        System.out.println("ORDERS SIZE AFTER: " + orders.size());
    }

    public static void MarkOrdersAsServiced(String facilityLocation){
        for(int i = orders.size() - 1; i >= 0; i--){
            if(orders.get(i).getNode().equals(facilityLocation)){
                orders.get(i).setServiced(true);
            }
        }
    }

    //  method that initializes a complete weighted undirected graph
    private static void initializeGraph(int numOfNodes){

        // Create the VertexFactory so the generator can create vertices
        Supplier<String> vSupplier = new Supplier<String>()
        {
            private int id = 1;

            @Override
            public String get()
            {
                return getCharForNumber(id++);
            }

            private String getCharForNumber(int i) {
                return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
            }
        };


        completeGraph = new DefaultUndirectedWeightedGraph<>(vSupplier, SupplierUtil.createDefaultWeightedEdgeSupplier());

        vSupplier = new Supplier<String>()
        {
            private int id = 1;

            @Override
            public String get()
            {
                return getCharForNumber(id++);
            }

            private String getCharForNumber(int i) {
                return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
            }
        };

        completeTruckGraph = new DefaultUndirectedWeightedGraph<>(vSupplier, SupplierUtil.createDefaultWeightedEdgeSupplier());

        // Create the CompleteGraphGenerator object
        CompleteGraphGenerator<String, DefaultWeightedEdge> completeGenerator =
                new CompleteGraphGenerator<>(numOfNodes);

        // Use the CompleteGraphGenerator object to make completeGraph a
        // complete graph with [size] number of vertices
        completeGenerator.generateGraph(completeGraph);
        completeGenerator.generateGraph(completeTruckGraph);

        try {
            Scanner scanner = new Scanner(new File("NodeTextList.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineArray = line.split(",");
                completeGraph.setEdgeWeight(lineArray[0], lineArray[1], Double.parseDouble(lineArray[2]));
                completeTruckGraph.setEdgeWeight(lineArray[0], lineArray[1], Double.parseDouble(lineArray[2]));
            }
        }

        catch (FileNotFoundException e){
            e.printStackTrace();
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

        orders.add(orders.size(), order);
        //System.out.println("GENERATE");
        numOrdersCap++;

        /*
        TODO: MARK NODE WITH ORDER
         */
    }

    //  timer that generates orders every random seconds to replicate real life
    //  random seconds is between 1-4 seconds
    public static class OrderGenerator extends TimerTask {

        private boolean ordersComplete = false;

        public void run() {
                //int delay = (1 + new Random().nextInt(4)) * 1000;
                timer.schedule(new OrderGenerator(), 500);
                generateOrder();
                this.cancel();
        }

        public boolean getOrderStatus(){
            return ordersComplete;
        }
    }

    private static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to move facility...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
