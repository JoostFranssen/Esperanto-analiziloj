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
public class ReVoReader {
	public static final String TAG_RADIKO = "rad";
	public static final String TAG_KAPVORTO = "kap";
	public static final String TAG_TILDO = "tld"; //~, por ripeto de la vortero
	public static final String TAG_VORTSPECO = "vspec";
	public static final String TAG_DERIVAĴO = "drv";
	public static final String TAG_DIFINO = "dif";
	public static final String TAG_SUBARTIKOLO = "subart"; //por pluraj difinoj en unu artikoloj
	
	private static final Pattern KORELATIVO_REGEX_PATTERN = Pattern.compile("^(k|t|ĉ|nen|)i(a|e|o|u|al|am|el|es|om)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern KORELATIVO_REGEX_PATTERN_OTHER = Pattern.compile("^ali(u|al|am|el|es|om)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern NUMERALO_REGEX_PATTERN = Pattern.compile("^(unu|du|tri|kvar|kvin|ses|sep|ok|naŭ|dek|cent|mil)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/**
	 * Regula esprimo, kiu reprezentas unu minusklan aŭ majusklan Esperanto-literon.
	 */
	public static final String ESPERANTO_LETTER_REGEX = "[[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ]&&[^qQw-yW-Y]]";
	
	private String filePath;
	private ReVoEntry entry;
	
	public ReVoReader(File file) {
		this(file.getPath());
	}
	public ReVoReader(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Kreas aŭ redonas objekton de {@code ReVoEntry} kun informoj eltiritaj el ReVo-dosiero. Ne garantias, ke kelkaj propraĵoj ne estas {@code null}.
	 * @return {@code ReVoEntry} kun informoj laŭeble akireblaj
	 */
	public ReVoEntry getEnigo() {
		if(entry == null) {
			entry = new ReVoEntry();
			readFile();
			return entry;
		}
		return entry;
	}
	
	/**
	 * Legas ReVo-dosieron.
	 */
	private void readFile() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(filePath);
			Element mainElement = document.getDocumentElement();
			mainElement.normalize();
			
			entry.setVortero(extractVortero(mainElement));
			
			determineVorterSpeco(mainElement);
			
			determineTransitiveco(mainElement);
		} catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determinas la transitivecon el la ReVo-dosiero.
	 * @param mainElement la ĉefa elemento de la dosiero
	 */
	private void determineTransitiveco(Element mainElement) {
		NodeList derivaĵoNodes = mainElement.getElementsByTagName(TAG_DERIVAĴO);
		for(int i = 0; i < derivaĵoNodes.getLength(); i++) {
			Element node = (Element)derivaĵoNodes.item(i);
			String[] parts = splitVortoAroundRadiko(node);
			if(parts[0].isEmpty() && parts[2].equalsIgnoreCase(Finaĵo.VERBO_INFINITIVO.getValue())) {
				entry.setTransitiveco(extractTransitiveco(node));
				break;
			}
		}
	}
	
	/**
	 * Determinas la specon de la vortero el la ReVo-dosiero.
	 * @param mainElement la ĉefa elemento de la dosiero
	 */
	private void determineVorterSpeco(Element mainElement) {
		NodeList kapvortNodes = mainElement.getElementsByTagName(TAG_KAPVORTO);
		Element firstArtikolo;
		if(mainElement.getElementsByTagName(TAG_SUBARTIKOLO).getLength() > 0) {
			firstArtikolo = (Element)mainElement.getElementsByTagName(TAG_SUBARTIKOLO).item(0);
		} else {
			firstArtikolo = mainElement;
		}
		
		boolean dashBeforeRadiko = false; //por determini ĉu estas finaĵo aŭ sufikso
		
		if(kapvortNodes.getLength() > 0) {
			Element kapvortNode = (Element)kapvortNodes.item(0);
			String finaĵo = extractFinaĵoAfterRadiko(kapvortNode);
			if(!finaĵo.isEmpty()) {
				entry.setVorterSpeco(VorterSpeco.RADIKO);
				return;
			}
			
			if(isDashAfterRadiko(kapvortNode)) {
				entry.setVorterSpeco(VorterSpeco.PREFIKSO);
				return;
			}
			
			dashBeforeRadiko = isDashBeforeRadiko(kapvortNode);
		}
		
		//specifaj kazoj
		String vortero = entry.getVortero().toLowerCase(Locale.ROOT);
		switch(vortero) {
			case "la":
				entry.setVorterSpeco(VorterSpeco.ARTIKOLO);
				return;
			case "ajn":
			case "ĉi":
			case "ne":
			case "tuj":
				entry.setVorterSpeco(VorterSpeco.ADVERBO);
				return;
			case "amen":
				entry.setVorterSpeco(VorterSpeco.INTERJEKCIO);
				return;
			case "ekzorc":
				entry.setVorterSpeco(VorterSpeco.RADIKO);
				return;
		}
		
		if(isKorelativo(entry.getVortero())) {
			entry.setVorterSpeco(VorterSpeco.KORELATIVO);
			return;
		}
		
		if(isNumeralo(entry.getVortero())) {
			entry.setVorterSpeco(VorterSpeco.NUMERALO);
			return;
		}
		
		String match;
		if(dashBeforeRadiko) {
			match = testDefinitionByText(firstArtikolo, "finaĵo", "sufikso");
		} else {
    		match = testDefinitionByText(firstArtikolo,
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
		
		if(match != null) {
			match = match.toLowerCase(Locale.ROOT);
			switch(match) {
				case "prep.":
					match = "prepozicio";
					break;
				case "konj.":
					match = "konjunkcio";
					break;
				case "ekkrio":
				case "interj.":
					match = "interjekcio";
					break;
			}
			
			match = match.toUpperCase(Locale.ROOT);
			try {
				entry.setVorterSpeco(VorterSpeco.valueOf(match));
				return;
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
		if(entry.getVortero().endsWith("aŭ")) { //restantaj aŭvortoj
			entry.setVorterSpeco(VorterSpeco.ADVERBO);
			return;
		}
	}
	
	/**
	 * Testas la vortero de {@code enigo} je ĉiuj provizitaj tekstoj en la difino de la ReVo-dosiero.
	 * @param mainElement la ĉefa elemento de la ReVo-dosiero
	 * @param textParts teksto, kun kiu {@code enigo.getVortero()} devas kongrui
	 * @return la kongruintan altiraĵon el la difino en la ReVo-dosiero
	 */
	private String testDefinitionByText(Element mainElement, String... textParts) {
		String regexPatternFromText = String.format("(%s)", "|%s".repeat(textParts.length).substring(1));
		String regexFromText = String.format(regexPatternFromText, (Object[])textParts);
		
		Pattern pattern = Pattern.compile(regexFromText, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		
		NodeList vortSpecNodes = mainElement.getElementsByTagName(TAG_VORTSPECO);
		if(vortSpecNodes.getLength() > 0) {
			Matcher matcher = pattern.matcher(vortSpecNodes.item(0).getTextContent());
			
			if(matcher.matches()) {
				return matcher.group();
			}
		}
		
		NodeList difinNodes = mainElement.getElementsByTagName(TAG_DIFINO);
		if(difinNodes.getLength() > 0) {
			String definition = difinNodes.item(0).getTextContent();
			Matcher matcher = pattern.matcher(definition);
			if(matcher.find()) {
				return matcher.group();
			}
		}
		return null;
	}
	
	/**
	 * Kontrolas, ĉu vorto estas korelativo. Inkluzivas kelkaj ali-vortoj, kiuj ne konfliktas kun ali/.
	 * @param vorto
	 * @return ĉu vorto estas korelativo aŭ ne
	 */
	private boolean isKorelativo(String vorto) {
		Matcher matcher = KORELATIVO_REGEX_PATTERN.matcher(vorto);
		if(matcher.matches()) {
			return true;
		} else {
			return (KORELATIVO_REGEX_PATTERN_OTHER.matcher(vorto)).matches();
		}
	}
	
	/**
	 * Kontrolas, ĉu vorta estas numeralo.
	 * @param vorto
	 * @return ĉu vorto estas numeralo
	 */
	private boolean isNumeralo(String vorto) {
		return (NUMERALO_REGEX_PATTERN.matcher(vorto)).matches();
	}
	
	/**
	 * Eltiras la vorteron el la ReVo-dosiero.
	 * @param mainElement la ĉefa elemento de la ReVo-dosiero
	 * @return la vorteron trovitan
	 */
	private String extractVortero(Element mainElement) {
		NodeList nodes = mainElement.getElementsByTagName(TAG_RADIKO);
		if(nodes.getLength() > 0) {
			return nodes.item(0).getTextContent();
		}
		return "";
	}
	
	/**
	 * Eltiras la finaĵon skribitan post la radiko en la ReVo-dosiero.
	 * @param kapvortNode la kapvortnodo, en kiu staras la radiko
	 * @return la finaĵon
	 */
	private String extractFinaĵoAfterRadiko(Element kapvortNode) {
		String contents = kapvortNode.getTextContent();
		Pattern pattern = Pattern.compile(String.format("(%s)/(\\S*)", entry.getVortero()));
		Matcher matcher = pattern.matcher(contents);
		if(matcher.find()) {
			return matcher.group(2);
		} else {
			return "";
		}
	}
	
	/**
	 * Kontrolas, ĉu estas streketo (-) antaŭ la radiko. Utile por determini, ĉu vortero estas sufikso aŭ finaĵo.
	 * @param kapvortNode la kapvortnodo, kiu entenas la radikon
	 * @return ĉu estas streketo antaŭ la radiko
	 */
	private boolean isDashBeforeRadiko(Element kapvortNode) {
		String contents = kapvortNode.getTextContent();
		Pattern pattern = Pattern.compile(String.format("-(%s)", entry.getVortero()));
		Matcher matcher = pattern.matcher(contents);
		return matcher.find();
	}
	
	/**
	 * Kontrolas, ĉu estas streketo (-) post la radiko. Utile port determini, ĉu vortero estas prefikso.
	 * @param kapvortNode la kapvortnodo, kiu entenas la radikon
	 * @return ĉu estas streketo post la radiko
	 */
	private boolean isDashAfterRadiko(Element kapvortNode) {
		String contents = kapvortNode.getTextContent();
		Pattern pattern = Pattern.compile(String.format("(%s)-", entry.getVortero()));
		Matcher matcher = pattern.matcher(contents);
		return matcher.find();
	}

	/**
	 * Disigas vorton ĉirkaŭ la radiko por determini la derivaĵon. Ekzemple sub 'sci/i' povas esti enigo 'multescia'. Ĉi tio disigas ĝin al ["multe", "sci", "a"].
	 * @param kapvortNode la kapvortnodo, kiu entenas la radikon
	 * @return disigon de la partoj rekte antaŭ kaj post la radiko, kaj en la mezo la radikon
	 */
	private String[] splitVortoAroundRadiko(Element kapvortNode) {
		String[] parts = {"", "", ""};
		
		NodeList nodes = kapvortNode.getElementsByTagName(TAG_TILDO);
		if(nodes.getLength() > 0) {
			Node radikNode = nodes.item(0);
			if(radikNode.getPreviousSibling() != null) {
				String beforeContents = radikNode.getPreviousSibling().getTextContent().trim();
				Pattern pattern = Pattern.compile("\\b" + ESPERANTO_LETTER_REGEX + "*$");
				Matcher matcher = pattern.matcher(beforeContents);
				if(matcher.lookingAt()) {
					parts[0] = matcher.group();
				}
			}
			if(radikNode.getNextSibling() != null) {
				String afterContents = radikNode.getNextSibling().getTextContent().trim();
				Pattern pattern = Pattern.compile("^" + ESPERANTO_LETTER_REGEX + "*\\b");
				Matcher matcher = pattern.matcher(afterContents);
				if(matcher.lookingAt()) {
					parts[2] = matcher.group();
				}
			}
			parts[1] = entry.getVortero();
		}
		return parts;
	}
	
	/**
	 * Eltiras la transitivecon de derivaĵnodo. Ekzemple, por la substantiva radiko 'aŭt/o' ĝi eltiras la transitivecon de la derivaĵo 'aŭt/i'.
	 * @param derivaĵNode la nodo, kiu entenas la derivaĵon en la ReVo-dosiero
	 * @return la Transitivecon
	 */
	private Transitiveco extractTransitiveco(Element derivaĵNode) {
		NodeList nodes = derivaĵNode.getElementsByTagName(TAG_VORTSPECO);
		boolean transitiva = false;
		boolean netransitiva = false;
		
		for(int i = 0; i < nodes.getLength(); i++) {
			String contents = nodes.item(i).getTextContent();
			switch(contents) {
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