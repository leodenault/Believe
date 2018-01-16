package musicGame.xml;

import java.util.LinkedList;
import java.util.List;

public class CompoundDef extends ChildDef {
  private List<ChildDef> subChildDefs;

  public CompoundDef(String name) {
    super(name);
    this.subChildDefs = new LinkedList<ChildDef>();
  }

  public CompoundDef addSubChildDef(ChildDef def) {
    this.subChildDefs.add(def);
    return this;
  }

  public CompoundDef addString(String name) {
    this.subChildDefs.add(new StringDef(name));
    return this;
  }

  public CompoundDef addInteger(String name) {
    this.subChildDefs.add(new IntegerDef(name));
    return this;
  }

  public CompoundDef addBoolean(String name) {
    this.subChildDefs.add(new BooleanDef(name));
    return this;
  }

  public CompoundDef addList(String name, CompoundDef subChildDef) {
    this.subChildDefs.add(new ListDef(name, subChildDef));
    return this;
  }

  public List<ChildDef> getSubChildDefs() {
    return subChildDefs;
  }
}
