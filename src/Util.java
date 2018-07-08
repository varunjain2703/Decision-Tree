import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Util {

	private Util() {
	}

	/* READ CSV FILE STORE TUPLES INTO DATABASE */
	public static DataSet readCSV(String filePath) {

		Scanner in;
		DataSet dataset = new DataSet();

		try {

			in = new Scanner(new File(filePath));

			
			/*TODO: IF FIRST ROW IS INFORMATION */
			//String str = in.next();
			

			double maxCount = Double.MIN_VALUE;
			while (in.hasNext()) {
				String[] strAttrs = in.next().split(",");

				dataset.data.add(strAttrs);
				String c = strAttrs[strAttrs.length - 1];
				if (dataset.classes.containsKey(c)) {
					dataset.classes.put(c, dataset.classes.get(c) + 1);
				} else {
					dataset.classes.put(c, 1.0);
				}
				if (dataset.classes.get(c) > maxCount) {
					maxCount = dataset.classes.get(c);
					dataset.majorityClass = c;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}

		return dataset;
	}

	public static void printDataSet(DataSet dataset) {
		for (String[] attr : dataset.data) {
			for (String string : attr) {
				System.out.print(string + ",");
			}
			System.out.println();
		}
	}

	public static void printTree(Node root) {
		Queue<Node> list = new LinkedList();
		list.add(root);
		int curc = root.branches.size();
		int ncurc = 0;
		System.out.println(" A:" + root.attr);
		while (!list.isEmpty()) {
			Node cur = list.remove();
			for (BranchDecision branch : cur.branches) {
				System.out.print(" B:" + (null != branch.equalTo ? branch.equalTo : branch.attrSet) + "->");
				if (branch.resultant.attr != -1) {
					System.out.print(" A:" + branch.resultant.attr);
					list.add(branch.resultant);
					ncurc += branch.resultant.branches.size();
				} else
					System.out.print(" L:" + branch.resultant.label);
				System.out.print(",");
				curc--;
			}
			System.out.print("||");
			if (curc == 0) {
				curc = ncurc;
				ncurc = 0;
				System.out.println();
			}
		}
	}

	public static <T> Set<Set<T>> combinations(List<T> groupSize, int k) {

		Set<Set<T>> allCombos = new HashSet<Set<T>>();
		// base cases for recursion
		if (k == 0) {
			// There is only one combination of size 0, the empty team.
			allCombos.add(new HashSet<T>());
			return allCombos;
		}
		if (k > groupSize.size()) {
			// There can be no teams with size larger than the group size,
			// so return allCombos without putting any teams in it.
			return allCombos;
		}

		// Create a copy of the group with one item removed.
		List<T> groupWithoutX = new ArrayList<T>(groupSize);
		T x = groupWithoutX.remove(groupWithoutX.size() - 1);

		Set<Set<T>> combosWithoutX = combinations(groupWithoutX, k);
		Set<Set<T>> combosWithX = combinations(groupWithoutX, k - 1);
		for (Set<T> combo : combosWithX) {
			combo.add(x);
		}
		allCombos.addAll(combosWithoutX);
		allCombos.addAll(combosWithX);
		return allCombos;
	}

	public static DataSet combineDataSet(Set<String> combination, Map<String, DataSet> djs) {
		DataSet dataset = new DataSet();
		for (String key : combination) {
			dataset.data.addAll(djs.get(key).data);
			for (Map.Entry<String, Double> entry : djs.get(key).classes.entrySet()) {
				if (!dataset.classes.containsKey(entry.getKey())) {
					dataset.classes.put(entry.getKey(), entry.getValue());
					if (null == dataset.majorityClass) {
						dataset.majorityClass = entry.getKey();
					}
				} else
					dataset.classes.put(entry.getKey(), dataset.classes.get(entry.getKey()) + entry.getValue());
				if (dataset.classes.get(dataset.majorityClass) < dataset.classes.get(entry.getKey())) {
					dataset.majorityClass = entry.getKey();
				}
			}
		}
		return dataset;
	}

}
