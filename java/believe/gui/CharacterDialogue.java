package believe.gui;

import believe.command.Command;
import believe.core.io.FontLoader;
import believe.gui.TextComponent.HorizontalTextAlignment;
import believe.gui.TextComponent.VerticalTextAlignment;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import javax.annotation.Nullable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@AutoFactory
public final class CharacterDialogue extends ComponentBase {
  public static final Image EMPTY_IMAGE = new Image() {};

  public static final class DialogueResponse {
    final Image portrait;
    final String text;

    public DialogueResponse(Image portrait, String text) {
      this.portrait = portrait;
      this.text = text;
    }
  }

  private static final float DIALOGUE_HEIGHT_PERCENT = 0.1f;

  private final TextComponent textComponent;
  private final Iterator<DialogueResponse> dialogueResponseIterator;
  @Nullable private final Command followupCommand;

  private Image currentPortrait;

  public CharacterDialogue(
      @Provided GUIContext container,
      @Provided FontLoader fontLoader,
      List<DialogueResponse> dialogueResponses,
      @Nullable Command followupCommand) {
    super(container);
    dialogueResponseIterator = dialogueResponses.iterator();
    this.followupCommand = followupCommand;
    currentPortrait = EMPTY_IMAGE;
    textComponent = new TextComponent(container, fontLoader.getBaseFont(), "");
    textComponent.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    textComponent.setVerticalTextAlignment(VerticalTextAlignment.TOP);

    if (dialogueResponseIterator.hasNext()) {
      setUpDialogueResponse(dialogueResponseIterator.next());
    }
  }

  @Override
  public void resetLayout() {}

  @Override
  protected void renderComponent(GUIContext context, Graphics graphics) throws SlickException {
    if (currentPortrait != EMPTY_IMAGE) {
      int portraitY = context.getHeight() - currentPortrait.getHeight();
      graphics.setColor(Color.black);
      graphics.fillRect(0, portraitY, currentPortrait.getWidth(), currentPortrait.getHeight());
      graphics.drawImage(currentPortrait, 0, portraitY);
    }
    textComponent.render(context, graphics);
  }

  public Optional<Command> getFollupCommand() {
    return Optional.ofNullable(followupCommand);
  }

  /**
   * Returns true if the dialogue scrolled due to having more text to display. Otherwise returns
   * false.
   */
  public boolean scroll() {
    if (textComponent.scroll()) {
      return true;
    }

    if (dialogueResponseIterator.hasNext()) {
      setUpDialogueResponse(dialogueResponseIterator.next());
      return true;
    }

    return false;
  }

  private void setUpDialogueResponse(DialogueResponse dialogueResponse) {
    Image originalPortrait = dialogueResponse.portrait;
    int preTextWidth;

    if (originalPortrait == EMPTY_IMAGE) {
      preTextWidth = 0;
      currentPortrait = EMPTY_IMAGE;
    } else {
      float yFillPercent = ((float) originalPortrait.getHeight()) / container.getHeight();
      currentPortrait = originalPortrait.getScaledCopy(DIALOGUE_HEIGHT_PERCENT / yFillPercent);
      preTextWidth = currentPortrait.getWidth();
    }
    int textY = (int) (container.getHeight() * (1 - DIALOGUE_HEIGHT_PERCENT));

    textComponent.setLocation(preTextWidth, textY);
    textComponent.setWidth(container.getWidth() - preTextWidth);
    textComponent.setHeight(container.getHeight() - textY);
    textComponent.setText(dialogueResponse.text);
  }
}
