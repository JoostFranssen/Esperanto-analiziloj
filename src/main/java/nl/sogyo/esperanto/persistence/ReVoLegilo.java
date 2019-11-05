package nl.sogyo.esperanto.persistence;

import java.io.IOException;
import java.util.Arrays;
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
			
			NodeList kapvortNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_KAPVORTO);
			
			if(kapvortNodoj.getLength() > 0) {
				String finaĵo = ekstraktiFinaĵonPostRadiko((Element)kapvortNodoj.item(0));
				if(!finaĵo.isEmpty()) {
					enigo.setVorterSpeco(VorterSpeco.RADIKO);
				}
			}
			
			NodeList derivaĵoNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_DERIVAĴO);
			for(int i = 1; i < derivaĵoNodoj.getLength(); i++) {
				Element nodo = (Element)derivaĵoNodoj.item(i);
				String[] partoj = disigiVortonĈirkaŭRadiko(nodo);
				if(partoj[0].isEmpty() && partoj[2].equalsIgnoreCase(Finaĵo.VERBO_INFINITIVO.getValoro())) {
					enigo.setTransitiveco(ekstraktiTransitivecon(nodo));
					break;
				}
			}
		} catch(ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				partoj[0] = radikNodo.getPreviousSibling().getTextContent();
			}
			if(radikNodo.getNextSibling() != null) {
				partoj[2] = radikNodo.getNextSibling().getTextContent();
			}
			partoj[1] = enigo.getVortero();
		}
		
		return partoj;
	}
	
	private Transitiveco ekstraktiTransitivecon(Element derivaĵNodo) {
		NodeList nodoj = derivaĵNodo.getElementsByTagName(ETIKEDO_VORTSPECO);
		if(nodoj.getLength() > 0) {
			String enhavo = nodoj.item(0).getTextContent();
			switch(enhavo) {
				case "tr":
					return Transitiveco.TRANSITIVA;
				case "ntr":
					return Transitiveco.NETRANSITIVA;
				case "x":
					return Transitiveco.AMBAŬ;
			}
		}
		return Transitiveco.NEDIFINITA;
	}
}