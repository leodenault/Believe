package musicGame.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import musicGame.core.exception.SpriteSheetLoadingException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

public class SpriteSheetManager {
	
	private static final String FILE_LOCATION = "data/spriteSheets.xml";
	private static final String SPRITE_SHEET_NODE = "spriteSheet";
	private static final String NAME_NODE = "name";
	private static final String SHEET_NODE = "sheet";
	private static final String WIDTH_NODE = "frameWidth";
	private static final String HEIGHT_NODE = "frameHeight";
	private static final String LENGTH_NODE = "frameLength";
	private static final List<String> NODE_NAMES =
			Arrays.asList(NAME_NODE, SHEET_NODE, WIDTH_NODE, HEIGHT_NODE, LENGTH_NODE);
	
	private static SpriteSheetManager INSTANCE;
	
	private String spriteSheetData;
	private HashMap<String, Animation> sheets;
	
	private SpriteSheetManager(String spriteSheedData) {
		this.spriteSheetData = spriteSheedData;
		this.sheets = new HashMap<String, Animation>();
		loadSpriteSheet();
	}
	
	protected SpriteSheetManager() {}
	
	public static SpriteSheetManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SpriteSheetManager(FILE_LOCATION);
		}
		return INSTANCE;
	}
	
	public Animation getSpriteSheet(String name) {
		Animation anim = sheets.get(name);
		return anim.copy();
	}
	
	private void loadSpriteSheet() {
		XMLParser parser = new XMLParser();
		int child = 0;
		
		try {
			XMLElement root = parser.parse(spriteSheetData);
			XMLElementList children = root.getChildren();
			
			for (; child < children.size(); child++) {
				SpriteSheetDatum datum = extractSheet(children.get(child));
				
				if (sheets.containsKey(datum.name)) {
					Log.warn(String.format("Child #%d with name %s was already specified. Overriding"
							+ " original value", child, datum.name));
				}
				
				sheets.put(datum.name, new Animation(
						new SpriteSheet(datum.sheet, datum.frameWidth, datum.frameHeight),
						datum.frameLength));
			}
		} catch (SlickException e) {
			Log.error(String.format("There was an error attempting to"
					+ " read the sprite sheet data file named %s. The exception is listed below:\n\n%s",
					spriteSheetData, e.getMessage()));
		} catch (SpriteSheetLoadingException | NumberFormatException e) {
			Log.error(String.format("There was an error trying to parse sheet #%d in %s. The reason given:\n\n%s",
					child, spriteSheetData, e.getMessage()));
		}
	}
	
	protected SpriteSheetDatum extractSheet(XMLElement sheetDatum)
			throws SpriteSheetLoadingException, NumberFormatException {
		String nodeName = sheetDatum.getName();
		
		if (!nodeName.equals(SPRITE_SHEET_NODE)) {
			throw new SpriteSheetLoadingException(String.format(
					"The description of a sprite sheet should have the name"
							+ "\"%s\"", SPRITE_SHEET_NODE));
		}
		
		XMLElementList children = sheetDatum.getChildren();
		List<String> childrenParsed = new LinkedList<String>();
		SpriteSheetDatum datum = new SpriteSheetDatum();

		int numChildren = children.size();
		if (numChildren != NODE_NAMES.size()) {
			throw new SpriteSheetLoadingException(String.format(
					"The description of a sprite sheet should have exactly 4"
					+ " children. They should be named %s, respectively",
					createNodeNameListString(NODE_NAMES)));
		}
		
		for (int i = 0; i < numChildren; i++) {
			XMLElement child = children.get(i);
			String name = child.getName();
			if (!NODE_NAMES.contains(name)) {
				throw new SpriteSheetLoadingException(String.format("The name of a child in a"
						+ " sprite sheet description should be one of %s",
						createNodeNameListString(NODE_NAMES)));
			} else if (childrenParsed.contains(name)) {
				throw new SpriteSheetLoadingException(String.format("Cannot specify child with"
						+ " name %s as it has already been specified", name));
			} else if (child.getChildren().size() != 0) {
				throw new SpriteSheetLoadingException("Sprite sheet description"
						+ " should not contain any children, only content");
			}
			
			String content = child.getContent();
			setDatumAttribute(datum, name, content);
			childrenParsed.add(name);
		}
		return datum;
	}
	
	private void setDatumAttribute(SpriteSheetDatum datum, String attributeName, String attribute) {
		if (attributeName.equals(SHEET_NODE)) {
			datum.sheet = attribute;
		} else if (attributeName.equals(NAME_NODE)) {
			datum.name = attribute;
		} else {
			int value = Integer.parseInt(attribute);
			
			if (attributeName.equals(WIDTH_NODE)) {
				datum.frameWidth = value;
			} else if (attributeName.equals(HEIGHT_NODE)) {
				datum.frameHeight = value;
			} else if (attributeName.equals(LENGTH_NODE)) {
				datum.frameLength = value;
			}
		}
	}
	
	private String createNodeNameListString(List<String> nodes) {
		StringBuilder builder = new StringBuilder();
		for (String node : nodes) {
			builder.append(node);
			builder.append(", ");
		}
		builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}
}
