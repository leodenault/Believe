package musicGame.core;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

import musicGame.xml.CompoundDef;
import musicGame.xml.ListDef;
import musicGame.xml.XMLBoolean;
import musicGame.xml.XMLCompound;
import musicGame.xml.XMLDataParser;
import musicGame.xml.XMLInteger;
import musicGame.xml.XMLList;
import musicGame.xml.XMLLoadingException;
import musicGame.xml.XMLString;

public class SpriteSheetManager {
	
	private static final String FILE_LOCATION = "/data/spriteSheets.xml";
	private static final String TOP_NODE = "spriteSheets";
	private static final String SPRITE_SHEET_NODE = "spriteSheet";
	private static final String NAME_NODE = "name";
	private static final String SHEET_NODE = "sheet";
	private static final String WIDTH_NODE = "frameWidth";
	private static final String HEIGHT_NODE = "frameHeight";
	private static final String SEQUENCES_NODE = "frameSequences";
	
	private static final String SEQUENCE_NODE = "sequence";
	private static final String START_FRAME_NODE = "start";
	private static final String END_FRAME_NODE = "end";
	private static final String LENGTH_NODE = "frameLength";
	private static final String LOOPING_NODE = "looping";
	private static final String PING_PONG_NODE = "pingPong";
	private final static ListDef SCHEMA = new ListDef(
			TOP_NODE,
			new CompoundDef(SPRITE_SHEET_NODE).addString(NAME_NODE)
			.addString(SHEET_NODE)
			.addInteger(WIDTH_NODE)
			.addInteger(HEIGHT_NODE)
			.addList(SEQUENCES_NODE,
					new CompoundDef(SEQUENCE_NODE).addString(NAME_NODE)
					.addInteger(START_FRAME_NODE)
					.addInteger(END_FRAME_NODE)
					.addInteger(LENGTH_NODE)
					.addBoolean(LOOPING_NODE)
					.addBoolean(PING_PONG_NODE)
					)
			);
	
	private static SpriteSheetManager INSTANCE;
	
	private String spriteSheetData;
	private HashMap<String, AnimationSet> sheets;
	
	private SpriteSheetManager(String spriteSheedData) {
		this.spriteSheetData = spriteSheedData;
		this.sheets = new HashMap<String, AnimationSet>();
		loadSpriteSheet();
	}
	
	protected SpriteSheetManager() {}
	
	public static SpriteSheetManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SpriteSheetManager(FILE_LOCATION);
		}
		return INSTANCE;
	}
	
	public AnimationSet getAnimationSet(String name) {
		return sheets.get(name).copy();
	}
	
	private void loadSpriteSheet() {
		XMLDataParser parser = new XMLDataParser(spriteSheetData, SCHEMA);
		
		try {
			XMLList top = parser.loadFile();
			
			int childNum = 0;
			for (XMLCompound child : top.children) {
				String sheetName = child.<XMLString>getValue(NAME_NODE).value;
				if (sheets.containsKey(sheetName)) {
					Log.warn(String.format("Sheet #%d with name %s was already specified. Overriding"
							+ " original value", childNum, sheetName));
				}
				
				SpriteSheet sheet = new SpriteSheet(
						child.<XMLString>getValue(SHEET_NODE).value,
						child.<XMLInteger>getValue(WIDTH_NODE).value,
						child.<XMLInteger>getValue(HEIGHT_NODE).value
						);
				
				HashMap<String, Animation> animations = new HashMap<String, Animation>();
				for (XMLCompound sequence : child.<XMLList>getValue(SEQUENCES_NODE).children) {
					animations.put(
							sequence.<XMLString>getValue(NAME_NODE).value,
							createAnimation(
									sheet,
									sequence.<XMLInteger>getValue(LENGTH_NODE).value,
									sequence.<XMLInteger>getValue(START_FRAME_NODE).value,
									sequence.<XMLInteger>getValue(END_FRAME_NODE).value,
									sequence.<XMLBoolean>getValue(LOOPING_NODE).value,
									sequence.<XMLBoolean>getValue(PING_PONG_NODE).value
									));
				}

				sheets.put(sheetName, new AnimationSet(animations));
				childNum++;
			}
		} catch (SlickException | XMLLoadingException e) {
			Log.error(String.format("There was an error attempting to"
					+ " read the sprite sheet data file named %s. The exception is listed below:\n\n%s",
					spriteSheetData, e.getMessage()));
		}
	}
	
	private Animation createAnimation(SpriteSheet sheet, int length, int start, int end,
			boolean looping, boolean pingPong) throws SlickException {
		Animation anim = new Animation();
		int horizontalCount = sheet.getHorizontalCount();
		
		for (int i = start; i <= end; i++) {
			anim.addFrame(sheet.getSprite(i % horizontalCount, i / horizontalCount), length);
		}
		
		anim.setAutoUpdate(false);
		anim.setLooping(looping);
		anim.setPingPong(pingPong);
		return anim;
	}
}
