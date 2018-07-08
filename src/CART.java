import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CART {

	public static Node AttributeSelector(DataSet dataset, List<Integer> attrList) {

		double giniD = getGini(dataset);
		
		Double maxGini = -1.0;
		int selectedAttr = -1;
		BranchDecision leftBranch = new BranchDecision();
		BranchDecision rightBranch = new BranchDecision();
		for (Integer i : attrList) {
			Map<String, DataSet> djs = new HashMap();
			for (String[] str : dataset.data) {

				if (!djs.containsKey(str[i])) {
					djs.put(str[i], new DataSet());
				}
				djs.get(str[i]).data.add(str);

				if (!djs.get(str[i]).classes.containsKey(str[str.length - 1])) {
					djs.get(str[i]).classes.put(str[str.length - 1], 1.0);
					if (null == djs.get(str[i]).majorityClass) {
						djs.get(str[i]).majorityClass = str[str.length - 1];
					}
				} else {
					djs.get(str[i]).classes.put(str[str.length - 1],
							djs.get(str[i]).classes.get(str[str.length - 1]) + 1);
				}

				if (djs.get(str[i]).classes.get(djs.get(str[i]).majorityClass) < djs.get(str[i]).classes
						.get(str[str.length - 1])) {
					djs.get(str[i]).majorityClass = str[str.length - 1];
				}
			}

			/*if(djs.keySet().size() == 1)
				System.out.println("ERROR");*/
			List<String> dVAttr = new ArrayList(djs.keySet());
			for (int j = 1; j < djs.keySet().size(); j++) {
				Set<Set<String>> combinations = Util.combinations(dVAttr, j);
				for (Set<String> branch1 : combinations) {
					DataSet D1 = Util.combineDataSet(branch1, djs);
					Set<String> branch2 = new HashSet(djs.keySet());
					branch2.removeAll(branch1);
					DataSet D2 = Util.combineDataSet(branch2, djs);
					double giniA = getGiniA(dataset, D1, D2, giniD);
					if (giniA > maxGini) {
						maxGini = giniA;
						selectedAttr = i;
						leftBranch.attrSet = branch1;
						rightBranch.attrSet = branch2;
						leftBranch.partitionedDataSet = D1;
						rightBranch.partitionedDataSet = D2;
					}
					if(selectedAttr == -1)
						System.out.println("ERROR");
				}
			}
			/*if(selectedAttr == -1)
				System.out.println("ERROR");*/
			
		}

		Node result = new Node(selectedAttr);
		if(null != leftBranch.partitionedDataSet)
			result.branches.add(leftBranch);
		if(null != rightBranch.partitionedDataSet)
			result.branches.add(rightBranch);
		
		
		
		//attrList.remove(Integer.valueOf(result.attr));

		// System.out.println(selectedAttr + ":" + maxGain);

		return result;
	}
	
	private static double getGini(DataSet dataset) {
		double giniD = 0.0;
		for (String c : dataset.classes.keySet()) {
			double pi = dataset.classes.get(c) / dataset.data.size();
			giniD -= pi * pi;
		}
		giniD++;
		return giniD;
	}

	private static double getGiniA(DataSet D, DataSet D1, DataSet D2, double giniD) {
		double giniDA = 0.0;
		giniDA = (((double)(D1.data.size()) / D.data.size()) * getGini(D1)) + (((double)(D2.data.size()) / D.data.size()) * getGini(D2));
		return giniD - giniDA;
	}

}
