package believe.gui;

import believe.gui.TextComponent.HorizontalTextAlignment;
import believe.gui.TextComponent.VerticalTextAlignment;
import javax.annotation.Nullable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public final class CharacterDialogue extends ComponentBase {
  private static final float DIALOGUE_HEIGHT_PERCENT = 0.1f;

  @Nullable private final Image characterProfile;
  private final TextComponent text;

  public CharacterDialogue(
      GUIContext container, Font font, String text, boolean hasProfile) throws SlickException {
    super(container);

    int preTextWidth = 0;
    if (hasProfile) {
      Image originalProfile = new Image("/res/graphics/sprites/testProfile.png");
      float yFillPercent = ((float) originalProfile.getHeight()) / container.getHeight();
      characterProfile = originalProfile.getScaledCopy(DIALOGUE_HEIGHT_PERCENT / yFillPercent);
      preTextWidth = characterProfile.getWidth();
    } else {
      characterProfile = null;
    }
    int textY = (int) (container.getHeight() * (1 - DIALOGUE_HEIGHT_PERCENT));
    this.text = new TextComponent(
        container,
        font,
        preTextWidth,
        textY,
        container.getWidth() - preTextWidth,
        container.getHeight() - textY,
        text);
    this.text.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    this.text.setVerticalTextAlignment(VerticalTextAlignment.TOP);
  }

  @Override
  public void resetLayout() {

  }

  @Override
  protected void renderComponent(
      GUIContext context, Graphics g) throws SlickException {
    if (characterProfile != null) {
      int profileY = context.getHeight() - characterProfile.getHeight();
      g.setColor(Color.black);
      g.fillRect(0, profileY, characterProfile.getWidth(), context.getHeight());
      g.drawImage(characterProfile, 0, profileY);
    }
    text.render(context, g);
  }

  @Override
  public void setWidth(int width) {
    super.setWidth(width);
    text.setWidth(width);
  }

  public void scroll() {
    text.scroll();
  }
}
