package edu.rosehulman.serg.smellbuster.test;

public class Test {

	public void TestMethod1(int a, int b) {
		if (a > 10) {
			System.out.println(a);
		} else {
			System.out.println(b);
		}
	}

	public void TestMethod2(int a, int b) {
		if (a > 10) {
			System.out.println(a);
		} else if (b > 20) {
			System.out.println(b);
		} else if (a*b > 7){
			System.out.println(a + b);
		} else {
			System.out.println(a/b);
		}
	}

	public void TestMethod3(int a, int b) {
		if (a > 10) {
			if (b < 20) {
				System.out.println(a);
			}
		} else if (b > 20) {
			System.out.println(b);
		} else {
			System.out.println(a + b);
		}
	}

	public void TestMethod4(int a, int b) {
		if (a > 10) {
			if (b < 20) {
				System.out.println(a);
			}
		} else if (a < 25) {
			if (b > 30) {
				System.out.println(b);
			} else {
				System.out.println(a + b);
			}
		} else {
			System.out.println(a - b);
		}
	}

	public void TestMethod5(int a, int b) {
		if (a > 10) {
			if (b < 20) {
				System.out.println(a);
			}
		} else if (a < 25) {
			if (b > 30) {
				System.out.println(b);
			} else {
				System.out.println(a + b);
			}
		} else {
			if (a * b > 25) {
				System.out.println(a - b);
			} else {
				System.out.println(a / b);
			}
		}
	}

	public void TestMethod6(int a, int b) {
		if (a > 10) {
			if (b < 20) {
				System.out.println(a);
			} else if (a - b > 10) {
				System.out.println(a * b);
			} else if (b - a > 0) {
				System.out.println(a * 2);
			} else {
				System.out.println(a * 3);
			}
		} else if (a < 25) {
			if (b > 30) {
				System.out.println(b);
			} else {
				System.out.println(a + b);
			}
		} else {
			if (a * b > 25) {
				System.out.println(a - b);
			} else {
				System.out.println(a / b);
			}
		}
	}
	
	public void TestMethod7(int a, int b) {
		if (a > 10) {
			if (b < 20) {
				System.out.println(a);
			} else if (a - b > 10) {
				System.out.println(a * b);
			} else if (b - a > 0) {
				System.out.println(a * 2);
			} else {
				System.out.println(a * 3);
			}
		} else if (a < 25) {
			if (b > 30) {
				System.out.println(b);
			} else {
				System.out.println(a + b);
			}
		} else if (a/b < 20) {
			if (b-a > 30) {
				System.out.println(b);
			} else {
				System.out.println(a + b);
			}
		} else {
			if (a * b > 25) {
				System.out.println(a - b);
			} else {
				System.out.println(a / b);
			}
		}
	}
	
	public void TestMethod8(int a, int b) {
		if (a > 10) {
			if (a * b > 25) {
				if (b-a > 30) {
					System.out.println(b);
				} else {
					System.out.println(a + b);
				}
			} else {
				System.out.println(a / b);
			}
		} else {
			System.out.println(b);
		}
	}

}