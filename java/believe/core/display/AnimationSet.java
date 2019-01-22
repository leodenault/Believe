package believe.core.display;

import java.util.HashMap;

import org.newdawn.slick.Animation;

public class AnimationSet {
  private HashMap<String, Animation> set;

  public AnimationSet(HashMap<String, Animation> set) {
    this.set = set;
  }

  public Animation get(String name) {
    return set.get(name);
  }

  public AnimationSet copy() {
    HashMap<String, Animation> copy = new HashMap<String, Animation>();
    for (String key : set.keySet()) {
      copy.put(key, set.get(key).copy());
    }
    return new AnimationSet(copy);
  }
}
