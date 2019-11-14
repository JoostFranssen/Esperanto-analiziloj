package nl.sogyo.esperanto.API;

/**
 * Finaĵoj.
 * @author jfranssen
 *
 */
public enum Finaĵo {
	VERBO_INFINITIVO("i");
	
	private String value;
	private Finaĵo(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
