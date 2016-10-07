import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Assignment2Sum {
	/*
	 * Download the following text file:
	 * 
	 * algo1-programming_prob-2sum.txt
	 * 
	 * The goal of this problem is to implement a variant of the 2-SUM algorithm
	 * (covered in the Week 6 lecture on hash table applications).
	 * 
	 * The file contains 1 million integers, both positive and negative (there
	 * might be some repetitions!).This is your array of integers, with the ith
	 * row of the file specifying the ith entry of the array.
	 * 
	 * Your task is to compute the number of target values t in the interval
	 * [-10000,10000] (inclusive) such that there are distinct numbers x,y in
	 * the input file that satisfy x+y=t. (NOTE: ensuring distinctness requires
	 * a one-line addition to the algorithm from lecture.)
	 * 
	 * Write your numeric answer (an integer between 0 and 20001) in the space
	 * provided.
	 * 
	 * Answer: 427
	 * 
	 * OPTIONAL CHALLENGE: If this problem is too easy for you, try implementing
	 * your own hash table for it. For example, you could compare performance
	 * under the chaining and open addressing approaches to resolving
	 * collisions.
	 */
	static Map<Long, Long> inputs = new HashMap<>();
	static int minT = -10000;
	static int maxT = 10000;
	static String inputFileName = "resources/algo1-programming_prob-2sum.txt";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		readInput();
		HashSet<Integer> c = new HashSet<>();
		for (int t = minT; t < maxT; t++) {
			for (Long k : inputs.keySet()) {
				if (inputs.get(t - k) != null) {
					c.add(t);
				}
			}
			System.out.println("done " + (t - minT) + "/" + (maxT - minT));
		}
		System.out.println(c.size());
	}

	private static void readInput() throws FileNotFoundException, IOException {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFileName))) {
			reader.lines().forEach(l -> {
				long val = Long.valueOf(l);
				inputs.put(val, val);
			});
		}
	}
}
