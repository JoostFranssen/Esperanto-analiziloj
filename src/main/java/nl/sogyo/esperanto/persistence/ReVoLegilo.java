package nl.sogyo.esperanto.persistence;

import java.io.IOException;
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
			// TODO Auto-generated catch block
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
		
		if(estasKorelativo(enigo.getVortero())) {
			enigo.setVorterSpeco(VorterSpeco.KORELATIVO);
			return;
		}
		
		if(testiDifinonJeTeksto(ĉefElemento, "prepozicio")) {
			enigo.setVorterSpeco(VorterSpeco.PREPOZICIO);
			return;
		}
		
		if(testiDifinonJeTeksto(ĉefElemento, "konjunkcio")) {
			enigo.setVorterSpeco(VorterSpeco.KONJUNKCIO);
			return;
		}
	}

	private boolean testiDifinonJeTeksto(Element ĉefElemento, String teksto) {
		NodeList vortSpecNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_VORTSPECO);
		if(vortSpecNodoj.getLength() > 0) {
			if(vortSpecNodoj.item(0).getTextContent().equalsIgnoreCase(teksto)) {
				return true;
			}
		}
		
		NodeList difinNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_DIFINO);
		if(difinNodoj.getLength() > 0) {
			String difino = difinNodoj.item(0).getTextContent();
			Pattern ŝablono = Pattern.compile(teksto, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher kongruilo = ŝablono.matcher(difino);
			if(kongruilo.find()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean estasKorelativo(String vorto) {
		
		return vorto.matches("^(k|t|ĉ|nen|)i(aj?n?|en?|on?|uj?n?|al|am|es|om)$");
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