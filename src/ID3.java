import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ID3 {

	public static Node AttributeSelector(DataSet dataset, List<Integer> attrList) {

		Double infoD = 0.0;
		for (String c : dataset.classes.keySet()) {
			double temp = dataset.classes.get(c) / dataset.data.size();
			infoD -= temp * (Math.log(temp) / Math.log(2));
		}
		
		Double maxGain = -1.0;
		int selectedAttr = -1;
		Map<String, DataSet> selectedDatasets = null;
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
			Double infoDA = 0.0;
			for (DataSet dj : djs.values()) {
				double weight = ((double) dj.data.size()) / dataset.data.size();
				Double infoDj = 0.0;
				for (Map.Entry<String, Double> c : dj.classes.entrySet()) {
					double temp = c.getValue() / dj.data.size();
					infoDj -= temp * (Math.log(temp) / Math.log(2));
				}
				infoDA += weight * infoDj;
			}
			Double gain = (infoD - infoDA);
			if (gain > maxGain) {
				maxGain = infoD - infoDA;
				selectedAttr = i;
				selectedDatasets = djs;
			}
			if(selectedAttr == -1)
				System.out.println("ERROR");
		}

		Node result = new Node(selectedAttr);
		for (Map.Entry<String, DataSet> dj : selectedDatasets.entrySet()) {
			BranchDecision branch = new BranchDecision();
			branch.equalTo = dj.getKey();
			branch.partitionedDataSet = dj.getValue();
			result.branches.add(branch);
		}
		attrList.remove(Integer.valueOf(result.attr));

		// System.out.println(selectedAttr + ":" + maxGain);

		return result;
	}

}
