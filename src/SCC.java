import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SCC {
	/*
	 * The file contains the edges of a directed graph. Vertices are labeled as
	 * positive integers from 1 to 875714. Every row indicates an edge, the
	 * vertex label in first column is the tail and the vertex label in second
	 * column is the head (recall the graph is directed, and the edges are
	 * directed from the first column vertex to the second column vertex). So
	 * for example, the 11th row looks liks : "2 47646". This just means that
	 * the vertex with label 2 has an outgoing edge to the vertex with label
	 * 47646
	 * 
	 * Your task is to code up the algorithm from the video lectures for
	 * computing strongly connected components (SCCs), and to run this algorithm
	 * on the given graph.
	 * 
	 * Output Format: You should output the sizes of the 5 largest SCCs in the
	 * given graph, in decreasing order of sizes, separated by commas (avoid any
	 * spaces). So if your algorithm computes the sizes of the five largest SCCs
	 * to be 500, 400, 300, 200 and 100, then your answer should be
	 * "500,400,300,200,100" (without the quotes). If your algorithm finds less
	 * than 5 SCCs, then write 0 for the remaining terms. Thus, if your
	 * algorithm computes only 3 SCCs whose sizes are 400, 300, and 100, then
	 * your answer should be "400,300,100,0,0" (without the quotes). (Note also
	 * that your answer should not have any spaces in it.)
	 * 
	 * WARNING: This is the most challenging programming assignment of the
	 * course. Because of the size of the graph you may have to manage memory
	 * carefully. The best way to do this depends on your programming language
	 * and environment, and we strongly suggest that you exchange tips for doing
	 * this on the discussion forums.
	 */

	/*
	 * In Eclipse, increase stack size before running this
	 * http://stackoverflow.com/a/2127262
	 * 
	 * Open the Run Configuration for your application (Run/Run
	 * Configurations..., then look for the applications entry in 'Java
	 * application').
	 * 
	 * The arguments tab has a text box Vm arguments, enter -Xss128m (or a bigger
	 * parameter). The default value is 512 kByte.
	 */
	static final int VERTICES_COUNT = 875714;// 12; for SCC-small.txt
	static final int LARGESTS_COUNT = 5;
	static String inputFileName = "resources/SCC.txt";// SCC-small.txt

	static Map<Integer, List<Integer>> g = new HashMap<>(VERTICES_COUNT);
	static Map<Integer, List<Integer>> gReverse = new HashMap<>(VERTICES_COUNT);
	static boolean[] vertexExplored = new boolean[VERTICES_COUNT];
	static int[] vertexFinishes = new int[VERTICES_COUNT];
	static int[] finishVertics = new int[VERTICES_COUNT];
	static TreeMap<Integer, List<Integer>> largests = new TreeMap<>();
	static int finishTime = 0;
	static int sccSize = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		readInput();
		calculateFinishes();
		calculateSCC();
		largests.descendingMap().forEach((s, l) -> {
			System.out.println("size: " + s + ", scc at this size: " + l.size());
		});
	}

	private static void calculateFinishes() {
		for (int v = VERTICES_COUNT; v > 0; v--) {
			if (vertexExplored[v - 1] == false) {
				dfs_getFinish(v);
			}
		}
	}

	private static void dfs_getFinish(int v) {
		vertexExplored[v - 1] = true;
		List<Integer> tails = gReverse.get(v);
		if (tails != null) {
			tails.forEach(j -> {
				if (vertexExplored[j - 1] == false) {
					dfs_getFinish(j);
				}
			});
		}
		finishTime++;
		vertexFinishes[v - 1] = finishTime;
		finishVertics[finishTime - 1] = v;
	}

	private static void calculateSCC() {
		// reset explored array
		for (int f = 0; f < VERTICES_COUNT; f++) {
			vertexExplored[f] = false;
		}
		for (int f = VERTICES_COUNT; f > 0; f--) {
			if (vertexExplored[f - 1] == false) {
				sccSize = 0;
				dfs_getSCC(f);
				checkForLargests(f);
			}
		}
	}

	private static void checkForLargests(int f) {
		if (largests.size() <= LARGESTS_COUNT) {
			addToLargests(f);
		} else {
			if (largests.firstKey() < sccSize) {
				addToLargests(f);
			}
		}

		while (largests.size() > LARGESTS_COUNT) {
			largests.remove(largests.firstKey());
		}
	}

	private static void addToLargests(int f) {
		List<Integer> leaders = largests.get(sccSize);
		if (leaders == null) {
			leaders = new ArrayList<Integer>();
			largests.put(sccSize, leaders);
		}
		leaders.add(f);
	}

	private static void dfs_getSCC(int f) {
		vertexExplored[f - 1] = true;
		sccSize++;
		List<Integer> heads = g.get(finishVertics[f - 1]);
		if (heads != null) {
			heads.forEach(v -> {
				int fNew = vertexFinishes[v - 1];
				if (vertexExplored[fNew - 1] == false) {
					dfs_getSCC(fNew);
				}
			});
		}
	}

	private static void readInput() throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
			String edge;
			while ((edge = reader.readLine()) != null) {
				String th[] = edge.split("\\s");
				if (th.length == 2) {
					Integer tail = Integer.valueOf(th[0]);
					Integer head = Integer.valueOf(th[1]);
					List<Integer> vh = g.get(tail);
					if (vh == null) {
						vh = new ArrayList<Integer>();
						g.put(tail, vh);
					}
					vh.add(head);
					List<Integer> hv = gReverse.get(head);
					if (hv == null) {
						hv = new ArrayList<Integer>();
						gReverse.put(head, hv);
					}
					hv.add(tail);
				}
			}
			System.out.println("read:" + g.size() + " tail vertices, " + gReverse.size() + " head vertices.");
		}
	}
}
