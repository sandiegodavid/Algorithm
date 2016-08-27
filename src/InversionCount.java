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
		int ic = sortAndCount(0, ARRAY_SIZE);
		System.out.println("Inversion Count is: " + ic);
	}

	private static int sortAndCount(int s, int e) {
		if (e - s <= 1) {
			return 0;
		}
		int m = (s + e) / 2;
		int x = sortAndCount(s, m);
		int y = sortAndCount(m, e);
		int z = countSplitInversion(s, e);
		return x + y + z;
	}

	private static int countSplitInversion(int s, int e) {
		int c = 0, i = 0, j = 0, n = e - s, m = (s + e) / 2;
		int l[] = new int[n];
		for (int k = 0; k < n; k++) {
			int bi = s + i, cj = m + j;
			if (bi == m) {
				l[k] = a[cj];
				j++;
				continue;
			}
			if (cj == e) {
				l[k] = a[bi];
				i++;
				if (k + 1 < n) {
					c++;
				}
				continue;
			}
			if (a[bi] > a[cj]) {
				l[k] = a[cj];
				c++;
				j++;
			} else {
				l[k] = a[bi];
				i++;
			}
		}
		for (int k = 0; k < n; k++) {
			a[s + k] = l[k];
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