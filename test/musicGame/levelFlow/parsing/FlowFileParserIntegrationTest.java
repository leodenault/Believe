package musicGame.levelFlow.parsing;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.newdawn.slick.SlickException;

import musicGame.levelFlow.parsing.FlowComponentBuilder.BeatData;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

@RunWith(Parameterized.class)
public class FlowFileParserIntegrationTest {
  private static final String TEST_LFL =
      "Song = somesong.ogg " +
      "Keys = a s k l " +
      "Tempo = 120 " +
      "Offset = 123 " +
      "SubdivisionImages = sub1 sub2 sub3 " +

      "BEGIN " +
      "x--- " +
      "---- " +
      "---- " +
      "---- " +
      "x--- " +
      "---- " +
      "---- " +
      "---- " +
      "END";
  private static final String TEST_LFL_WITH_BLANK_LINES =
      "Song = somesong.ogg\n" +
      "Keys = a s k l\n" +
      "Tempo = 120\n" +
      "Offset = 123\n" +
      "SubdivisionImages = sub1 sub2 sub3\n" +
      "\n" +
      "BEGIN\n" +
      "x---\n" +
      "----\n" +
      "----\n" +
      "----\n" +
      "\n" +
      "x---\n" +
      "----\n" +
      "----\n" +
      "----\n" +
      "END";

  private FlowFileParser parser;
  private FlowComponentBuilder builder;

  @Parameters
  public static Collection<String[]> parameters() {
    return Arrays.asList(new String[][] {
        {TEST_LFL},
        {TEST_LFL_WITH_BLANK_LINES}
    });
  }

  public FlowFileParserIntegrationTest(String lfl) {
    System.out.println(lfl);
    builder = new FlowComponentBuilder(null, 0);
    parser = new FlowFileParser(new StringReader(lfl), builder);
  }

  @Test
  public void testBeatTimingsAreCorrectWithoutBlankLines()
      throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException {
    parser.parse();
    List<BeatData>[] beats = builder.getBeats();
    assertThat(beats[0].get(0).getPosition(), is(0));
    assertThat(beats[0].get(1).getPosition(), is(4));
  }
}
