package musicGame.xml;

public class ListDef extends ChildDef {
	public CompoundDef subChildDef;
	
	public ListDef(String name, CompoundDef subChildDef) {
		super(name);
		this.subChildDef = subChildDef;
	}
}
