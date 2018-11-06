package main.model.tsp;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq implements Iterator<Integer> {

	private Integer[] candidates;
	private int numberOfCandidates;

	/**
	 * Create an iterator to iterate on unseenNodes
	 * 
	 * @param unseenNodes
	 * @param sommetCrt
	 */
	public IteratorSeq(Collection<Integer> unseenNodes, int sommetCrt) {
		// FIXME why is sommetCrt here ?
		this.candidates = new Integer[unseenNodes.size()];
		numberOfCandidates = 0;
		for (Integer s : unseenNodes) {
			candidates[numberOfCandidates++] = s;
		}
	}

	@Override
	public boolean hasNext() {
		return numberOfCandidates > 0;
	}

	@Override
	public Integer next() {
		return candidates[--numberOfCandidates];
	}

	@Override
	public void remove() {
	}

}