import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class FLP {

    private Graph<String, DefaultWeightedEdge> completeGraph;
    private boolean firstFacilityTurn;

    public FLP(Graph<String, DefaultWeightedEdge> completeGraph){
        this.completeGraph = completeGraph;
    }

    public void setSecondFacilityTurn(){
        firstFacilityTurn = false;
    }

    public void setFirstFacilityTurn(){
        firstFacilityTurn = true;
    }

    //  method that determines where the facility should move next to make workload (sphere of influence) more even
    private String GetNextBestMove(String firstFacilityPos, String secondFacilityPos){

        //  sphere of influence comparator
        Comparator<Pair<SphereOfInfluence, SphereOfInfluence>> sphereOfInfluenceComparator = (Pair<SphereOfInfluence, SphereOfInfluence> p1, Pair<SphereOfInfluence, SphereOfInfluence> p2) ->
                Double.compare(Math.abs(p2.getKey().getTotalCost() - p2.getValue().getTotalCost()),Math.abs(p1.getKey().getTotalCost() - p1.getValue().getTotalCost()));

        List<Pair<SphereOfInfluence, SphereOfInfluence>> sphereOfInfluenceList = new ArrayList<>();

        if(firstFacilityTurn){
            List<DefaultWeightedEdge> edgesFromFirstFacility = new ArrayList<>(completeGraph.edgesOf(firstFacilityPos));


            //  loop through all edges from first facility and remove the edge that is connected to second facility
            for(int i = edgesFromFirstFacility.size() - 1; i >= 0; i--){
                if(completeGraph.getEdgeSource(edgesFromFirstFacility.get(i)).equals(secondFacilityPos) ||
                        completeGraph.getEdgeTarget(edgesFromFirstFacility.get(i)).equals(secondFacilityPos)){
                    edgesFromFirstFacility.remove(i);
                    continue;
                }

                Pair<SphereOfInfluence, SphereOfInfluence> sphereOfInfluencePair = CalculateSphereOfInfluence(firstFacilityPos, secondFacilityPos);
                sphereOfInfluenceList.add(sphereOfInfluencePair);
            }

        }

        else{
            List<DefaultWeightedEdge> edgesFromSecondFacility = new ArrayList<>(completeGraph.edgesOf(secondFacilityPos));

            //  loop through all edges from first facility and remove the edge that is connected to second facility
            for(int i = edgesFromSecondFacility.size() - 1; i >= 0; i--){
                if(completeGraph.getEdgeSource(edgesFromSecondFacility.get(i)).equals(firstFacilityPos) ||
                        completeGraph.getEdgeTarget(edgesFromSecondFacility.get(i)).equals(firstFacilityPos)){
                    edgesFromSecondFacility.remove(i);
                    continue;
                }

                //  put second facility pos in the first parameter
                //  CalculateSphereOfInfluence() is designed to have the first parameter move first
                Pair<SphereOfInfluence, SphereOfInfluence> sphereOfInfluencePair = CalculateSphereOfInfluence(secondFacilityPos, firstFacilityPos);
                sphereOfInfluenceList.add(sphereOfInfluencePair);
            }

        }

        sphereOfInfluenceList.sort(sphereOfInfluenceComparator);
        return sphereOfInfluenceList.get(0).getKey().getFacilityPosition();
    }

    //  method that determines sphere of influence (workload) for both facilities
    private Pair<SphereOfInfluence, SphereOfInfluence> CalculateSphereOfInfluence(String firstFacilityPos, String secondFacilityPos){
        List<DefaultWeightedEdge> edgesFromFirstFacility = new ArrayList<>(completeGraph.edgesOf(firstFacilityPos));
        List<DefaultWeightedEdge> edgesFromSecondFacility = new ArrayList<>(completeGraph.edgesOf(secondFacilityPos));

        SphereOfInfluence firstFacilityInfluence = new SphereOfInfluence(0, 0, firstFacilityPos);
        SphereOfInfluence secondFacilityInfluence = new SphereOfInfluence(0, 0, secondFacilityPos);

        //  edge weight comparator
        Comparator<DefaultWeightedEdge> compareById = (DefaultWeightedEdge e1, DefaultWeightedEdge e2) ->
                Double.compare(completeGraph.getEdgeWeight(e1), completeGraph.getEdgeWeight(e2));

        //  loop through all edges from first facility and remove the edge that is connected to second facility
        for(int i = 0; i < edgesFromFirstFacility.size(); i++){
            if(completeGraph.getEdgeSource(edgesFromFirstFacility.get(i)).equals(secondFacilityPos) ||
                completeGraph.getEdgeTarget(edgesFromFirstFacility.get(i)).equals(secondFacilityPos)){
                edgesFromFirstFacility.remove(i);
                break;
            }
        }

        //  loop through all edges from second facility and remove the edge that is connected to first facility
        for(int i = 0; i < edgesFromSecondFacility.size(); i++){
            if(completeGraph.getEdgeSource(edgesFromSecondFacility.get(i)).equals(firstFacilityPos) ||
                    completeGraph.getEdgeTarget(edgesFromSecondFacility.get(i)).equals(firstFacilityPos)){
                edgesFromSecondFacility.remove(i);
                break;
            }
        }

        //  sort in ascending weight
        edgesFromFirstFacility.sort(compareById);
        edgesFromSecondFacility.sort(compareById);

        Set<String> unvisitedVertices = completeGraph.vertexSet();

        //  remove both facilities from unvisited list
        unvisitedVertices.remove(firstFacilityPos);
        unvisitedVertices.remove(secondFacilityPos);

        int index = 0;
        while(unvisitedVertices.size() > 0){
            DefaultWeightedEdge lowestEdgeWeightFirstFacility = edgesFromFirstFacility.get(index);
            DefaultWeightedEdge lowestEdgeWeightSecondFacility = edgesFromSecondFacility.get(index);

            //  not guaranteed to be target or source
            //  check to see which of target or source is the first facility position
            String possibleFirstFacilityPos = completeGraph.getEdgeSource(lowestEdgeWeightFirstFacility);
            if(firstFacilityPos.equals(possibleFirstFacilityPos)){
                String nextMove = completeGraph.getEdgeTarget(lowestEdgeWeightFirstFacility);
                unvisitedVertices.remove(nextMove);
            }

            else{
                String nextMove = completeGraph.getEdgeSource(lowestEdgeWeightFirstFacility);
                unvisitedVertices.remove(nextMove);
            }

            //  add to total cost of first facility influence
            //  increment num nodes
            firstFacilityInfluence.totalCost += completeGraph.getEdgeWeight(lowestEdgeWeightFirstFacility);
            firstFacilityInfluence.numNodes++;

            //  all nodes may be visited after first facility turn, so check to see if all vertices have been visited before second facility's turn
            if(unvisitedVertices.size() > 0){
                //  not guaranteed to be target or source
                //  check to see which of target or source is the first facility position
                String possibleSecondFacilityPos = completeGraph.getEdgeSource(lowestEdgeWeightSecondFacility);
                if(secondFacilityPos.equals(possibleSecondFacilityPos)){
                    String nextMove = completeGraph.getEdgeTarget(lowestEdgeWeightSecondFacility);
                    unvisitedVertices.remove(nextMove);
                }

                else{
                    String nextMove = completeGraph.getEdgeSource(lowestEdgeWeightSecondFacility);
                    unvisitedVertices.remove(nextMove);
                }

                //  add to total cost of first facility influence
                //  increment num nodes
                secondFacilityInfluence.totalCost += completeGraph.getEdgeWeight(lowestEdgeWeightSecondFacility);
                secondFacilityInfluence.numNodes++;
            }
            index++;
        }

        return new Pair<>(firstFacilityInfluence, secondFacilityInfluence);
    }


    private class SphereOfInfluence{
        private double totalCost;
        private int numNodes;
        private String facilityPosition;

        public SphereOfInfluence(){

        }

        public SphereOfInfluence(double totalCost, int numNodes, String facilityPosition) {
            this.totalCost = totalCost;
            this.numNodes = numNodes;
            this.facilityPosition = facilityPosition;
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
}
