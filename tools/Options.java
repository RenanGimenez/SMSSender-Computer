package tools;

import java.io.File;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Options {

	private static String showMessagesBy;
	private static boolean soundAllowed;
	private static boolean windowsNotificationAllowed;
	private static String country;
	private static String PATH = "options.xml";
	private static String prefixOfCountry;
	private static Document optionsFile;

	static {
		try {

			File fXmlFile = new File(PATH);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			optionsFile = dBuilder.parse(fXmlFile);
			optionsFile.getDocumentElement().normalize();
			
			Node values = optionsFile.getElementsByTagName("values").item(0);
			if (values.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) values;
				soundAllowed = Boolean.valueOf(element.getElementsByTagName("soundAllowed").item(0).getTextContent());
				windowsNotificationAllowed = Boolean.valueOf(element.getElementsByTagName("windowsNotificationAllowed").item(0).getTextContent());
				showMessagesBy = element.getElementsByTagName("showMessagesBy").item(0).getTextContent();
				country = element.getElementsByTagName("country").item(0).getTextContent();
				prefixOfCountry = element.getElementsByTagName("prefix").item(0).getTextContent();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] getCountryChoices() {

		Node choicesNode = optionsFile.getElementsByTagName("choices").item(0);
		Node countriesNode = ((Element)choicesNode).getElementsByTagName("countries").item(0);

		Element element = (Element) countriesNode;
		NodeList listCountries = element.getElementsByTagName("choice");
		String[] choices = new String[listCountries.getLength()];
		for(int i=0; i<listCountries.getLength(); ++i) {
			choices[i] = listCountries.item(i).getTextContent();
		}

		return choices;
	}
	public static String getShowMessagesBy() {
		return showMessagesBy;
	}
	public static void setShowMessagesBy(String showMessagesBy) {
		Options.showMessagesBy = showMessagesBy;
		Node values = optionsFile.getElementsByTagName("values").item(0);
		Element element = (Element) values;
		element.getElementsByTagName("showMessagesBy").item(0).setTextContent(showMessagesBy);
		
		try {
			saveChanges();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isSoundAllowed() {
		return soundAllowed;
	}
	public static void setSoundAllowed(boolean soundAllowed) {
		Options.soundAllowed = soundAllowed;
		Node values = optionsFile.getElementsByTagName("values").item(0);
		Element element = (Element) values;
		element.getElementsByTagName("soundAllowed").item(0).setTextContent(Boolean.toString(soundAllowed));
		
		try {
			saveChanges();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isWindowsNotificationAllowed() {
		return windowsNotificationAllowed;
	}
	public static void setWindowsNotificationAllowed(boolean windowsNotificationAllowed) {
		Options.windowsNotificationAllowed = windowsNotificationAllowed;
		Node values = optionsFile.getElementsByTagName("values").item(0);
		Element element = (Element) values;
		element.getElementsByTagName("windowsNotificationAllowed").item(0).setTextContent(Boolean.toString(windowsNotificationAllowed));
		
		try {
			saveChanges();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getCountry() {
		return country;
	}
	public static void setCountry(String country) {
		Options.country = country;
		Node values = optionsFile.getElementsByTagName("values").item(0);
		Element element = (Element) values;
		element.getElementsByTagName("country").item(0).setTextContent(country);
		
		Node choicesNode = optionsFile.getElementsByTagName("choices").item(0);
		Node countriesNode = ((Element)choicesNode).getElementsByTagName("countries").item(0);

		Element element2 = (Element) countriesNode;
		NodeList listCountries = element2.getElementsByTagName("choice");
		for(int i=0; i<listCountries.getLength(); ++i) {
			if(listCountries.item(i).getTextContent().equals(country)) {
				element.getElementsByTagName("prefix").item(0).setTextContent(
						listCountries.item(i).getAttributes().getNamedItem("prefix").getNodeValue()
						);
				break;
			}
			
		}
		
		try {
			saveChanges();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[] getShowMessagesByChoices() {
		Node choicesNode = optionsFile.getElementsByTagName("choices").item(0);
		Node showMessagesByNode = ((Element)choicesNode).getElementsByTagName("showMessagesBy").item(0);

		Element element = (Element) showMessagesByNode;
		NodeList listCountries = element.getElementsByTagName("choice");
		String[] choices = new String[listCountries.getLength()];
		for(int i=0; i<listCountries.getLength(); ++i) {
			choices[i] = listCountries.item(i).getTextContent();
		}

		return choices;
	}
	public static String getPrefixOfCountry() {
		return prefixOfCountry;
		
	}

	private static void saveChanges() throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(optionsFile);
		StreamResult result = new StreamResult(new File(PATH));
		transformer.transform(source, result);
	}
}
