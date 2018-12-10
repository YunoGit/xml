package xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLBuilder {
	private String file;
	private Document document;

	public XMLBuilder(String file) throws Exception {
		this.file = file;
		this.document = this.getDocument();
	}

	private Document getDocument() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new File(this.file));
		return document;
	}

	public List<Element> getElements(String name) throws Exception {
		List<Element> list = new ArrayList<Element>();
		NodeList nodeList = this.document.getElementsByTagName(name);
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element element = (Element) nodeList.item(i);
			list.add(element);
		}
		return list;
	}

	public String getAttributes(Element element, String name) {
		return element.getAttribute(name);
	}

	public void removeElement(Element element) {
		element.getParentNode().removeChild(element);
	}

	public void removeElements(List<Element> list) {
		Iterator<Element> temp = list.iterator();
		while (temp.hasNext()) {
			Element element = temp.next();
			this.removeElement(element);
		}
	}

	public void saveElement(Element parentElement, String name, Map<String, String> attributes) {
		Element element = this.document.createElement(name);
		for (String key : attributes.keySet()) {
			element.setAttribute(key, attributes.get(key));
		}
		parentElement.appendChild(element);
	}

	public void saveXML() throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer former = factory.newTransformer();
		former.setOutputProperty("doctype-public", this.document.getDoctype().getPublicId());
		former.setOutputProperty("doctype-system", this.document.getDoctype().getSystemId());
		former.transform(new DOMSource(this.document), new StreamResult(this.file));
	}

	public String getFile() {
		return file;
	}
}