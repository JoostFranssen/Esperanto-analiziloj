package nl.sogyo.esperanto.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import nl.sogyo.esperanto.API.Finaĵo;
import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Legilo, kiu interpretas XML-dosieron de ReVo kaj kapablas produkti objekton de {@code ReVoEnigo} kun la akiritaj informoj.
 * @author jfranssen
 *
 */
public class ReVoLegilo {
	public static final String ETIKEDO_RADIKO = "rad";
	public static final String ETIKEDO_KAPVORTO = "kap";
	public static final String ETIKEDO_TILDO = "tld"; //~, por ripeto de la vortero
	public static final String ETIKEDO_VORTSPECO = "vspec";
	public static final String ETIKEDO_DERIVAĴO = "drv";
	public static final String ETIKEDO_DIFINO = "dif";
	public static final String ETIKEDO_SUBARTIKOLO = "subart"; //por pluraj difinoj en unu artikoloj
	
	/**
	 * Regula esprimo, kiu reprezentas unu minusklan aŭ majusklan Esperanto-literon.
	 */
	public static final String ESPERANTO_LITERO_REGEX = "[[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ]&&[^qQw-yW-Y]]";
	
	private String dosierPado;
	private ReVoEnigo enigo;
	
	public ReVoLegilo(File dosiero) {
		this(dosiero.getPath());
	}
	public ReVoLegilo(String dosierPado) {
		this.dosierPado = dosierPado;
	}
	
	/**
	 * Kreas aŭ redonas objekton de {@code ReVoEnigo} kun informoj eltiritaj el ReVo-dosiero. Ne garantias, ke kelkaj propraĵoj ne estas {@code null}.
	 * @return {@code ReVoEnigo}n kun informoj laŭeble akireblaj
	 */
	public ReVoEnigo getEnigo() {
		if(enigo == null) {
			enigo = new ReVoEnigo();
			legiDosieron();
			return enigo;
		}
		return enigo;
	}
	
