import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class InversionCount {
	static int ARRAY_SIZE = 10;
	static int a[] = new int[ARRAY_SIZE];
	static String inputFileName = "src/IntegerArray.txt";

	public static void main(String args[]) {
		readInput();
		System.out.println("Inversion Count is: " + sortAndCount(0, ARRAY_SIZE) + " for ARRAY_SIZE " + ARRAY_SIZE);
	}

	private static long sortAndCount(int s, int e) {
		System.out.println("s:" + s + ", e:" + e);
		if (e - s <= 1) {
			return 0;
		}
		int m = (s + e) / 2;
		long x = sortAndCount(s, m);
		long y = sortAndCount(m, e);
		long z = countSplitInversion(s, e);
		return x + y + z;
	}

	private static long countSplitInversion(int s, int e) {
		long c = 0;
		int i = 0, j = 0, n = e - s, m = (s + e) / 2;		
		int d[] = new int[n];
		for (int k = 0; k < n; k++) {
			int bi = s + i, cj = m + j;
			if (bi == m) {
				d[k] = a[cj];
				j++;
				continue;
			}
			if (cj == e) {
				d[k] = a[bi];
				i++;
				continue;
			}
			if (a[bi] > a[cj]) {
				d[k] = a[cj];
				c += m - bi;
				j++;
			} else {
				d[k] = a[bi];
				i++;
			}
		}
		for (int k = 0; k < n; k++) {
			a[s + k] = d[k];
		}
		return c;
	}

	private static void readInput() {
		try (FileReader r = new FileReader(inputFileName); BufferedReader br = new BufferedReader(r);) {
			String is;
			for (int i = 0; (i < ARRAY_SIZE) && ((is = br.readLine()) != null); i++) {
				a[i] = Integer.valueOf(is);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("can't file input: " + inputFileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("can't read file input: " + inputFileName);
		}
	}
}