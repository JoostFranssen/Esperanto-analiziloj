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
import org.xml.sax.helpers.DefaultHandler;

import nl.sogyo.esperanto.API.Finaĵo;
import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

public class ReVoLegilo extends DefaultHandler {
	public static final String ETIKEDO_RADIKO = "rad";
	public static final String ETIKEDO_KAPVORTO = "kap";
	public static final String ETIKEDO_TILDO = "tld"; //por ripeto de la vortero
	public static final String ETIKEDO_VORTSPECO = "vspec";
	public static final String ETIKEDO_DERIVAĴO = "drv";
	public static final String ETIKEDO_DIFINO = "dif";
	
	public static final String ESPERANTO_LITERO_REGEX = "[[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ]&&[^qQw-yW-Y]]";
	
	private String dosierPado;
	private ReVoEnigo enigo;
	
	public ReVoLegilo(File dosiero) {
		this(dosiero.getPath());
	}
	public ReVoLegilo(String dosierPado) {
		this.dosierPado = dosierPado;
	}
	
	public ReVoEnigo getEnigo() {
		if(enigo == null) {
			enigo = new ReVoEnigo();
			legiDosieron();
			return enigo;
		}
		return enigo;
	}
	
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

	private void determiniVorterSpecon(Element ĉefElemento) {
		NodeList kapvortNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_KAPVORTO);
		
		if(kapvortNodoj.getLength() > 0) {
			String finaĵo = ekstraktiFinaĵonPostRadiko((Element)kapvortNodoj.item(0));
			if(!finaĵo.isEmpty()) {
				enigo.setVorterSpeco(VorterSpeco.RADIKO);
				return;
			}
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
		if(enigo.getVortero().equalsIgnoreCase("la")) {
			
		}
		
		if(estasKorelativo(enigo.getVortero())) {
			enigo.setVorterSpeco(VorterSpeco.KORELATIVO);
			return;
		}
		
		if(estasNumeralo(enigo.getVortero())) {
			enigo.setVorterSpeco(VorterSpeco.NUMERALO);
			return;
		}
		
		String kongruaĵo = testiDifinonJeTeksto(ĉefElemento,
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
	
	private boolean estasKorelativo(String vorto) {
		return vorto.matches("^(k|t|ĉ|nen|)i(aj?n?|en?|on?|uj?n?|al|am|el|es|om)$");
	}
	
	private boolean estasNumeralo(String vorto) {
		return vorto.matches("^(unu|du|tri|kvar|kvin|ses|sep|ok|naŭ|dek|cent|mil)$");
	}
	
	private String ekstraktiVorteron(Element ĉefElemento) {
		NodeList nodoj = ĉefElemento.getElementsByTagName(ETIKEDO_RADIKO);
		if(nodoj.getLength() > 0) {
			return nodoj.item(0).getTextContent();
		}
		return "";
	}
	
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