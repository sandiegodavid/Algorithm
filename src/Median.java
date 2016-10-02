import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

public class Median {
	/*
	 * Download the following text file:
	 * 
	 * Median.txt
	 * 
	 * The goal of this problem is to implement the "Median Maintenance"
	 * algorithm (covered in the Week 5 lecture on heap applications). The text
	 * file contains a list of the integers from 1 to 10000 in unsorted order;
	 * you should treat this as a stream of numbers, arriving one by one.
	 * Letting xi denote the ith number of the file, the kth median mk is
	 * defined as the median of the numbers x1,…,xk. (So, if k is odd, then mk
	 * is ((k+1)/2)th smallest number among x1,…,xk; if k is even, then mk is
	 * the (k/2)th smallest number among x1,…,xk.)
	 * 
	 * In the box below you should type the sum of these 10000 medians, modulo
	 * 10000 (i.e., only the last 4 digits). That is, you should compute
	 * (m1+m2+m3+⋯+m10000)mod10000.
	 * 
	 * Answer: 1213
	 * 
	 * OPTIONAL EXERCISE: Compare the performance achieved by heap-based and
	 * search-tree-based implementations of the algorithm.
	 */

	static int medianTotal = 0;
	static String inputFileName = "resources/Median.txt";
	static PriorityQueue<Integer> lower = new PriorityQueue<>((x, y) -> (y - x));
	static PriorityQueue<Integer> higher = new PriorityQueue<>((x, y) -> (x - y));

	public static void main(String[] args) throws NumberFormatException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					doMedian(Integer.valueOf(line));
				}
			}
		}
		System.out.println(medianTotal % 10000);
	}

	private static void doMedian(Integer element) {
		if (lower.peek() == null) {
			lower.add(element);
			medianTotal = element;
			return;
		}
		if (lower.peek() >= element) {
			lower.add(element);
			if (lower.size() == higher.size() + 2) {
				higher.add(lower.poll());
			}
		} else {
			higher.add(element);
			if (higher.size() == lower.size() + 1) {
				lower.add(higher.poll());
			}
		}
		medianTotal += lower.peek();
	}
}
