package edu.rosehulman.serg.smellbuster.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.rosehulman.serg.smellbuster.util.JavaClass;
import edu.rosehulman.serg.smellbuster.xmlparser.DOMParser;

public class ResultTableLogic {

	private Map<String, ArrayList<JavaClass>> metricMap;
	private Map<Integer, String> versionMap;

	public ResultTableLogic(Map<Integer, String> versionMap) {
		this.metricMap = new HashMap<>();
		this.versionMap = versionMap;
		parseMetrics();
	}

	public ArrayList<JavaClass> createClassListMap(String className) {
		ArrayList<JavaClass> list = new ArrayList<JavaClass>();
		JavaClass[] classList = new JavaClass[this.versionMap.keySet().size()];
		for (String version : metricMap.keySet()) {
			ArrayList<JavaClass> jcList = metricMap.get(version);
			for (JavaClass jc : jcList) {
				String name = jc.getName();
				if (name.equals(className)) {

					int j = 0;
					Iterator<Integer> itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					classList[j] = jc;
				}
			}
		}
		for (int i = 0; i < classList.length; i++) {
			list.add(classList[i]);
		}

		return list;
	}

	public void parseMetrics() {
		File[] files = new File("MetricAnalysis").listFiles();
		DOMParser parser = new DOMParser();

		for (int i = 0; i < files.length; i++) {
			parser.setFileName(files[i].getAbsolutePath());
			parser.parseXML();

			ArrayList<JavaClass> jcList = parser.getJavaClassList();

			String[] nameParts = files[i].getName().split("-");
			String version = nameParts[nameParts.length - 1];
			version = version.substring(0, version.length() - 4);

			metricMap.put(version, jcList);
		}
	}

	public String getWMCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getWmc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getNOCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getNoc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCBOValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCbo());
						}
					}
				}
			}
		}
		return value;
	}

	public String getLCOMValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getLcom());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCAValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCa());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCEValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCe());
						}
					}
				}
			}
		}
		return value;
	}

	public String getLCOM3ValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getLcom3());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCAMValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCam());
						}
					}
				}
			}
		}
		return value;
	}

	public String getICValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getIc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCBMValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCbm());
						}
					}
				}
			}
		}
		return value;
	}

	public String getAMCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getAmc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getAggregateMetrics(String className) {
		String value = "N/A";
		double score = 0.0;
		ArrayList<JavaClass> classList = createClassListMap(className);
		for (JavaClass jc : classList) {
			if (jc != null) {
				double wmc = jc.getWmc();
				double noc = jc.getNoc();
				double cbo = jc.getCbo();
				double lcom3 = jc.getLcom3();
				double cam = jc.getCam();
				double ic = jc.getIc();
				double cbm = jc.getCbm();
				double amc = jc.getAmc();
				double cc = jc.getCc();
				score += ((10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm
						- (0.5 * amc) - cc);
			}
		}
		value = String.valueOf(score);
		return value;
	}

	public String getAggregateMetricsOfMax(String className) {
		String value = "N/A";
		ArrayList<JavaClass> classList = createClassListMap(className);
		double wmc = Integer.MIN_VALUE;
		double noc = Integer.MIN_VALUE;
		double cbo = Integer.MIN_VALUE;
		double lcom3 = Integer.MIN_VALUE;
		double cam = Integer.MIN_VALUE;
		double ic = Integer.MIN_VALUE;
		double cbm = Integer.MIN_VALUE;
		double amc = Integer.MIN_VALUE;
		double cc = Integer.MIN_VALUE;
		for (JavaClass jc : classList) {
			if (jc != null) {
				wmc = Math.max(wmc, jc.getWmc());
				noc = Math.max(noc, jc.getNoc());
				cbo = Math.max(cbo, jc.getCbo());
				lcom3 = Math.max(lcom3, jc.getLcom3());
				cam = Math.max(cam, jc.getCam());
				ic = Math.max(ic, jc.getIc());
				cbm = Math.max(cbm, jc.getCbm());
				amc = Math.max(amc, jc.getAmc());
				cc = Math.max(cc, jc.getCc());
			}
		}
		if (wmc != Integer.MAX_VALUE) {
			double score = ((10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm
					- (0.5 * amc) - cc);
			value = String.valueOf(score);
		}
		return value;
	}

	public String getAggregateMetricsOfMin(String className) {
		String value = "N/A";
		ArrayList<JavaClass> classList = createClassListMap(className);
		double wmc = Integer.MAX_VALUE;
		double noc = Integer.MAX_VALUE;
		double cbo = Integer.MAX_VALUE;
		double lcom3 = Integer.MAX_VALUE;
		double cam = Integer.MAX_VALUE;
		double ic = Integer.MAX_VALUE;
		double cbm = Integer.MAX_VALUE;
		double amc = Integer.MAX_VALUE;
		double cc = Integer.MAX_VALUE;
		for (JavaClass jc : classList) {
			if (jc != null) {
				wmc = Math.min(wmc, jc.getWmc());
				noc = Math.min(noc, jc.getNoc());
				cbo = Math.min(cbo, jc.getCbo());
				lcom3 = Math.min(lcom3, jc.getLcom3());
				cam = Math.min(cam, jc.getCam());
				ic = Math.min(ic, jc.getIc());
				cbm = Math.min(cbm, jc.getCbm());
				amc = Math.min(amc, jc.getAmc());
				cc = Math.min(cc, jc.getCc());
			}
		}
		if (wmc != Integer.MAX_VALUE) {
			double score = ((10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm
					- (0.5 * amc) - cc);
			value = String.valueOf(score);
		}
		return value;
	}
}
