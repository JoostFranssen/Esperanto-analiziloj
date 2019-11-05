package nl.sogyo.esperanto.persistence;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import nl.sogyo.esperanto.API.VorterSpeco;

public class ReVoLegilo extends DefaultHandler {
	public static final String ETIKEDO_RADIKO = "rad";
	public static final String ETIKEDO_KAPVORTO = "kap";
	
	private String dosierPado;
	private ReVoEnigo enigo;
	
	private boolean vorteroTrovita, vorterSpecoTrovita, transitivecoTrovita;
	private String legendaElemento = "";
	private StringBuilder legatajSignoj;
	
	public ReVoLegilo(String dosierPado) {
		this.dosierPado = dosierPado;
		enigo = new ReVoEnigo();
		legatajSignoj = new StringBuilder();
	}
	
	public ReVoEnigo legiDosieron() {
		try {
			XMLReader legilo = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			legilo.setContentHandler(this);
			legilo.parse(dosierPado);
		} catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return enigo;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if(!vorteroTrovita) {
			if(qName.equalsIgnoreCase(ETIKEDO_RADIKO)) {
				legendaElemento = ETIKEDO_RADIKO;
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) {
		if(!qName.equalsIgnoreCase(legendaElemento)) {
			return;
		}
		
		legendaElemento = "";
		String teksto = legatajSignoj.toString();
		legatajSignoj = new StringBuilder();
		
		if(qName.equalsIgnoreCase(ETIKEDO_RADIKO)) {
			setVortero(teksto);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		if(!legendaElemento.isEmpty()) {
			for(int i = start; i < start + length; i++) {
				legatajSignoj.append(ch[i]);
			}
		}
	}
	
	private void setVortero(String vortero) {
		enigo.setVortero(vortero);
		vorteroTrovita = true;
	}
}