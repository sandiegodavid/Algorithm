import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MinCut {
	/*
	 * The file contains the adjacency list representation of a simple
	 * undirected graph. There are 200 vertices labeled 1 to 200. The first
	 * column in the file represents the vertex label, and the particular row
	 * (other entries except the first column) tells all the vertices that the
	 * vertex is adjacent to. So for example, the 6th row looks like :
	 * "6	155	56	52	120	......". This just means that the vertex with label
	 * 6 is adjacent to (i.e., shares an edge with) the vertices with labels
	 * 155,56,52,120,......,etc
	 * 
	 * Your task is to code up and run the randomized contraction algorithm for
	 * the min cut problem and use it on the above graph to compute the min cut.
	 * (HINT: Note that you'll have to figure out an implementation of edge
	 * contractions. Initially, you might want to do this naively, creating a
	 * new graph from the old every time there's an edge contraction. But you
	 * should also think about more efficient implementations.) (WARNING: As per
	 * the video lectures, please make sure to run the algorithm many times with
	 * different random seeds, and remember the smallest cut that you ever
	 * find.) Write your numeric answer in the space provided. So e.g., if your
	 * answer is 5, just type 5 in the space provided.
	 */
	final static int VERTEX_COUNT = 200;
	static int[][] graph = new int[VERTEX_COUNT][];
	static String inputFileName = "resources/kargerMinCut.txt";

	public static void main(String args[]) throws FileNotFoundException, IOException {
		int minCut = VERTEX_COUNT * (VERTEX_COUNT - 1);
		int loopCnt = (int) (VERTEX_COUNT * VERTEX_COUNT * Math.log(VERTEX_COUNT));
		loopCnt = 20;
		for (int i = 0; i < loopCnt; i++) {
			readInput();
			int myMinCut = minCut();
			if (minCut > myMinCut) {
				minCut = myMinCut;
			}
		}
		System.out.println("minCut: " + minCut + " after " + loopCnt + " tries.");
	}

	// Note:
	// - if graph[i][0] == 0, vertex [i] has been collapsed
	private static int minCut() {
		int remainingVertices = VERTEX_COUNT;
		while (remainingVertices > 2) {
			collapse();
			remainingVertices--;
		}
		return getRemainingEdgeCount(true);
	}

	private static void collapse() {
		// randomly choose from all remaining edges in the graph
		int edgeCnt = getRemainingEdgeCount(false);
		int edgeToCollapse = (int) (Math.random() * edgeCnt);
		// count edgeToCollapse-th edges which haven't been collapsed yet
		for (int i = 0; i < VERTEX_COUNT; i++) {
			if (graph[i][0] != 0) {
				for (int j = 1; j < graph[i].length; j++) {
					if (edgeToCollapse == 0) {
						collapse(i, graph[i][j] - 1);
						return;
					}
					edgeToCollapse--;
				}
			}
		}
	}

	private static int getRemainingEdgeCount(boolean doPrint) {
		int edgeCnt = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			if (graph[i][0] != 0) {
				edgeCnt += graph[i].length - 1;
				if (doPrint) {
					System.out.println(Arrays.toString(graph[i]));
				}
			}
		}
		return edgeCnt / 2;
	}

	private static void collapse(int collapsedVertex, int newVertex) {
		// to make indexing easy, always collapse to the smaller vertex
		if (collapsedVertex < newVertex) {
			int swp = collapsedVertex;
			collapsedVertex = newVertex;
			newVertex = swp;
		}

		// remove self loops
		int selfLoops = 0;
		for (int i = 1; i < graph[collapsedVertex].length; i++) {
			if (graph[collapsedVertex][i] == newVertex + 1) {
				graph[collapsedVertex][i] = 0;
				selfLoops++;
			}
		}
		for (int i = 1; i < graph[newVertex].length; i++) {
			if (graph[newVertex][i] == collapsedVertex + 1) {
				graph[newVertex][i] = 0;
				selfLoops++;
			}
		}
		int[] collapsedEdges = new int[graph[collapsedVertex].length - 1 + graph[newVertex].length - selfLoops];

		// move edges incident on the collapsed vertex to the new vertex
		collapsedEdges[0] = newVertex + 1;
		int e = 1;
		for (int i = 1; i < graph[collapsedVertex].length; i++) {
			if (graph[collapsedVertex][i] != 0) {
				collapsedEdges[e] = graph[collapsedVertex][i];
				e++;
			}
		}
		for (int i = 1; i < graph[newVertex].length; i++) {
			if (graph[newVertex][i] != 0) {
				collapsedEdges[e] = graph[newVertex][i];
				e++;
			}
		}
		graph[newVertex] = collapsedEdges;

		// mark collapsed vertex as 0
		graph[collapsedVertex][0] = 0;

		// replace collapsedVertex with newVertex in the rest of the graph
		for (int i = 0; i < VERTEX_COUNT; i++) {
			if (graph[i][0] != 0) {
				for (int j = 1; j < graph[i].length; j++) {
					if (graph[i][j] == collapsedVertex + 1) {
						graph[i][j] = newVertex + 1;
					}
				}
			}
		}
	}

	private static void readInput() throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
			String is;
			for (int i = 0; (i < VERTEX_COUNT) && ((is = reader.readLine()) != null); i++) {
				String vs[] = is.split("\\s");
				graph[i] = new int[vs.length];
				for (int j = 0; j < vs.length; j++) {
					graph[i][j] = Integer.valueOf(vs[j]);
				}
			}
		}
	}
}