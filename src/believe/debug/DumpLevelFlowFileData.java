package believe.debug;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

import believe.levelFlow.parsing.FlowComponentBuilder;
import believe.levelFlow.parsing.FlowComponentBuilder.BeatData;
import believe.levelFlow.parsing.FlowFileParser;
import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;

public class DumpLevelFlowFileData {
  private static final int SEPARATORS = 16;

  public static void main(String[] args)
      throws FlowComponentBuilderException, SlickException, IOException, FlowFileParserException {
    if (args.length != 1) {
      System.out.println("Expecting relative file path to flow file that will be dumped.");
      return;
    }

    String fileName = args[0];
    FlowComponentBuilder builder = new FlowComponentBuilder(null, 0);
    FlowFileParser parser = new FlowFileParser(new InputStreamReader(ResourceLoader.getResourceAsStream(fileName)), builder);
    parser.parse();

    dumpData(builder.getInputKeys(), builder.getBeats(), builder.getBpm(), builder.getOffset(), builder.getSubdivisions());
  }

  private static void dumpData(char[] inputKeys, List<BeatData>[] beats, int bpm, int offset, int subdivisions) {
    if (beats == null || beats.length == 0) {
      System.out.println("Input keys was not set, therefore there is no data in the list of beats.");
    } else {
      for (char key : inputKeys) {
        System.out.print(key + generateSpaces(1, SEPARATORS));
      }
      System.out.println();

      int maxLines = 0;
      for (int i = 0; i < inputKeys.length; i++) {
        maxLines = Math.max(maxLines, beats[i].size());
      }

      StringBuilder[] lines = new StringBuilder[maxLines];
      for (int i = 0; i < maxLines; i++) {
        lines[i] = new StringBuilder();
      }

      for (int i = 0; i < beats.length; i++) {
        for (int j = 0; j < beats[i].size(); j++) {
          BeatData data = beats[i].get(j);
          StringBuilder line = lines[j];
          StringBuilder cell = new StringBuilder();
          cell.append(data.getPosition());
          cell.append("/");
          cell.append(String.format("%.3f", data.getPosition() * ((60.0/bpm) / subdivisions)));
          generateSpaces(cell, cell.length(), SEPARATORS);
          line.append(cell.toString());
        }
      }

      for (StringBuilder line : lines) {
        System.out.println(line.toString());
      }
    }
  }

  private static String generateSpaces(int used, int total) {
    return generateSpaces(new StringBuilder(), used, total);
  }

  private static String generateSpaces(StringBuilder builder, int used, int total) {
    for (int i = 0; i < total - used; i++) {
      builder.append(" ");
    }
    return builder.toString();
  }
}
