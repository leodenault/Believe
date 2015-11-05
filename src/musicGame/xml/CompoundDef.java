package musicGame.xml;

import java.util.LinkedList;
import java.util.List;

public class CompoundDef extends ChildDef {
	private List<ChildDef> subChildDefs;
	
	public CompoundDef(String name) {
		super(name);
		this.subChildDefs = new LinkedList<ChildDef>();
	}
	
	public void addSubChildDef(ChildDef def) {
		this.subChildDefs.add(def);
	}
	
	public List<ChildDef> getSubChildDefs() {
		return subChildDefs;
	}
}
