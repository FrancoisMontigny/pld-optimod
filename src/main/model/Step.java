package main.model;

import java.util.ArrayList;
import java.util.List;

public class Step {

	private List<Section> sections;
	
	public Step(List<Section> sections) {
		this.sections = new ArrayList<>(sections);
	}
	
	public List<Section> getSections() {
		return sections;
	}
}
