import java.util.*;


public class Node {
	
	int attr = -1;
	Set<String> attrSet; // not used
	String label = "ERROR";
	
	ArrayList<BranchDecision> branches;
	
	Node(String label){
		this.label = label;
		this.branches = new ArrayList<>();
		attrSet = new HashSet<String> ();
	}
	
	Node(int attr){
		this.attr = attr;
		this.branches = new ArrayList<>();
		attrSet = new HashSet<String> ();
	}
	
}
