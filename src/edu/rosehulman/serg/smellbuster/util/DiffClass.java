package edu.rosehulman.serg.smellbuster.util;

public class DiffClass {
		private String name;
		private String packageName;
		private String location;
		private char type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public char getType() {
			return type;
		}

		public void setType(char type) {
			this.type = type;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getLocation(){
			return this.location;
		}
		
		public void setLocation(String location){
			this.location = location;
		}

	}
