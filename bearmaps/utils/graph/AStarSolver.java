
package bearmaps.utils.graph;
import bearmaps.utils.pq.MinHeapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AStarSolver<Vertex>  implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome; // will be set to one of the option in solverOutcome based on constructor
    private List<Vertex> solution = new ArrayList<>();
    private double solutionWeight = 0;
    private int numStatesExplored;
    private double explorationTime; // keep track of time using the stopwatch class
    private MinHeapPQ<Vertex> fringe ;
    private HashMap<Vertex, Double> distances;
    private HashMap<Vertex, Vertex> edgeTos;

    // need to figure out how to: add to solution at the appropriate time, and keep track of needed distances
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {

        // initializing variables
        Stopwatch tracker = new Stopwatch();
        fringe = new MinHeapPQ<>();
        distances = new HashMap();
        edgeTos = new HashMap();

        // adding start to fringe and trackers
        double priorityValueOfSource = input.estimatedDistanceToGoal(start, end);
        fringe.insert(start, priorityValueOfSource);
        distances.put(start, 0.0);
        edgeTos.put(start, null);

        // iterating through neighbors
        boolean worked = false;
        while (fringe.size() != 0) {

            if (tracker.elapsedTime() >= timeout) {
                worked = false;
                outcome = SolverOutcome.TIMEOUT;
                break;
            }

            if (fringe.peek().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                worked = true;
                break;
            }

            Vertex curr = fringe.poll();
            numStatesExplored++;

            for (WeightedEdge edge : input.neighbors(curr)) {
                relax(edge, input, end);
            }

            explorationTime = tracker.elapsedTime();
        }

        if (fringe.size() == 0 && !worked) {
            outcome = SolverOutcome.UNSOLVABLE;
            solution.clear();
            solutionWeight = 0;
        } else if (!worked) {
            solution.clear();
            solutionWeight = 0;
        } else if (worked) {
            // adding results to solution
            solutionWeight = distances.get(end);
            while (end != null) {
                solution.add(0, end);
                end = edgeTos.get(end);
            }
        }
    }

    public void relax (WeightedEdge<Vertex> edge, AStarGraph<Vertex> input, Vertex goal) {

        // getting needed variables
        Vertex from =  edge.from();
        Vertex to = edge.to();
        double weight = edge.weight();

        double newDistance = distances.get(from) + weight;


        // comparing new and old distances
        if (!distances.containsKey(to) || newDistance < distances.get(to)) {
            distances.put(to, newDistance);
            edgeTos.put(to, from);

            if (fringe.contains(to)) {
                fringe.changePriority(to, newDistance + input.estimatedDistanceToGoal(to, goal));
            } else {
                fringe.insert(to,newDistance + input.estimatedDistanceToGoal(to, goal));
            }

        }
    }

    public SolverOutcome outcome() {
        return this.outcome;
    }

    public List<Vertex> solution() {
        return this.solution;
    }

    public double solutionWeight() {
        return this.solutionWeight;
    }

    public int numStatesExplored() {
        return this.numStatesExplored;
    }

    public double explorationTime() {
        return this.explorationTime;
    }

}
