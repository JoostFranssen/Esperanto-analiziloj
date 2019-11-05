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

import nl.sogyo.esperanto.API.VorterSpeco;

public class ReVoLegilo extends DefaultHandler {
	public static final String ETIKEDO_RADIKO = "rad";
	public static final String ETIKEDO_KAPVORTO = "kap";
	
	private String dosierPado;
	private ReVoEnigo enigo;
	
	public ReVoLegilo(String dosierPado) {
		this.dosierPado = dosierPado;
		enigo = new ReVoEnigo();
	}
	
	public ReVoEnigo legiDosieron() {
		DocumentBuilderFactory fabriko = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder konstruilo = fabriko.newDocumentBuilder();
			Document dokumento = konstruilo.parse(dosierPado);
			Element ĉefElemento = dokumento.getDocumentElement();
			ĉefElemento.normalize();
			
			enigo.setVortero(ekstraktiVorteron(ĉefElemento));
			
			NodeList kapvortNodoj = ĉefElemento.getElementsByTagName(ETIKEDO_KAPVORTO);
			
			if(kapvortNodoj.getLength() > 0) {
				String finaĵo = ekstraktiFinaĵonDeKapvorto(kapvortNodoj.item(0));
				if(!finaĵo.isEmpty()) {
					enigo.setVorterSpeco(VorterSpeco.RADIKO);
				}
			}
			
			
		} catch(ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return enigo;
	}
	
	private String ekstraktiVorteron(Element ĉefElemento) {
		NodeList nodoj = ĉefElemento.getElementsByTagName(ETIKEDO_RADIKO);
		if(nodoj.getLength() > 0) {
			return nodoj.item(0).getTextContent();
		}
		return "";
	}
	
	private String ekstraktiFinaĵonDeKapvorto(Node kapvortNodo) {
		String enhavo = kapvortNodo.getTextContent();
		
		Pattern ŝablono = Pattern.compile(String.format("(%s)/(\\S*)", enigo.getVortero()));
		Matcher kongruilo = ŝablono.matcher(enhavo);
		if(kongruilo.find()) {
			return kongruilo.group(2);
		} else {
			return "";
		}
	}
}