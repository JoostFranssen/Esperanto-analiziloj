package nl.sogyo.esperanto.API;

/**
 * Finaĵoj.
 * @author jfranssen
 *
 */
public enum Finaĵo {
	VERBO_INFINITIVO("i");
	
	private String valoro;
	private Finaĵo(String valoro) {
		this.valoro = valoro;
	}
	
	public String getValoro() {
		return valoro;
	}
}
