package believe.gui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.core.io.testing.FakeFontLoader;
import believe.gui.CharacterDialogue.DialogueResponse;
import believe.gui.testing.FakeGuiContext;
import believe.gui.testing.FakeImage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.Arrays;
import java.util.Collections;

/** Unit tests for {@link CharacterDialogue}. */
public final class CharacterDialogueTest {
  private GUIContext context;
  private Image image1;
  private Image image2;
  private FakeFontLoader fontLoader;

  @Mock private Graphics graphics;

  @Before
  public void setUp() {
    initMocks(this);
    image1 = new FakeImage(500, 500);
    image2 = new FakeImage(50, 50);
    context = new FakeGuiContext(1000, 1000);
    fontLoader = new FakeFontLoader();
  }

  @Test
  public void render_drawsCurrentImage() throws SlickException {
    CharacterDialogue dialogue =
        new CharacterDialogue(
            context,
            fontLoader,
            Arrays.asList(
                new DialogueResponse(image1, "response 1"),
                new DialogueResponse(image2, "response 2")),
            () -> {});

    dialogue.render(context, graphics);
    dialogue.scroll();
    dialogue.render(context, graphics);
    verify(graphics, times(2)).drawImage(eq(new FakeImage(100, 100)), anyFloat(), anyFloat());
  }

  @Test
  public void render_portraitIsEmpty_imageIsNeverDrawn() throws SlickException {
    CharacterDialogue dialogue =
        new CharacterDialogue(
            context,
            fontLoader,
            Collections.singletonList(
                new DialogueResponse(CharacterDialogue.EMPTY_IMAGE, "response 1")),
            () -> {});

    dialogue.render(context, graphics);
    verify(graphics, never()).drawImage(any(), anyFloat(), anyFloat());
  }
}
