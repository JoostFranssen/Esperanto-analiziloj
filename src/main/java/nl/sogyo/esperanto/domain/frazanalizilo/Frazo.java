package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.Arrays;
import java.util.stream.IntStream;

import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

public class Frazo {
	private Vorto[] vortoj;
	
	public static void main(String[] args) {
		System.out.println(new Frazo("saluton, mi fartas bone."));
	}
	
	public Frazo(String frazo) {
		String[] vortoStrings = frazo.replaceAll("[,\\.!?]", "").split("\\s");
		
		vortoj = new Vorto[vortoStrings.length];
		IntStream.range(0, vortoStrings.length).parallel().forEach(i -> {
			vortoj[i] = new Vorto(vortoStrings[i]);
		});
	}
	
	@Override
	public String toString() {
		return String.join(" ", Arrays.asList(vortoj).stream().map(v -> v.toString()).toArray(String[]::new));
	}
}
