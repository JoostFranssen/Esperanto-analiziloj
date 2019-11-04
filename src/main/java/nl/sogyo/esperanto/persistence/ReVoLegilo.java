package nl.sogyo.esperanto.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import nl.sogyo.esperanto.API.VorterSpeco;

public class ReVoLegilo extends DefaultHandler {
	private File dosiero;
	private ReVoEnigo enigo;
	
	public ReVoLegilo(File dosiero) {
		this.dosiero = dosiero;
	}
	
	public ReVoEnigo legiDosieron() {
		try {
			XMLReader legilo = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			legilo.setContentHandler(this);
			legilo.parse(dosiero.getPath());
		} catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return enigo;
	}
	
	@Override
	public void endDocument() throws SAXException {
		enigo = new ReVoEnigo("aŭt", VorterSpeco.RADIKO);
	}
}