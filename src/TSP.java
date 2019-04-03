import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class TSP {

    private int bestHeuristic;
    private Graph<String, DefaultWeightedEdge> completeGraph;
    private List<DefaultWeightedEdge> edgeVisited;
    private ArrayList<String> unvisitedNodes;

    public TSP(Graph<String, DefaultWeightedEdge> completeGraph){
        this.completeGraph = completeGraph;
    }

    public BaseLocation getBaseLocation(Set<String> vertices){
        BaseLocation baseLocation = new BaseLocation();

        String[] arrayVertices = vertices.toArray(new String[0]);

        //  make a nodes visited list
        unvisitedNodes = new ArrayList<>(Arrays.asList(arrayVertices));

        //  iterate through all possible starting positions for 2 trucks
        for(int i = 0; i < arrayVertices.length; i++){
            String firstTruckPosition = arrayVertices[i];

            for(int j = 0; j < arrayVertices.length; j++){
                String secondTruckPosition = arrayVertices[j];
            }
        }

        return baseLocation;
    }

    private int runTSP(String firstTruckPosition, String secondTruckPosition){

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
            //List<DefaultWeightedEdge> firstCurrentNodeEdges =
            //firstCurrentNode = nextMove(edgesFromFirstStart, )
        }

        return 0;

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

    private String nextMove(List<DefaultWeightedEdge> edgesFromStart, List<DefaultWeightedEdge> edgesFromCurrent, String currentNode, String truckNode){
        //  remove edge connecting current node to start node
        edgesFromCurrent.remove(completeGraph.getEdge(currentNode, truckNode));

        //  only compare edges that haven't been visited
        for(int i = edgesFromStart.size() - 1; i >= 0; i--){
            for(int j = 0; j < edgeVisited.size(); j++){
                if(edgeVisited.get(j).toString().equals(edgesFromStart.get(i).toString())){
                    edgesFromStart.remove(i);
                }
            }
        }

        //  only compare edges that haven't been visited
        for(int i = edgesFromCurrent.size() - 1; i >= 0; i--){
            for(int j = 0; j < edgeVisited.size(); j++){
                if(edgeVisited.get(j).toString().equals(edgesFromCurrent.get(i).toString())){
                    edgesFromCurrent.remove(i);
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
                return completeGraph.getEdgeTarget(minEdgeFromCurrent);
            }

            else{
                return completeGraph.getEdgeSource(minEdgeFromCurrent);
            }
        }

        //  discontinue current robot and send out a new one
        else{
            edgeVisited.add(minEdgeFromStart);

            //  since edge has 2 vertices, check to see which node is not the start
            if(!completeGraph.getEdgeTarget(minEdgeFromStart).equals(truckNode)){
                return completeGraph.getEdgeTarget(minEdgeFromStart);
            }

            else{
                return completeGraph.getEdgeSource(minEdgeFromStart);
            }
        }
    }


    /*
    Getter and Setters
     */
    public int getBestHeuristic() {
        return bestHeuristic;
    }

    public void setBestHeuristic(int bestHeuristic) {
        this.bestHeuristic = bestHeuristic;
    }


}


