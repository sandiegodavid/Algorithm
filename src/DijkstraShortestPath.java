import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DijkstraShortestPath {
	/*
	 * Dijkstra's shortest-path algorithm.
	 * 
	 * The file contains an adjacency list representation of an undirected
	 * weighted graph with 200 vertices labeled 1 to 200. Each row consists of
	 * the node tuples that are adjacent to that particular vertex along with
	 * the length of that edge. For example, the 6th row has 6 as the first
	 * entry indicating that this row corresponds to the vertex labeled 6. The
	 * next entry of this row "141,8200" indicates that there is an edge between
	 * vertex 6 and vertex 141 that has length 8200. The rest of the pairs of
	 * this row indicate the other vertices adjacent to vertex 6 and the lengths
	 * of the corresponding edges.
	 * 
	 * Your task is to run Dijkstra's shortest-path algorithm on this graph,
	 * using 1 (the first vertex) as the source vertex, and to compute the
	 * shortest-path distances between 1 and every other vertex of the graph. If
	 * there is no path between a vertex v and vertex 1, we'll define the
	 * shortest-path distance between 1 and v to be 1000000.
	 * 
	 * You should report the shortest-path distances to the following ten
	 * vertices, in order: 7,37,59,82,99,115,133,165,188,197. You should encode
	 * the distances as a comma-separated string of integers. So if you find
	 * that all ten of these vertices except 115 are at distance 1000 away from
	 * vertex 1 and 115 is 2000 distance away, then your answer should be
	 * 1000,1000,1000,1000,1000,2000,1000,1000,1000,1000. Remember the order of
	 * reporting DOES MATTER, and the string should be in the same order in
	 * which the above ten vertices are given. The string should not contain any
	 * spaces. Please type your answer in the space provided.
	 * 
	 * IMPLEMENTATION NOTES: This graph is small enough that the straightforward
	 * O(mn) time implementation of Dijkstra's algorithm should work fine.
	 * OPTIONAL: For those of you seeking an additional challenge, try
	 * implementing the heap-based version. Note this requires a heap that
	 * supports deletions, and you'll probably need to maintain some kind of
	 * mapping between vertices and their positions in the heap.
	 * 
	 */
	static String inputFileName = "resources/dijkstraData.txt";
	static final int VERTEX_COUNT = 200;
	static Map<Integer, List<AdjVertex>> adjList = new HashMap<>(VERTEX_COUNT);
	static PriorityQueue<Map.Entry<Integer, Integer>> heap = new PriorityQueue<>(VERTEX_COUNT,
			(Map.Entry.comparingByKey()));
	static Map<Integer, Integer> processed = new HashMap<>();
	static final int MAX_DISTANCE = 1000000;
	static Integer query[] = new Integer[] { 7, 37, 59, 82, 99, 115, 133, 165, 188, 197 };

	public static void main(String[] args) throws FileNotFoundException, IOException {
		readInput();
		dijkstra();
		System.out.println(Stream.of(query).map(q -> processed.get(q)).collect(Collectors.toList()));
	}

	private static void dijkstra() {
		processed.put(1, 0);
		while (processed.size() < VERTEX_COUNT) {
			int nextV = 0;
			int shortestDis = MAX_DISTANCE;
			for (Map.Entry<Integer, Integer> p : processed.entrySet()) {
				for (AdjVertex adj : adjList.get(p.getKey())) {
					if (processed.get(adj.head) == null) {
						int greedyScore = p.getValue() + adj.edgeLength;
						if (greedyScore < shortestDis) {
							nextV = adj.head;
							shortestDis = greedyScore;
						}
					}
				}
			}
			processed.put(nextV, shortestDis);
		}
	}

	private static void readInput() throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
			String is;
			while ((is = reader.readLine()) != null) {
				String adjs[] = is.split("\\s");
				List<AdjVertex> adjl = new ArrayList<>(adjs.length - 1);
				int vertex = Integer.valueOf(adjs[0]);
				adjList.put(vertex, adjl);
				if (adjs.length > 0) {
					Stream.of(adjs).forEach(s -> {
						AdjVertex adj = new AdjVertex(s);
						if (adj.head != 0) {
							adjl.add(adj);
						}
					});
				}
			}
		}
	}

	static class AdjVertex {
		int head;
		int edgeLength;

		AdjVertex(String adjs) {
			String[] parts = adjs.split(",");
			if (parts.length == 2) {
				head = Integer.valueOf(parts[0]);
				edgeLength = Integer.valueOf(parts[1]);
			}
		}
	}
}
