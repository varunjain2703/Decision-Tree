import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	// Attribute DataSet_modified.csv, example (1).csv, Dresses_Attribute_Sales.csv
	// Attribute DataSet_modified._p.csv, Dresses_Attribute_Sales._p.csv
	static final String trainingFilePath = "Attribute DataSet_modified.csv";
	static final String validationFilePath = "test.csv";
	static final String method = "CART"; // ID3, C45, CART

	public static void main(String[] args) {
		DataSet dataset = Util.readCSV(trainingFilePath);
		
		/*Print Training Dataset*/
		// Util.printDataSet(dataset);

		/*Print Majority Class*/
		// System.out.println(dataset.majorityClass);

		long startTime = System.currentTimeMillis();
		List<Integer> attrList = new ArrayList();
		for (int i = 0; i < dataset.data.get(0).length - 1; i++) {
			attrList.add(i);
		}
		Node root = buildTree(dataset, attrList);

		long endTime = System.currentTimeMillis();
		System.out.println("Training Run Time:" + (endTime - startTime));
		
		//System.out.println("Tree Built");
		
		/*Print Tree*/
		//Util.printTree(root);
		
		startTime = System.currentTimeMillis();
		validation(root);
		endTime = System.currentTimeMillis();
		System.out.println("Validation Run Time:" + (endTime - startTime));

	}

	private static void validation(Node root) {
		DataSet validationData = Util.readCSV(validationFilePath);
		double count = 0;
		for (String[] strings : validationData.data) {
			String result = predict(root, strings);
			/*if (null == result) {
				System.out.println();
			}*/
			if (null != result && result.equalsIgnoreCase(strings[strings.length - 1])) {
				count++;
			}
		}
		System.out.println(count / validationData.data.size());
	}

	public static String predict(Node root, String[] inpRecord) {
		if (root.attr == -1) {
			return root.label;
		}
		for (BranchDecision branch : root.branches) {
			if (null != branch.equalTo) {
				if (branch.equalTo.equals(inpRecord[root.attr])) {
					return predict(branch.resultant, inpRecord);
				}
			} else {
				if (branch.attrSet.contains(inpRecord[root.attr])) {
					return predict(branch.resultant, inpRecord);
				}
			}
		}
		return null;
	}

	public static Node buildTree(DataSet dataset, List<Integer> attrList) {
		Node curr;
		if (dataset.classes.keySet().size() == 1) {
			return new Node(dataset.majorityClass);
		}
		if (attrList.isEmpty()) {
			return new Node(dataset.majorityClass);
		}
		if (method.equalsIgnoreCase("ID3")) {
			curr = ID3.AttributeSelector(dataset, attrList);
		} else if (method.equalsIgnoreCase("C45")) {
			curr = C45.AttributeSelector(dataset, attrList);
		} else {
			curr = CART.AttributeSelector(dataset, attrList);
		}

		/* make condition here-> moved to attrSelection */
		// attrList.remove(Integer.valueOf(curr.attr));

		if (curr.attr == -1)
			return new Node(dataset.majorityClass);

		for (BranchDecision branch : curr.branches) {
			if (branch.partitionedDataSet.data.isEmpty()) {
				return new Node(dataset.majorityClass);
			} else {
				branch.resultant = buildTree(branch.partitionedDataSet, attrList);
			}
		}

		return curr;

	}

}
