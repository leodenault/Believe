package musicGame.xml;

public class XMLBoolean extends XMLPrimitive<Boolean> {

	@Override
	protected Boolean extractValue(String content) {
		boolean value = Boolean.parseBoolean(content);
		return value;
	}

}
