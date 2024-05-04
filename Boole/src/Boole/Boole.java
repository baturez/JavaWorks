package Boole;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class Boole {
	public static List<String> degiskenler = new ArrayList<String>();
	public static Map<String, Boolean> degerler = new HashMap<String, Boolean>();
	public static String[] sc;
	public static ArrayList<String> deger = new ArrayList<String>();
	public static Map<String, String> FF = new HashMap<String, String>();
	public static Map<String, String> FY = new HashMap<String, String>();
	public static void main(String[] args) {
		String file = "boole.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			br.close();
			System.out.println(file + " dosyası okundu.");
		} catch (IOException e) {
			System.err.println("Dosya okunamadı " + e.getMessage());
		}
		assignments(file, degiskenler);
		System.out.println("doğruluk tablosu: \n ");
		for (int i = 0; i < degiskenler.size(); i++) {
			System.out.print(degiskenler.get(i) + " ");
		}
		System.out.print("F");
		System.out.println("");
		dogruluk_tablosu(degerler, file);
		System.out.println("Fonksiyon İfadeleri:\r\n" + "");
		Read(file);
		Write();
		funcitons();
	}
	public static void Read(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String sıra;
			while ((sıra = br.readLine()) != null) {
				int index = sıra.indexOf("F =");
				if (index != -1) {
					String expression = sıra.substring(index + 4);
					expression = expression.replaceAll("\\s+", "");
					sc = expression.split("");
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("Dosya okunamadı " + e.getMessage());
		}
	}
	public static boolean F(Map<String, Boolean> degerler, String file) {
		boolean Fx = degerler.get(degiskenler.get(0))
				| (degerler.get(degiskenler.get(2)) & degerler.get(degiskenler.get(3))
						| (!degerler.get(degiskenler.get(0)) & degerler.get(degiskenler.get(1))
								& !degerler.get(degiskenler.get(2)) & !degerler.get(degiskenler.get(3))));
		return Fx;
	}
	public static void Write() {
		for (int i = 0; i < sc.length; i++) {
			deger.add(sc[i]);
		}
		ArrayList<String> birlestirilmisDeger = new ArrayList<String>();
		StringBuilder birlesmisKelime = new StringBuilder();
		for (int i = 0; i < deger.size(); i++) {
			String current = deger.get(i);
			if ("+".equals(current) && birlesmisKelime.length() > 0) {
				birlestirilmisDeger.add(birlesmisKelime.toString());
				birlesmisKelime.setLength(0);
				birlestirilmisDeger.add("+");
			} else {
				birlesmisKelime.append(current);
			}
		}
		if (birlesmisKelime.length() > 0) {
			birlestirilmisDeger.add(birlesmisKelime.toString());
		}
		deger.removeAll(deger);
		deger.addAll(birlestirilmisDeger);
	}
	public static void funcitons() {
		String ax = "";
		ArrayList<String> bx = new ArrayList<String>();
		ArrayList<String> dx = new ArrayList<String>();
		Set<String> sx = FF.keySet();
		Set<String> sy = FY.keySet();
		dx.addAll(sy);
		bx.addAll(sx);
		String by = "";
		for (int i = 0; i < FF.size(); i++) {
			String x = String.valueOf(i);
			if (!(FF.get(x) == null)) {
				ax = ax + FF.get(x) + "+ ";
			}
		}
		for (int i = 0; i < FY.size(); i++) {
			String x = String.valueOf(i);
			if (!(FY.get(x) == null)) {
				by = by + "(" + FY.get(x) + ")" + ". ";
			}
		}
		System.out.println("F = " + ax);
		System.out.println("F = Σ" + bx);
		System.out.println("F = " + by);
		System.out.println("F = ∏" + sy);
	}
	public static void dogruluk_tablosu(Map<String, Boolean> degerler, String file) {
		for (int i = 0; i < degiskenler.size(); i++) {
			degerler.put(degiskenler.get(i), null);
		}
		degerler.put(degiskenler.get(0), false);
		degerler.put(degiskenler.get(1), false);
		degerler.put(degiskenler.get(2), false);
		degerler.put(degiskenler.get(3), false);
		Collection<Boolean> values = degerler.values();
		for (boolean value : values) {
			System.out.print(value ? "1 " : "0 ");
		}
		System.out.print(F(degerler, file) ? "1" : "0");
		if (F(degerler, file)) {
			FF.put("0", degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2) + "'"
					+ degiskenler.get(3) + "'");
		} else {
			FY.put("0", degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2) + "'"
					+ degiskenler.get(3) + "'");
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("1", degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2) + "'"
						+ degiskenler.get(3));
			} else {
				FY.put("1", degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2) + "'"
						+ degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("2", degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2)
						+ degiskenler.get(3) + "'");
			}
			else {
				FY.put("2", degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2)
						+ degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("3",
						degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2) + degiskenler.get(3));
			} else {
				FY.put("3",
						degiskenler.get(0) + "'" + degiskenler.get(1) + "'" + degiskenler.get(2) + degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), false);
			degerler.put(degiskenler.get(1), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("4", degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + "'"
						+ degiskenler.get(3) + "'");
			} else {
				FY.put("4", degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + "'"
						+ degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), false);
			degerler.put(degiskenler.get(1), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("5",
						degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + "'" + degiskenler.get(3));
			} else {
				FY.put("5",
						degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + "'" + degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), true);
			degerler.put(degiskenler.get(1), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("6",
						degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3) + "'");
			} else {
				FY.put("6",
						degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), true);
			degerler.put(degiskenler.get(1), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");

			if (F(degerler, file)) {
				FF.put("7", degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3));
			} else {
				FY.put("7", degiskenler.get(0) + "'" + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(false) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), false);
			degerler.put(degiskenler.get(1), false);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("8", degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + "'"
						+ degiskenler.get(3) + "'");
			} else {
				FY.put("8", degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + "'"
						+ degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), false);
			degerler.put(degiskenler.get(1), false);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("9",
						degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + "'" + degiskenler.get(3));
			} else {
				FY.put("9",
						degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + "'" + degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), true);
			degerler.put(degiskenler.get(1), false);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("10",
						degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + degiskenler.get(3) + "'");
			} else {
				FY.put("10",
						degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), true);
			degerler.put(degiskenler.get(1), false);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("11", degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + degiskenler.get(3));
			} else {
				FY.put("11", degiskenler.get(0) + degiskenler.get(1) + "'" + degiskenler.get(2) + degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(false)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), false);
			degerler.put(degiskenler.get(1), true);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("12",
						degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + "'" + degiskenler.get(3) + "'");
			} else {
				FY.put("12",
						degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + "'" + degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), false);
			degerler.put(degiskenler.get(1), true);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("13", degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + "'" + degiskenler.get(3));
			} else {
				FY.put("13", degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + "'" + degiskenler.get(3));
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(false) && degerler.get(degiskenler.get(3)).equals(true)) {
			degerler.put(degiskenler.get(3), false);
			degerler.put(degiskenler.get(2), true);
			degerler.put(degiskenler.get(1), true);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("14", degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3) + "'");
			} else {
				FY.put("14", degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3) + "'");
			}
		}
		System.out.println("");
		if (degerler.get(degiskenler.get(0)).equals(true) && degerler.get(degiskenler.get(1)).equals(true)
				&& degerler.get(degiskenler.get(2)).equals(true) && degerler.get(degiskenler.get(3)).equals(false)) {
			degerler.put(degiskenler.get(3), true);
			degerler.put(degiskenler.get(2), true);
			degerler.put(degiskenler.get(1), true);
			degerler.put(degiskenler.get(0), true);
			for (boolean value : values) {
				System.out.print(value ? "1 " : "0 ");
			}
			System.out.print(F(degerler, file) ? "1" : "0");
			if (F(degerler, file)) {
				FF.put("15", degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3));
			} else {
				FY.put("15", degiskenler.get(0) + degiskenler.get(1) + degiskenler.get(2) + degiskenler.get(3));
			}
		}
		System.out.println("");
	}
	public static void assignments(String file, List<String> degiskenler) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String sıra;
			while ((sıra = br.readLine()) != null) {
				int index = sıra.indexOf("F =");
				if (index != -1) {
					String expression = sıra.substring(index + 4);
					expression = expression.replaceAll("\\s+", "");

					for (char c : expression.toCharArray()) {
						if (Character.isLetter(c)) {
							String degisken = String.valueOf(c);
							if (!degiskenler.contains(degisken)) {
								degiskenler.add(degisken);
							}
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.println("Dosya okunamadı " + e.getMessage());
		}
		Collections.sort(degiskenler);
	}
}