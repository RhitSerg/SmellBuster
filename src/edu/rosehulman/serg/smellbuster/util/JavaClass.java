package edu.rosehulman.serg.smellbuster.util;

public class JavaClass {
		private String name;
		private double wmc;
		private double dit;
		private double noc;
		private double cbo;
		private double rfc;
		private double lcom;
		private double ca;
		private double ce;
		private double npm;
		private double lcom3;
		private double loc;
		private double dam;
		private double moa;
		private double mfa;
		private double cam;
		private double ic;
		private double cbm;
		private double amc;
		private double cc;
		private String other;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			String[] nameSplit = name.split("\\.");
			name = nameSplit[nameSplit.length - 1];
			name += ".java";
			this.name = name;
		}

		public double getWmc() {
			return wmc;
		}

		public void setWmc(double wmc) {
			this.wmc = wmc;
		}

		public double getDit() {
			return dit;
		}

		public void setDit(double dit) {
			this.dit = dit;
		}

		public double getNoc() {
			return noc;
		}

		public void setNoc(double noc) {
			this.noc = noc;
		}

		public double getCbo() {
			return cbo;
		}

		public void setCbo(double cbo) {
			this.cbo = cbo;
		}

		public double getRfc() {
			return rfc;
		}

		public void setRfc(double rfc) {
			this.rfc = rfc;
		}

		public double getLcom() {
			return lcom;
		}

		public void setLcom(double lcom) {
			this.lcom = lcom;
		}

		public double getCa() {
			return ca;
		}
		
		public void setCa(double ca) {
			this.ca = ca;
		}

		public double getCe() {
			return ce;
		}

		public void setCe(double ce) {
			this.ce = ce;
		}

		public double getNpm() {
			return npm;
		}

		public void setNpm(double npm) {
			this.npm = npm;
		}

		public double getLcom3() {
			return lcom3;
		}

		public void setLcom3(double lcom3) {
			this.lcom3 = lcom3;
		}

		public double getLoc() {
			return loc;
		}

		public void setLoc(double loc) {
			this.loc = loc;
		}

		public double getDam() {
			return dam;
		}

		public void setDam(double dam) {
			this.dam = dam;
		}

		public double getMoa() {
			return moa;
		}

		public void setMoa(double moa) {
			this.moa = moa;
		}

		public double getMfa() {
			return mfa;
		}

		public void setMfa(double mfa) {
			this.mfa = mfa;
		}

		public double getCam() {
			return cam;
		}

		public void setCam(double cam) {
			this.cam = cam;
		}

		public double getIc() {
			return ic;
		}

		public void setIc(double ic) {
			this.ic = ic;
		}

		public double getCbm() {
			return cbm;
		}

		public void setCbm(double cbm) {
			this.cbm = cbm;
		}

		public double getAmc() {
			return amc;
		}

		public void setAmc(double amc) {
			this.amc = amc;
		}

		public String getOther() {
			return other;
		}

		public void setOther(String other) {
			this.other = other;
		}

		public double getCc() {
			return cc;
		}

		public void setCc(double cc) {
			this.cc = cc;
		}

		public String getAggregate(){
			double score = ((10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm
					- (0.5 * amc) - cc);
			return String.valueOf(score);
		}
	}
