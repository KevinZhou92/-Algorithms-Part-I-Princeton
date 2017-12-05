import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.*;
import java.util.*;


public class BaseballElimination {    
    private final int numOfTeams;
    private final Map<String, Integer> nameToId;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final int[][] games;
    private final boolean[] isEliminated;
    private final Map<Integer, Set<String>> eliminatedList;
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numOfTeams = in.readInt();
        nameToId = new HashMap<String, Integer>();
        teams = new String[numOfTeams];
        wins = new int[numOfTeams];
        losses = new int[numOfTeams];
        remains = new int[numOfTeams];
        games = new int[numOfTeams][numOfTeams];
        isEliminated = new boolean[numOfTeams];
        eliminatedList = new HashMap<>();
        int id = 0;
        while (!in.isEmpty()) {
            teams[id] = in.readString();
            wins[id] = Integer.parseInt(in.readString());
            losses[id] = Integer.parseInt(in.readString());
            remains[id] = Integer.parseInt(in.readString());
            for (int j = 0; j < numOfTeams; j++) {
                games[id][j] = Integer.parseInt(in.readString());
            }
            nameToId.put(teams[id], id);
            id++;
        }
        trivialElimination();
        nonTrivialElimination();
    }
    private void trivialElimination() {
        for (int i = 0; i < numOfTeams; i++) {
            for (int j = 0; j < numOfTeams; j++) {
                if (i == j) {
                    continue;
                }
                if (wins[i] + remains[i] < wins[j]) {
                    isEliminated[i] = true;
                    if (eliminatedList.get(i) == null) {
                        eliminatedList.put(i, new HashSet<String>());
                    }
                    eliminatedList.get(i).add(teams[j]);
                }
            }
        }
    }
    private void nonTrivialElimination() {
        int numOfGameVertex = (numOfTeams - 1) * (numOfTeams - 2) / 2;
        int numOfTotalVertex = numOfGameVertex + numOfTeams + 2;
        
        for (int team = 0; team < numOfTeams; team++) {
            if (isEliminated[team]) {
                continue;
            }

            int index = 1;
            // construct the flow network
            FlowNetwork graph = new FlowNetwork(numOfTotalVertex);
            Map<Integer, Integer> teamToVertex = new HashMap<>();
            for (int i = 0; i < numOfTeams; i++) {
                if (i != team) {
                    teamToVertex.put(i, i + numOfGameVertex + 1);
                }
            }
            // add edge from source to vertex i-j
            for (int i = 0; i < numOfTeams; i++) {
                if (i == team) {
                    continue;
                }
                for (int j = i + 1; j < numOfTeams; j++) {
                    if (j == team) {
                        continue;
                    }
                    graph.addEdge(new FlowEdge(0, index, games[i][j]));
                    graph.addEdge(new FlowEdge(index, teamToVertex.get(i), Double.POSITIVE_INFINITY));
                    graph.addEdge(new FlowEdge(index, teamToVertex.get(j), Double.POSITIVE_INFINITY));
                    index++;
                }
            }
            // add edge from vertex i to target
            for (int i = 0; i < numOfTeams; i++) {
                if (i != team) {
                    graph.addEdge(new FlowEdge(teamToVertex.get(i), numOfTotalVertex - 1, Math.max(0, wins[team] + remains[team] - wins[i])));
                }
            }
            FordFulkerson maxFlow = new FordFulkerson(graph, 0, graph.V() - 1);
            for (FlowEdge e : graph.adj(0)) {
                if (e.flow() < e.capacity()) {
                    isEliminated[team] = true;
                    break;
                }
            }
            
            // find certification of elimination
            if (isEliminated[team]) {
                for (int i = 0; i < numOfTeams; i++) {
                    if (i == team) {
                        continue;
                    }
                    if (maxFlow.inCut(teamToVertex.get(i))) {
                        if (!eliminatedList.containsKey(team)) {
                            eliminatedList.put(team, new HashSet<String>());
                        }
                        eliminatedList.get(team).add(teams[i]);
                    }
                }
            }
        }
    }
    // number of teams
    public int numberOfTeams() {
        return numOfTeams;
    }                      
    // all teams
    public Iterable<String> teams() {
        return nameToId.keySet();
    } 
    // number of losses for given team
    public  int wins(String team) {
        if (nameToId.get(team) == null) {
            throw new IllegalArgumentException();
        }
        return wins[nameToId.get(team)];
    }
    // number of losses for given team
    public  int losses(String team) {
        if (nameToId.get(team) == null) {
            throw new IllegalArgumentException();
        }
        return losses[nameToId.get(team)];
    }                   
    // number of remaining games for given team
    public  int remaining(String team) {
        if (nameToId.get(team) == null) {
            throw new IllegalArgumentException();
        }
        return remains[nameToId.get(team)];
    } 
    // number of remaining games between team1 and team2
    public  int against(String team1, String team2) {
        if (nameToId.get(team1) == null || nameToId.get(team2) == null) {
            throw new IllegalArgumentException();
        }
        return games[nameToId.get(team1)][nameToId.get(team2)];
    }
    // is given team eliminated?
    public  boolean isEliminated(String team) {
        if (nameToId.get(team) == null) {
            throw new IllegalArgumentException();
        }
        return isEliminated[nameToId.get(team)];
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
         if (nameToId.get(team) == null) {
            throw new IllegalArgumentException();
        }
        int teamId = nameToId.get(team);
        return eliminatedList.get(teamId);
    } 
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

