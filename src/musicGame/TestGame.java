package musicGame;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import musicGame.GUI.GraphicsUtils;
import musicGame.levelFlow.FlowComponent;
import musicGame.levelFlow.IFlowComponentListener;
import musicGame.levelFlow.parsing.LegacyFlowFileParser;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

public class TestGame extends BasicGame implements IFlowComponentListener, ComponentListener {

	private List<AbstractComponent> rendered;
	private List<File> paths;
	private FlowComponent scoreBar;
	private String file = null;
	private String currentDirectory;
	private Image buttonImage;
	private Animation character;
	private enum State { PICK_MODE, PICK_FILE, PLAYING };
	private State currentState = State.PICK_MODE;
	private boolean readLocal = false;
	private boolean switchDirectory = false;
	private int screenWidth;
	private int screenHeight;
	private int sucess = 0;
	private int fail = 0;
	private int missed = 0;

	public TestGame(String title, int width, int height) {
		super(title);
		this.screenWidth = width;
		this.screenHeight = height;
	}

	public void run() {
		try {
			AppGameContainer game = new AppGameContainer(this);
			game.setShowFPS(false);
			game.setDisplayMode(800, 600, false);
			game.start();
		}
		catch (SlickException e) {

		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		this.character.draw(100, 100);
		if (this.currentState == State.PLAYING) {
			if (this.scoreBar != null) {
				this.scoreBar.render(container, g);
				g.setColor(new Color(0xFF0000));
				g.drawString("Success: " + this.sucess + "\nFailed: " + this.fail + "\nMissed: " + this.missed, 0, 100);
			}
		}
		else if (this.currentState == State.PICK_FILE) {
			if (!this.switchDirectory) {
				for (AbstractComponent com : this.rendered) {
					com.render(container, g);
					g.drawRect(com.getX(), com.getY(), com.getWidth(), com.getHeight());
					g.drawString(this.paths.get(this.rendered.indexOf(com)).getName(), 25, com.getY());
				}
			}
		}
		else if (this.currentState == State.PICK_MODE) {
			this.rendered.get(0).render(container, g);
			g.drawRect(this.rendered.get(0).getX(), this.rendered.get(0).getY(), this.rendered.get(0).getWidth(), this.rendered.get(0).getHeight());
			g.drawString("Read from jar", 25, 0);
			this.rendered.get(1).render(container, g);
			g.drawRect(this.rendered.get(1).getX(), this.rendered.get(1).getY(), this.rendered.get(1).getWidth(), this.rendered.get(1).getHeight());
			g.drawString("Read from external file", 25, 25);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.currentDirectory = ".";
		this.buttonImage = new Image("res/graphics/testButton.png");
		this.rendered = new LinkedList<AbstractComponent>();
		MouseOverArea area1 = new MouseOverArea(container, this.buttonImage, 0, 0, this);
		MouseOverArea area2 = new MouseOverArea(container, this.buttonImage, 0, 25, this);
		int[] alphas = new int[1];
		alphas[0] = 0xFFFF00FF;
		this.character = GraphicsUtils.makeAnimationWithAlpha(new Image("res/graphics/sprites/char.png"), alphas, 200, 300, 50);
		this.character.stop();
		this.character.setLooping(false);
		area1.setMouseOverColor(new Color(0x44FFFFFF));
		area2.setMouseOverColor(new Color(0x44FFFFFF));
		this.rendered.add(area1);
		this.rendered.add(area2);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input in = container.getInput();
		if (in.isKeyPressed(Input.KEY_ESCAPE)) {
			container.exit();
		}

		if (this.currentState == State.PICK_FILE && this.switchDirectory) {
			this.switchDirectory = false;
			this.rendered = new LinkedList<AbstractComponent>();
			this.paths = new LinkedList<File>();

			this.rendered = new LinkedList<AbstractComponent>();
			File f = new File(this.currentDirectory);
			int height = 0;

			MouseOverArea area = new MouseOverArea(container, this.buttonImage, 0, height, this);
			area.setMouseOverColor(new Color(0x44FFFFFF));
			this.rendered.add(area);
			this.paths.add(new File(this.currentDirectory + "/.."));
			height += 25;
			for (File sub : f.listFiles()) {
				if (sub.isDirectory() || sub.getName().substring(sub.getName().length() - 4).equals(".lfl")) {
					MouseOverArea area2 = new MouseOverArea(container, this.buttonImage, 0, height, this);
					area2.setMouseOverColor(new Color(0x44FFFFFF));
					this.rendered.add(area2);
					this.paths.add(sub);
					height += 25;
				}
			}
		}
		else if (this.currentState == State.PLAYING) {
			if (this.scoreBar == null) {
				LegacyFlowFileParser parser = null;
				try {
					if (this.readLocal) {
						parser = new LegacyFlowFileParser(container, new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/levelFlowFiles/test.lfl"))));
					}
					else {
						parser = new LegacyFlowFileParser(container, new BufferedReader(new FileReader(this.file)));
					}
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					container.exit();
				}

				if (parser != null) {
					this.scoreBar = parser.createFlowComponent(true, 32, (screenWidth - 128)/ 2, 30, 128, screenHeight);
					this.scoreBar.setSpeedMultiplier(4);
					this.scoreBar.addListener(this);
				}
			}
			else {
				this.scoreBar.update();
				if (this.character.getFrame() % 6 == 5 && this.character.isStopped()) {
					this.character.setCurrentFrame(0);
				}
				if (in.isKeyPressed(Input.KEY_ENTER)) {
					if (this.scoreBar.isPlaying()) {
						this.scoreBar.pause();
					}
					else {
						this.scoreBar.play();
					}
				}
				if (in.isKeyPressed(Input.KEY_SPACE)) {
					this.scoreBar.stop();
					this.sucess = 0;
					this.fail = 0;
					this.missed = 0;
				}
			}
		}
	}

	@Override
	public void beatSuccess(int index) {
		switch (index) {
		case 0:
			this.character.setCurrentFrame(0);
			this.character.start();
			this.character.stopAt(5);
			break;
		case 1:
			this.character.setCurrentFrame(6);
			this.character.start();
			this.character.stopAt(11);
			break;
		case 2:
			this.character.setCurrentFrame(12);
			this.character.start();
			this.character.stopAt(17);
			break;
		case 3:
			this.character.setCurrentFrame(18);
			this.character.start();
			this.character.stopAt(23);
			break;
		}
		this.sucess++;
	}

	@Override
	public void beatFailed() {
		this.fail++;
	}

	@Override
	public void beatMissed() {
		this.missed++;
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		if (source instanceof MouseOverArea) {
			if (this.currentState == State.PICK_MODE) {
				if (this.rendered.get(0) == source) {
					this.readLocal = true;
					this.currentState = State.PLAYING;
				}
				else {
					this.currentState = State.PICK_FILE;
					this.switchDirectory = true;
				}
			}
			else if (this.currentState == State.PICK_FILE) {
				if (this.paths.get(this.rendered.indexOf(source)).isDirectory()) {
					this.currentDirectory = this.paths.get(this.rendered.indexOf(source)).getAbsolutePath();
					this.switchDirectory = true;
				}
				else {
					this.file = this.paths.get(this.rendered.indexOf(source)).getAbsolutePath();
					this.currentState = State.PLAYING;
				}
			}
		}
	}
}
