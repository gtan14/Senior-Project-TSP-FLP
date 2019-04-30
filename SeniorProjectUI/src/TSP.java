import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class TSP {

    private Graph<String, DefaultWeightedEdge> completeGraph;
    private List<DefaultWeightedEdge> edgeVisited;
    private ArrayList<String> unvisitedNodes;
    private List<Heuristic> topHeuristics;

    public TSP(Graph<String, DefaultWeightedEdge> completeGraph){
        this.completeGraph = completeGraph;
    }

    public Map<String, BaseLocation> getBaseLocation(Set<String> vertices){
        topHeuristics = new ArrayList<>();

        String[] arrayVertices = vertices.toArray(new String[0]);

        Comparator<Heuristic> comparator = new Comparator<Heuristic>() {
            public int compare(Heuristic h1, Heuristic h2) {
                //System.out.println("H1 Cost: " + h1.getCost());
                //System.out.println("H2 Cost: " + h2.getCost());
                return Double.compare(h1.getCost(), (h2.getCost()));
            }
        };

        getTopHeuristics(arrayVertices, comparator);


        while(topHeuristics.size() < 3){
            getTopHeuristics(arrayVertices, comparator);
        }

        BaseLocation firstBaseLocation = new BaseLocation();
        BaseLocation secondBaseLocation = new BaseLocation();

        for(int i = 0; i < topHeuristics.size(); i++){
            if(firstBaseLocation.getFirst() == null){
                firstBaseLocation.setFirst(topHeuristics.get(i).getFirstFacilityPosition());
                secondBaseLocation.setFirst(topHeuristics.get(i).getSecondFacilityPosition());
            }

            else if(firstBaseLocation.getSecond() == null){
                firstBaseLocation.setSecond(topHeuristics.get(i).getFirstFacilityPosition());
                secondBaseLocation.setSecond(topHeuristics.get(i).getSecondFacilityPosition());
            }

            else if(firstBaseLocation.getThird() == null){
                firstBaseLocation.setThird(topHeuristics.get(i).getFirstFacilityPosition());
                secondBaseLocation.setThird(topHeuristics.get(i).getSecondFacilityPosition());
            }
        }

        //  create a map that stores the two facilities with their base locations
        Map<String, BaseLocation> baseLocationMap = new HashMap<>();
        baseLocationMap.put("first", firstBaseLocation);
        baseLocationMap.put("second", secondBaseLocation);

        return baseLocationMap;
    }

    private void getTopHeuristics(String[] arrayVertices, Comparator<Heuristic> comparator){
        //  iterate through all possible starting positions for 2 trucks
        for(int i = 0; i < arrayVertices.length; i++){
            String firstTruckPosition = arrayVertices[i];

            for(int j = 0; j < arrayVertices.length; j++){
                //  evaluate if both facilities are not at same location
                if(i != j){
                    String secondTruckPosition = arrayVertices[j];

                    //  make a nodes visited list
                    unvisitedNodes = new ArrayList<>(Arrays.asList(arrayVertices));

                    //  update heuristic list with best heuristics and facility positions
                    //  evaluate facilities with both facilities in different order
                    //  different ordering may result in different heuristic
                    evaluateHeuristic(firstTruckPosition, secondTruckPosition, comparator);
                    evaluateHeuristic(secondTruckPosition, firstTruckPosition, comparator);
                }
            }
        }
    }

    private void evaluateHeuristic(String firstTruckPosition, String secondTruckPosition, Comparator<Heuristic> comparator){
        double cost = runTSP(firstTruckPosition, secondTruckPosition);

        //  make sure heuristic list is sorted before binary search
        topHeuristics.sort(new Comparator<Heuristic>() {
            //  define what to sort by
            @Override
            public int compare(Heuristic o1, Heuristic o2) {
                return Double.compare(o1.getCost(), (o2.getCost()));
            }
        });

        Heuristic heuristic = new Heuristic(firstTruckPosition, secondTruckPosition, cost);
        //  get index to be inserted at
        int index = Collections.binarySearch(topHeuristics, heuristic, comparator);

        //System.out.println("COST: " + cost);
        //  heuristic was found with same cost
        //  insert into index it was found
        if(index >= 0){
            //System.out.println("Index: " + index);
            /*
            if(!duplicateFacilityPosition(index, heuristic)) {
                topHeuristics.add(index, heuristic);
            }*/
        }

        //  heuristic was not found with same cost
        //  index returned is a negative number
        else{
            int adjustedIndex = (index * -1) - 1;
            //System.out.println("Index: " + adjustedIndex);
            //System.out.println("Top: " + topHeuristics.size());
            //  heuristic should be added to list
            //System.out.println("Index: " + index);
            //System.out.println("Adjusted Index: " + adjustedIndex);
            if(adjustedIndex < topHeuristics.size() + 1){
                if(!duplicateFacilityPosition(adjustedIndex, heuristic)) {
                    topHeuristics.add(adjustedIndex, heuristic);
                }
            }
        }

        //  only keep the top 3 heuristics
        if(topHeuristics.size() > 3){
            //  remove from end of list, since list is in ascending order of cost
            //  remove worst cost
            topHeuristics.remove(topHeuristics.size() - 1);
        }

        /*
        for(int i = 0; i < topHeuristics.size(); i++){
            System.out.println("FirstEvaluate: " + topHeuristics.get(i).firstFacilityPosition + " ; Cost: " + topHeuristics.get(i).getCost());
            System.out.println("SecondEvaluate: " + topHeuristics.get(i).secondFacilityPosition + " ; Cost: " + topHeuristics.get(i).getCost());
        }

        System.out.println();*/



        for(int i = topHeuristics.size() - 1; i >= 0; i--){
            String firstTruck = topHeuristics.get(i).firstFacilityPosition;
            String secondTruck = topHeuristics.get(i).secondFacilityPosition;

            for(int j = i - 1; j >= 0; j--){
                String firstTruckCompare = topHeuristics.get(j).firstFacilityPosition;
                String secondTruckCompare = topHeuristics.get(j).secondFacilityPosition;

                //  remove worst cost which would be at index i since the for loop is reversed
                //  element is compared to element before it (better cost)
                if(firstTruck.equals(firstTruckCompare) || secondTruck.equals(secondTruckCompare) ||
                    firstTruck.equals(secondTruckCompare) || secondTruck.equals(firstTruckCompare)){
                    topHeuristics.remove(i);
                    //  this prevents index out of bounds exception since the outer loop index is used to remove from list
                    break;
                }
            }
        }
    }

    //  this method prevents heuristics being added to top heuristic list if it's worse and if one of the facility locations already exists in top heuristic list
    //  this ensures that heuristics being added contain unique facility locations
    private boolean duplicateFacilityPosition(int index, Heuristic heuristic){
        //System.out.println("INDEX: " + index);
        for(int i = 0; i < index; i++){
            Heuristic topHeuristic = topHeuristics.get(i);
            if(topHeuristic.getFirstFacilityPosition().equals(heuristic.getFirstFacilityPosition()) || topHeuristic.getFirstFacilityPosition().equals(heuristic.getSecondFacilityPosition()) ||
            topHeuristic.getSecondFacilityPosition().equals(heuristic.getFirstFacilityPosition()) || topHeuristic.getSecondFacilityPosition().equals(heuristic.getSecondFacilityPosition())){
                return true;
            }
        }

        return false;
    }

    private double runTSP(String firstTruckPosition, String secondTruckPosition){

        //  set variables for sum of edge weights and number of robots sent per facility
        double firstFacilityCost = 0;
        int firstFacilityNumRobots = 1;

        double secondFacilityCost = 0;
        int secondFacilityNumRobots = 1;

        String firstCurrentNode = firstTruckPosition;
        String secondCurrentNode = secondTruckPosition;

        //  all edges from starting positions
        List<DefaultWeightedEdge> edgesFromFirstStart = new ArrayList<>(completeGraph.edgesOf(firstTruckPosition));
        List<DefaultWeightedEdge> edgesFromSecondStart = new ArrayList<>(completeGraph.edgesOf(secondTruckPosition));

        //  list of all edges visited
        //  if edge has been visited, don't use it
        edgeVisited = new ArrayList<>();

        //  remove the starting nodes from list
        unvisitedNodes.remove(firstTruckPosition);
        unvisitedNodes.remove(secondTruckPosition);

        //  send out robot as first move for both trucks
        firstCurrentNode = firstMove(edgesFromFirstStart, completeGraph.getEdge(firstTruckPosition, secondTruckPosition), firstTruckPosition);
        secondCurrentNode = firstMove(edgesFromSecondStart, completeGraph.getEdge(firstTruckPosition, secondTruckPosition), secondTruckPosition);

        unvisitedNodes.remove(firstCurrentNode);
        unvisitedNodes.remove(secondCurrentNode);

        //  run algorithm until all nodes have been visited
        while(unvisitedNodes.size() > 0){
            //  get all edges from first facility's current node
            List<DefaultWeightedEdge> firstCurrentNodeEdges = new ArrayList<>(completeGraph.edgesOf(firstCurrentNode));

            //  first facility's next move
            //  this will return the node it travelled to, the cost of moving, and if it send out a new robot
            ReturnValueMove firstReturnValue = nextMove(edgesFromFirstStart, firstCurrentNodeEdges, firstCurrentNode, firstTruckPosition);

            //  assign node travelled to as current node for first facility
            firstCurrentNode = firstReturnValue.getNode();

            //  add travel cost to overall cost
            firstFacilityCost += firstReturnValue.getEdgeWeight();

            //  if new robot was sent, increment variable
            if(firstReturnValue.isNewRobotSent()){
                firstFacilityNumRobots++;
            }

            //  mark node as visited by removing from unvisited list
            unvisitedNodes.remove(firstCurrentNode);


            //  check to see if all nodes have been visited before the second facility's turn
            if(unvisitedNodes.size() > 0) {
                //  get all edges from second facility's current node
                List<DefaultWeightedEdge> secondCurrentNodeEdges = new ArrayList<>(completeGraph.edgesOf(secondCurrentNode));

                //  second facility's next move
                //  this will return the node it travelled to, the cost of moving, and if it send out a new robot
                ReturnValueMove secondReturnValue = nextMove(edgesFromSecondStart, secondCurrentNodeEdges, secondCurrentNode, secondTruckPosition);

                secondCurrentNode = secondReturnValue.getNode();
                secondFacilityCost += secondReturnValue.getEdgeWeight();

                if(secondReturnValue.isNewRobotSent()){
                    secondFacilityNumRobots++;
                }

                unvisitedNodes.remove(secondCurrentNode);
            }
        }

        double tspCost = (firstFacilityCost / firstFacilityNumRobots) + (secondFacilityCost / secondFacilityNumRobots);
        return tspCost;

    }

    //  edgeToRemove is the edge between the two starting points, which you don't want to traverse
    //  it is removed from the list of possible edges to take
    private String firstMove(List<DefaultWeightedEdge> edgesFromStart, DefaultWeightedEdge edgeToRemove, String startNode){
        edgesFromStart.remove(edgeToRemove);
        DefaultWeightedEdge minEdge = null;
        for(int i = 0; i < edgesFromStart.size(); i++){
            if(i == 0){
                minEdge = edgesFromStart.get(i);
            }

            else{
                if(completeGraph.getEdgeWeight(edgesFromStart.get(i)) < completeGraph.getEdgeWeight(minEdge)){
                    minEdge = edgesFromStart.get(i);
                }
            }
        }

        edgeVisited.add(minEdge);
        if(!completeGraph.getEdgeTarget(minEdge).equals(startNode)){
            return completeGraph.getEdgeTarget(minEdge);
        }

        else{
            return completeGraph.getEdgeSource(minEdge);
        }
    }

    private ReturnValueMove nextMove(List<DefaultWeightedEdge> edgesFromStart, List<DefaultWeightedEdge> edgesFromCurrent, String currentNode, String truckNode){
        //  remove edge connecting current node to start node
        edgesFromCurrent.remove(completeGraph.getEdge(currentNode, truckNode));

        //  only compare edges that haven't been visited
        for(int i = edgesFromStart.size() - 1; i >= 0; i--){
            for(int j = 0; j < edgeVisited.size(); j++){
                if(edgeVisited.get(j).toString().equals(edgesFromStart.get(i).toString())){
                    edgesFromStart.remove(i);
                    break;
                }
            }
        }

        //  only compare edges that haven't been visited
        for(int i = edgesFromCurrent.size() - 1; i >= 0; i--){
            for(int j = 0; j < edgeVisited.size(); j++){
                if(edgeVisited.get(j).toString().equals(edgesFromCurrent.get(i).toString())){
                    edgesFromCurrent.remove(i);
                    break;
                }
            }
        }

        //  get lowest edge weight from current node
        DefaultWeightedEdge minEdgeFromCurrent = null;
        for(int i = 0; i < edgesFromCurrent.size(); i++){
            if(i == 0){
                minEdgeFromCurrent = edgesFromCurrent.get(i);
            }

            else{
                if(completeGraph.getEdgeWeight(edgesFromCurrent.get(i)) < completeGraph.getEdgeWeight(minEdgeFromCurrent)){
                    minEdgeFromCurrent = edgesFromCurrent.get(i);
                }
            }
        }

        //  get lowest edge weight from start node
        DefaultWeightedEdge minEdgeFromStart = null;
        for(int i = 0; i < edgesFromStart.size(); i++){
            if(i == 0){
                minEdgeFromStart = edgesFromStart.get(i);
            }

            else{
                if(completeGraph.getEdgeWeight(edgesFromStart.get(i)) < completeGraph.getEdgeWeight(minEdgeFromStart)){
                    minEdgeFromStart = edgesFromStart.get(i);
                }
            }
        }

        //  continue path of current robot
        if(completeGraph.getEdgeWeight(minEdgeFromCurrent) < completeGraph.getEdgeWeight(minEdgeFromStart)){
            edgeVisited.add(minEdgeFromCurrent);

            //  check to see which node to travel
            //  don't want to travel to node already on
            if(!completeGraph.getEdgeTarget(minEdgeFromCurrent).equals(currentNode)){
                return new ReturnValueMove(completeGraph.getEdgeWeight(minEdgeFromCurrent), completeGraph.getEdgeTarget(minEdgeFromCurrent), false);
            }

            else{
                return new ReturnValueMove(completeGraph.getEdgeWeight(minEdgeFromCurrent), completeGraph.getEdgeSource(minEdgeFromCurrent), false);
            }
        }

        //  discontinue current robot and send out a new one
        else{
            edgeVisited.add(minEdgeFromStart);

            //  since edge has 2 vertices, check to see which node is not the start
            if(!completeGraph.getEdgeTarget(minEdgeFromStart).equals(truckNode)){
                return new ReturnValueMove(completeGraph.getEdgeWeight(minEdgeFromStart), completeGraph.getEdgeTarget(minEdgeFromStart), true);
            }

            else{
                return new ReturnValueMove(completeGraph.getEdgeWeight(minEdgeFromStart), completeGraph.getEdgeSource(minEdgeFromStart), true);
            }
        }
    }



    //  class that keeps track of the best heuristics
    private class Heuristic{
        private String firstFacilityPosition, secondFacilityPosition;
        private double cost;

        public Heuristic(String firstFacilityPosition, String secondFacilityPosition, double cost) {
            this.firstFacilityPosition = firstFacilityPosition;
            this.secondFacilityPosition = secondFacilityPosition;
            this.cost = cost;
        }

        public String getFirstFacilityPosition() {
            return firstFacilityPosition;
        }

        public void setFirstFacilityPosition(String firstFacilityPosition) {
            this.firstFacilityPosition = firstFacilityPosition;
        }

        public String getSecondFacilityPosition() {
            return secondFacilityPosition;
        }

        public void setSecondFacilityPosition(String secondFacilityPosition) {
            this.secondFacilityPosition = secondFacilityPosition;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }
    }
    //  values that are returned when a facility makes a move
    private class ReturnValueMove{
        private double edgeWeight;
        private String node;
        private boolean newRobotSent = false;

        public ReturnValueMove(double edgeWeight, String node, boolean newRobotSent){
            this.edgeWeight = edgeWeight;
            this.node = node;
            this.newRobotSent = newRobotSent;
        }

        public double getEdgeWeight() {
            return edgeWeight;
        }

        public void setEdgeWeight(double edgeWeight) {
            this.edgeWeight = edgeWeight;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public boolean isNewRobotSent() {
            return newRobotSent;
        }

        public void setNewRobotSent(boolean newRobotSent) {
            this.newRobotSent = newRobotSent;
        }
    }

}


