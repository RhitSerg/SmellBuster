package edu.rosehulman.serg.smellbuster.metricparser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.rosehulman.serg.smellbuster.util.*;

public class CKJMParser implements IMetricParser {

	private DocumentBuilderFactory factory = null;
	private DocumentBuilder builder = null;
	private Document document = null;
	private ArrayList<MetricDOMObject> domObjectList;
	private ArrayList<DiffClass> dcList;
	private ArrayList<String> baseVersion;

	public CKJMParser() {
		this.factory = DocumentBuilderFactory.newInstance();
		try {
			this.builder = this.factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeDOMFromFile(String fileName) {
		try {
			File f = new File(fileName);
			this.document = this.builder.parse(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseXML() {

		this.domObjectList = new ArrayList<>();
		this.dcList = new ArrayList<>();
		this.baseVersion = new ArrayList<>();
		NodeList nodeList = this.document.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);
			if (node instanceof Element) {
				MetricDOMObject metricDomObj = new MetricDOMObject();

				NodeList childNodes = node.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node cNode = childNodes.item(j);
					if (cNode instanceof Element) {
						String content = cNode.getLastChild().getTextContent()
								.trim();
						switch (cNode.getNodeName()) {
						case "name":
							metricDomObj.setName(content);
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
							metricDomObj.setValueForMetric("cc", cc);
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
							metricDomObj.setValueForMetric(cNode.getNodeName(), Double.parseDouble(content));
							break;
						}
					}
				}
				this.domObjectList.add(metricDomObj);
			}

		}
	}

	public ArrayList<MetricDOMObject> getDOMObjList() {
		return this.domObjectList;
	}

	public ArrayList<DiffClass> getDiffClassList() {
		return this.dcList;
	}

	public ArrayList<String> getBaseVersion() {
		return this.baseVersion;
	}
}
