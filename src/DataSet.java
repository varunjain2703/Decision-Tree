import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {

	//ArrayList<Attribute> attrs;
	List<String[]> data = new ArrayList();
	Map<String, Double> classes = new HashMap();
	String majorityClass = null;
}
