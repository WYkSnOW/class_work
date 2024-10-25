import java.util.*;

/**
 * Your implementation of various different graph algorithms.
 *
 * @author Waiyuk Kwong
 * @version 1.0
 * @userid wkwong6
 * @GTID 903722134
 *
 * Collaborators: NA
 *
 * Resources: NA
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth first search (bfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * You may import/use java.util.Set, java.util.List, java.util.Queue, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for BFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the bfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start can not be null.");
        } else if (graph == null) {
            throw new IllegalArgumentException("Graph can not be null.");
        } else if (!(graph.getAdjList().containsKey(start))) {
            throw new IllegalArgumentException("Start must exist in the graph.");
        }
        Queue<Vertex<T>> q = new LinkedList<>();
        List<Vertex<T>> r = new ArrayList<>();
        q.add(start);
        r.add(start);
        while (!q.isEmpty()) {
            Vertex<T> temp = q.remove();
            for (VertexDistance<T> dis : graph.getAdjList().get(temp)) {
                if (!(r.contains(dis.getVertex()))) {
                    q.add(dis.getVertex());
                    r.add(dis.getVertex());
                }
            }
        }
        return r;
    }


    /**
     * Helper method for dfs
     *
     * @param <T>   the generic typing of the data
     * @param curr  the vertex that we currently at
     * @param graph the graph to search through
     * @param r     the list of result
     * @param v     the set that include visited vertex
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    private static <T> void dfsHelper(Vertex<T> curr, Graph<T> graph, List<Vertex<T>> r, Set<Vertex<T>> v) {
        r.add(curr);
        v.add(curr);
        for (VertexDistance<T> next : graph.getAdjList().get(curr)) {
            if (!(v.contains(next.getVertex()))) {
                dfsHelper(next.getVertex(), graph, r, v);
            }
        }
    }

    /**
     * Performs a depth first search (dfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * *NOTE* You MUST implement this method recursively, or else you will lose
     * all points for this method.
     *
     * You may import/use java.util.Set, java.util.List, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for DFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the dfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start can not be null.");
        } else if (graph == null) {
            throw new IllegalArgumentException("Graph can not be null.");
        } else if (!(graph.getAdjList().containsKey(start))) {
            throw new IllegalArgumentException("Start must exist in the graph.");
        }
        List<Vertex<T>> r = new ArrayList<>();
        Set<Vertex<T>> v = new HashSet<>();
        dfsHelper(start, graph, r, v);
        return r;
    }

    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     *
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from start, or Integer.MAX_VALUE (representing
     * infinity) if no path exists.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Map, and java.util.Set and any class that
     * implements the aforementioned interfaces, as long as your use of it
     * is efficient as possible.
     *
     * You should implement the version of Dijkstra's where you use two
     * termination conditions in conjunction.
     *
     * 1) Check if all of the vertices have been visited.
     * 2) Check if the PQ is empty.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from start to every
     * other node in the graph
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
                                                        Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start can not be null.");
        } else if (graph == null) {
            throw new IllegalArgumentException("Graph can not be null.");
        } else if (!(graph.getAdjList().containsKey(start))) {
            throw new IllegalArgumentException("Start must exist in the graph.");
        }

        Queue<VertexDistance<T>> q = new PriorityQueue<>();
        Map<Vertex<T>, Integer> r = new HashMap<>();

        for (Vertex<T> v : graph.getAdjList().keySet()) {
            r.put(v, Integer.MAX_VALUE);
        }
        r.put(start, 0);
        q.add(new VertexDistance<>(start, 0));

        while (!(q.isEmpty())) {
            VertexDistance<T> curr = q.remove();
            for (VertexDistance<T> d : graph.getAdjList().get(curr.getVertex())) {
                int dis = curr.getDistance() + d.getDistance();
                if (dis < r.get(d.getVertex())) {
                    q.add(new VertexDistance<>(d.getVertex(), dis));
                    r.put(d.getVertex(), dis);
                }
            }
        }
        return r;
    }

    /**
     * Runs Prim's algorithm on the given graph and returns the Minimum
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     *
     * You may assume that the passed in graph is undirected. In this framework,
     * this means that if (u, v, 3) is in the graph, then the opposite edge
     * (v, u, 3) will also be in the graph, though as a separate Edge object.
     *
     * The returned set of edges should form an undirected graph. This means
     * that every time you add an edge to your return set, you should add the
     * reverse edge to the set as well. This is for testing purposes. This
     * reverse edge does not need to be the one from the graph itself; you can
     * just make a new edge object representing the reverse edge.
     *
     * You may assume that there will only be one valid MST that can be formed.
     *
     * You should NOT allow self-loops or parallel edges in the MST.
     *
     * You may import/use PriorityQueue, java.util.Set, and any class that 
     * implements the aforementioned interface.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for this method (storing the adjacency list in a variable is fine).
     *
     * @param <T> the generic typing of the data
     * @param start the vertex to begin Prims on
     * @param graph the graph we are applying Prims to
     * @return the MST of the graph or null if there is no valid MST
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    public static <T> Set<Edge<T>> prims(Vertex<T> start, Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start can not be null.");
        } else if (graph == null) {
            throw new IllegalArgumentException("Graph can not be null.");
        } else if (!(graph.getAdjList().containsKey(start))) {
            throw new IllegalArgumentException("Start must exist in the graph.");
        }
        Set<Vertex<T>> vs = new HashSet<>();
        Set<Edge<T>> mst = new HashSet<>();
        PriorityQueue<Edge<T>> pq = new PriorityQueue<>();

        for(VertexDistance<T> curr : graph.getAdjList().get(start)) {
            pq.add(new Edge<>(start, curr.getVertex(), curr.getDistance()));
        }

        vs.add(start);
        while (!pq.isEmpty() && vs.size() < graph.getVertices().size()) {
            Edge<T> edge = pq.remove();
            if (!vs.contains(edge.getV())) {
                vs.add(edge.getV());
                mst.add(edge);
                mst.add(new Edge<>(edge.getV(), edge.getU(), edge.getWeight()));

                for (VertexDistance<T> curr :graph.getAdjList().get(edge.getV())) {
                    if (!vs.contains(curr.getVertex())) {
                        pq.add(new Edge<>(edge.getV(), curr.getVertex(), curr.getDistance()));
                    }
                }
            }
        }
        if(vs.size() == graph.getVertices().size()) {
            return mst;
        }
        return null;
    }
}