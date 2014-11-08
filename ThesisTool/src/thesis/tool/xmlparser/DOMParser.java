package thesis.tool.xmlparser;

import java.io.File;
import java.util.ArrayList;
import thesis.tool.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMParser {

	private DocumentBuilderFactory factory = null;
	private DocumentBuilder builder = null;
	private Document document = null;
	private ArrayList<JavaClass> jcList;
	private ArrayList<DiffClass> dcList;
	private ArrayList<String> baseVersion;

	public DOMParser() {
		this.factory = DocumentBuilderFactory.newInstance();
		try {
			this.builder = this.factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFileName(String fileName) {
		try {
			File f = new File(fileName);
			this.document = this.builder.parse(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseXML() {

		this.jcList = new ArrayList<>();
		this.dcList = new ArrayList<>();
		this.baseVersion = new ArrayList<>();
		NodeList nodeList = this.document.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);
			if (node instanceof Element) {
				JavaClass jc = new JavaClass();

				NodeList childNodes = node.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node cNode = childNodes.item(j);
					if (cNode instanceof Element) {
						String content = cNode.getLastChild().getTextContent()
								.trim();
						switch (cNode.getNodeName()) {
						case "name":
							jc.setName(content);
							break;
						case "wmc":
							jc.setWmc(Double.parseDouble(content));
							break;
						case "dit":
							jc.setDit(Double.parseDouble(content));
							break;
						case "noc":
							jc.setNoc(Double.parseDouble(content));
							break;
						case "cbo":
							jc.setCbo(Double.parseDouble(content));
							break;
						case "rfc":
							jc.setRfc(Double.parseDouble(content));
							break;
						case "lcom":
							jc.setLcom(Double.parseDouble(content));
							break;
						case "ca":
							jc.setCa(Double.parseDouble(content));
							break;
						case "ce":
							jc.setCe(Double.parseDouble(content));
							break;
						case "npm":
							jc.setNpm(Double.parseDouble(content));
							break;
						case "lcom3":
							jc.setLcom3(Double.parseDouble(content));
							break;
						case "loc":
							jc.setLoc(Double.parseDouble(content));
							break;
						case "dam":
							jc.setDam(Double.parseDouble(content));
							break;
						case "moa":
							jc.setMoa(Double.parseDouble(content));
							break;
						case "mfa":
							jc.setMfa(Double.parseDouble(content));
							break;
						case "cam":
							jc.setCam(Double.parseDouble(content));
							break;
						case "ic":
							jc.setIc(Double.parseDouble(content));
							break;
						case "cbm":
							jc.setCbm(Double.parseDouble(content));
							break;
						case "amc":
							jc.setAmc(Double.parseDouble(content));
							break;
						case "cc":
							double cc = 0.0;
							NodeList children = cNode.getChildNodes();
							for (int k = 0; k < children.getLength(); k++) {
								Node tempNode = children.item(k);
								if (tempNode.getNodeName().equals("method")) {
									String ccScore = tempNode.getLastChild()
											.getTextContent().trim();
									cc += Double.parseDouble(ccScore);
								}
							}
							jc.setCc(cc);
							break;
						case "path":
							String type = cNode.getAttributes()
									.getNamedItem("item").getNodeValue();
							if (!type.equals("deleted")) {
								DiffClass dc = new DiffClass();
								dc.setName(content);
								dc.setType(type.charAt(0));
								this.dcList.add(dc);
							}
							break;
						case "entry":
							this.baseVersion.add(cNode.getChildNodes().item(1)
									.getTextContent().trim());
							break;
						default:
							jc.setOther(content);
							break;
						}
					}
				}
				this.jcList.add(jc);
			}

		}
	}

	public ArrayList<JavaClass> getJavaClassList() {
		return this.jcList;
	}

	public ArrayList<DiffClass> getDiffClassList() {
		return this.dcList;
	}

	public ArrayList<String> getBaseVersion() {
		return this.baseVersion;
	}
}
