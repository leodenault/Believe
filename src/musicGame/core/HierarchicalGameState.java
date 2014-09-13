package musicGame.core;

import java.util.LinkedList;
import java.util.List;



/**
 * Used to model hierarchical states in a finite state machine.
 * This allows to add sub states.
 */
public abstract class HierarchicalGameState extends GameStateBase implements SubGameState {
	
	protected HierarchicalGameState defaultState;
	protected List<HierarchicalGameState> subStates;
	
	public HierarchicalGameState() {
		this(NO_XML_FILE);
	}
	
	public HierarchicalGameState(String niftyXmlFile) {
		super(niftyXmlFile);
		this.subStates = new LinkedList<HierarchicalGameState>();
	}

	public void setDefaultState(HierarchicalGameState state) {
		this.defaultState = state;
		addState(state);
	}
	
	public void addState(HierarchicalGameState state) {
		this.subStates.add(state);
	}
}
