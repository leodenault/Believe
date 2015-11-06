package musicGame.xml;

import java.util.HashMap;

public class XMLNodeFactory {
	private static interface Creator<T extends XMLNode> {
		T createNode(ChildDef childDef);
	}
	
	private static XMLNodeFactory INSTANCE;
	@SuppressWarnings("serial")
	private static HashMap<Class<? extends ChildDef>, Creator<? extends XMLNode>> CREATORS =
		new HashMap<Class<? extends ChildDef>, Creator<? extends XMLNode>>() {{
		put(StringDef.class, new Creator<XMLString>() {
			@Override
			public XMLString createNode(ChildDef childDef) {
				return new XMLString();
			}
		});
		put(IntegerDef.class, new Creator<XMLInteger>() {
			@Override
			public XMLInteger createNode(ChildDef childDef) {
				return new XMLInteger();
			}
		});
		put(ListDef.class, new Creator<XMLList>() {
			@Override
			public XMLList createNode(ChildDef childDef) {
				return new XMLList(((ListDef)childDef).subChildDef);
			}
		});
		put(CompoundDef.class, new Creator<XMLCompound>() {
			@Override
			public XMLCompound createNode(ChildDef childDef) {
				return new XMLCompound(((CompoundDef)childDef).getSubChildDefs());
			}
		});
	}};
	
	private XMLNodeFactory() {}
	
	public static XMLNodeFactory getIntance() {
		if (INSTANCE == null) {
			INSTANCE = new XMLNodeFactory();
		}
		return INSTANCE;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends XMLNode> T createNode(ChildDef childDef) {
		return (T)CREATORS.get(childDef.getClass()).createNode(childDef);
	}
}