	/**
	 * Legas ReVo-dosieron.
	 */
	private void legiDosieron() {
		DocumentBuilderFactory fabriko = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder konstruilo = fabriko.newDocumentBuilder();
			Document dokumento = konstruilo.parse(dosierPado);
			Element ĉefElemento = dokumento.getDocumentElement();
			ĉefElemento.normalize();
			
			enigo.setVortero(ekstraktiVorteron(ĉefElemento));
			
			determiniVorterSpecon(ĉefElemento);
			
			determiniTransitivecon(ĉefElemento);
		} catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determinas la transitivecon el la ReVo-dosiero.
	 * @param ĉefElemento la ĉefa elemento de la dosiero
	 */
	private void determiniTransitivecon(Element ĉefElemento) {
		NodeList derivaĵoNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_DERIVAĴO);
		for(int i = 0; i < derivaĵoNodoj.getLength(); i++) {
			Element nodo = (Element)derivaĵoNodoj.item(i);
			String[] partoj = disigiVortonĈirkaŭRadiko(nodo);
			if(partoj[0].isEmpty() && partoj[2].equalsIgnoreCase(Finaĵo.VERBO_INFINITIVO.getValoro())) {
				enigo.setTransitiveco(ekstraktiTransitivecon(nodo));
				break;
			}
		}
	}
	
	/**
	 * Determinas la specon de la vortero el la ReVo-dosiero.
	 * @param ĉefElemento la ĉefa elemento de la dosiero
	 */
	private void determiniVorterSpecon(Element ĉefElemento) {
		NodeList kapvortNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_KAPVORTO);
		Element unuaArtikolo;
		if(ĉefElemento.getElementsByTagName(ETIKEDO_SUBARTIKOLO).getLength() > 0) {
			unuaArtikolo = (Element)ĉefElemento.getElementsByTagName(ETIKEDO_SUBARTIKOLO).item(0);
		} else {
			unuaArtikolo = ĉefElemento;
		}
		
		boolean streketoAntaŭRadiko = false; //por determini ĉu estas finaĵo aŭ sufikso
		
		if(kapvortNodoj.getLength() > 0) {
			Element kapvortNodo = (Element)kapvortNodoj.item(0);
			String finaĵo = ekstraktiFinaĵonPostRadiko(kapvortNodo);
			if(!finaĵo.isEmpty()) {
				enigo.setVorterSpeco(VorterSpeco.RADIKO);
				return;
			}
			
			if(estasStreketoPostRadiko(kapvortNodo)) {
				enigo.setVorterSpeco(VorterSpeco.PREFIKSO);
				return;
			}
			
			streketoAntaŭRadiko = estasStreketoAntaŭRadiko(kapvortNodo);
		}
		
		//specifaj kazoj
		String vortero = enigo.getVortero().toLowerCase(Locale.ROOT);
		switch(vortero) {
			case "la":
				enigo.setVorterSpeco(VorterSpeco.ARTIKOLO);
				return;
			case "ajn":
			case "ĉi":
			case "ne":
			case "tuj":
				enigo.setVorterSpeco(VorterSpeco.ADVERBO);
				return;
			case "amen":
				enigo.setVorterSpeco(VorterSpeco.INTERJEKCIO);
				return;
			case "ekzorc":
				enigo.setVorterSpeco(VorterSpeco.RADIKO);
				return;
				
		}
		
		if(estasKorelativo(enigo.getVortero())) {
			enigo.setVorterSpeco(VorterSpeco.KORELATIVO);
			return;
		}
		
		if(estasNumeralo(enigo.getVortero())) {
			enigo.setVorterSpeco(VorterSpeco.NUMERALO);
			return;
		}
		
		String kongruaĵo;
		if(streketoAntaŭRadiko) {
			kongruaĵo = testiDifinonJeTeksto(unuaArtikolo, "finaĵo", "sufikso");
		} else {
    		kongruaĵo = testiDifinonJeTeksto(unuaArtikolo,
    				"finaĵo",
    				"pronomo",
    				"prepozicio", "prep\\.",
    				"konjunkcio", "konj\\.",
    				"sufikso",
    				"prefikso",
    				"litero",
    				"ekkrio", "interjekcio", "interj\\.",
    				"adverbo",
    				"sonimito");
		}
		
		if(kongruaĵo != null) {
			kongruaĵo = kongruaĵo.toLowerCase(Locale.ROOT);
			switch(kongruaĵo) {
				case "prep.":
					kongruaĵo = "prepozicio";
					break;
				case "konj.":
					kongruaĵo = "konjunkcio";
					break;
				case "ekkrio":
				case "interj.":
					kongruaĵo = "interjekcio";
					break;
			}
			
			kongruaĵo = kongruaĵo.toUpperCase(Locale.ROOT);
			try {
				enigo.setVorterSpeco(VorterSpeco.valueOf(kongruaĵo));
				return;
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
		if(enigo.getVortero().endsWith("aŭ")) { //restantaj aŭvortoj
			enigo.setVorterSpeco(VorterSpeco.ADVERBO);
			return;
		}
	}
	
	/**
	 * Testas la vortero de {@code enigo} je ĉiuj provizitaj tekstoj en la difino de la ReVo-dosiero.
	 * @param ĉefElemento la ĉefa elemento de la ReVo-dosiero
	 * @param teksto teksto, kun kiu {@code enigo.getVortero()} devas kongrui
	 * @return la kongruintan altiraĵon el la difino en la ReVo-dosiero
	 */
	private String testiDifinonJeTeksto(Element ĉefElemento, String... teksto) {
		String regexŜablonoElTeksto = String.format("(%s)", "|%s".repeat(teksto.length).substring(1));
		String regexElTeksto = String.format(regexŜablonoElTeksto, (Object[])teksto);
		
		Pattern ŝablono = Pattern.compile(regexElTeksto, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		
		NodeList vortSpecNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_VORTSPECO);
		if(vortSpecNodoj.getLength() > 0) {
			Matcher kongruilo = ŝablono.matcher(vortSpecNodoj.item(0).getTextContent());
			
			if(kongruilo.matches()) {
				return kongruilo.group();
			}
		}
		
		NodeList difinNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_DIFINO);
		if(difinNodoj.getLength() > 0) {
			String difino = difinNodoj.item(0).getTextContent();
			Matcher kongruilo = ŝablono.matcher(difino);
			if(kongruilo.find()) {
				return kongruilo.group();
			}
		}
		return null;
	}
	
	/**
	 * Kontrolas, ĉu vorto estas korelativo. Inkluzivas kelkaj ali-vortoj, kiuj ne konfliktas kun ali/.
	 * @param vorto
	 * @return ĉu vorto estas korelativo aŭ ne
	 */
	private boolean estasKorelativo(String vorto) {
		return vorto.matches("^(k|t|ĉ|nen|)i(a|e|o|u|al|am|el|es|om)$") || vorto.matches("^ali(u|al|am|el|es|om)$");
	}
	
	/**
	 * Kontrolas, ĉu vorta estas numeralo.
	 * @param vorto
	 * @return ĉu vorto estas numeralo
	 */
	private boolean estasNumeralo(String vorto) {
		return vorto.matches("^(unu|du|tri|kvar|kvin|ses|sep|ok|naŭ|dek|cent|mil)$");
	}
	
	/**
	 * Eltiras la vorteron el la ReVo-dosiero.
	 * @param ĉefElemento la ĉefa elemento de la ReVo-dosiero
	 * @return la vorteron trovitan
	 */
	private String ekstraktiVorteron(Element ĉefElemento) {
		NodeList nodoj = ĉefElemento.getElementsByTagName(ETIKEDO_RADIKO);
		if(nodoj.getLength() > 0) {
			return nodoj.item(0).getTextContent();
		}
		return "";
	}
	
	/**
	 * Eltiras la finaĵon skribitan post la radiko en la ReVo-dosiero.
	 * @param kapvortNodo la kapvortnodo, en kiu staras la radiko
	 * @return la finaĵon
	 */
	private String ekstraktiFinaĵonPostRadiko(Element kapvortNodo) {
		String enhavo = kapvortNodo.getTextContent();
		
		Pattern ŝablono = Pattern.compile(String.format("(%s)/(\\S*)", enigo.getVortero()));
		Matcher kongruilo = ŝablono.matcher(enhavo);
		if(kongruilo.find()) {
			return kongruilo.group(2);
		} else {
			return "";
		}
	}
	
	/**
	 * Kontrolas, ĉu estas streketo (-) antaŭ la radiko. Utile por determini, ĉu vortero estas sufikso aŭ finaĵo.
	 * @param kapvortNodo la kapvortnodo, kiu entenas la radikon
	 * @return ĉu estas streketo antaŭ la radiko
	 */
	private boolean estasStreketoAntaŭRadiko(Element kapvortNodo) {
		String enhavo = kapvortNodo.getTextContent();
		Pattern ŝablono = Pattern.compile(String.format("-(%s)", enigo.getVortero()));
		Matcher kongruilo = ŝablono.matcher(enhavo);
		return kongruilo.find();
	}
	
	/**
	 * Kontrolas, ĉu estas streketo (-) post la radiko. Utile port determini, ĉu vortero estas prefikso.
	 * @param kapvortNodo la kapvortnodo, kiu entenas la radikon
	 * @return ĉu estas streketo post la radiko
	 */
	private boolean estasStreketoPostRadiko(Element kapvortNodo) {
		String enhavo = kapvortNodo.getTextContent();
		Pattern ŝablono = Pattern.compile(String.format("(%s)-", enigo.getVortero()));
		Matcher kongruilo = ŝablono.matcher(enhavo);
		return kongruilo.find();
	}

	/**
	 * Disigas vorton ĉirkaŭ la radiko por determini la derivaĵon. Ekzemple sub 'sci/i' povas esti enigo 'multescia'. Ĉi tio disigas ĝin al ["multe", "sci", "a"].
	 * @param kapvortNodo la kapvortnodo, kiu entenas la radikon
	 * @return disigon de la partoj rekte antaŭ kaj post la radiko, kaj en la mezo la radikon
	 */
	private String[] disigiVortonĈirkaŭRadiko(Element kapvortNodo) {
		String[] partoj = {"", "", ""};
		
		NodeList nodoj = kapvortNodo.getElementsByTagName(ETIKEDO_TILDO);
		if(nodoj.getLength() > 0) {
			Node radikNodo = nodoj.item(0);
			if(radikNodo.getPreviousSibling() != null) {
				String antaŭEnhavo = radikNodo.getPreviousSibling().getTextContent().trim();
				Pattern ŝablono = Pattern.compile("\\b" + ESPERANTO_LITERO_REGEX + "*$");
				Matcher kongruilo = ŝablono.matcher(antaŭEnhavo);
				if(kongruilo.lookingAt()) {
					partoj[0] = kongruilo.group();
				}
			}
			if(radikNodo.getNextSibling() != null) {
				String postEnhavo = radikNodo.getNextSibling().getTextContent().trim();
				Pattern ŝablono = Pattern.compile("^" + ESPERANTO_LITERO_REGEX + "*\\b");
				Matcher kongruilo = ŝablono.matcher(postEnhavo);
				if(kongruilo.lookingAt()) {
					partoj[2] = kongruilo.group();
				}
			}
			partoj[1] = enigo.getVortero();
		}
		return partoj;
	}
	
	/**
	 * Eltiras la transitivecon de derivaĵnodo. Ekzemple, por la substantiva radiko 'aŭt/o' ĝi eltiras la transitivecon de la derivaĵo 'aŭt/i'.
	 * @param derivaĵNodo la nodo, kiu entenas la derivaĵon en la ReVo-dosiero
	 * @return la Transitivecon
	 */
	private Transitiveco ekstraktiTransitivecon(Element derivaĵNodo) {
		NodeList nodoj = derivaĵNodo.getElementsByTagName(ETIKEDO_VORTSPECO);
		boolean transitiva = false;
		boolean netransitiva = false;
		
		for(int i = 0; i < nodoj.getLength(); i++) {
			String enhavo = nodoj.item(i).getTextContent();
			switch(enhavo) {
				case "tr":
					transitiva = true;
					break;
				case "ntr":
					netransitiva = true;
					break;
				case "x":
					transitiva = true;
					netransitiva = true;
					break;
			}
		}
		
		if(transitiva) {
			if(netransitiva) {
				return Transitiveco.AMBAŬ;
			} else {
				return Transitiveco.TRANSITIVA;
			}
		} else {
			if(netransitiva) {
				return Transitiveco.NETRANSITIVA;
			} else {
				return Transitiveco.NEDIFINITA;
			}
		}
	}
}