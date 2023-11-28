import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Graf {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String file = "Bilgiler.txt";
		Map<String, Integer> countrys = new LinkedHashMap<String, Integer>();
		Map<String, String> Starts = new LinkedHashMap<String, String>();
		Map<String, LinkedList<String>> ww = new LinkedHashMap<String, LinkedList<String>>();
		Map<String, String> speeds = new LinkedHashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			br.close();
			System.out.println(file + " dosyası okundu.");
		} catch (IOException e) {
			System.err.println("Dosya okunamadı " + e.getMessage());
		}
		Assignments(countrys, file);
		int[][] graf = createGraph(file, countrys);
		StartsPoints(Starts, countrys, file);
		ww = ways(file);
		calculateArrivalTime(countrys, graf, ww);
		calculateTotalDistance(countrys, graf, ww);
		System.out.println(
				"Kimin hangi saat itibariyle hangi noktada olduğunu hesaplamak için, aralarında bir boşluk bırakarak kişi ismini ve saati\r\n"
						+ "giriniz:");
		LocalTime time = LocalTime.parse("08:00", DateTimeFormatter.ofPattern("HH:mm"));
		;
		String bilgiler = sc.nextLine();
		String[] bilgi = bilgiler.split(" ");
		String isimx = bilgi[0];
		LocalTime girilen = LocalTime.parse(bilgi[1], DateTimeFormatter.ofPattern("HH:mm"));
		long saatFarki = time.until(girilen, java.time.temporal.ChronoUnit.HOURS);
		int sumx = 0;
		int c = ww.get(isimx).size();
		String dividex = ww.get(isimx).getLast();
		double dx = Double.parseDouble(dividex.replace(",", "."));
		String key = ww.get(isimx).get(c - 1);
		ww.get(isimx).remove(c - 1);
		String bul = null;
		for (int i = 0; i < c - 2; i++) {
			int x = countrys.get(ww.get(isimx).get(i));
			int y = countrys.get(ww.get(isimx).get(i + 1));
			sumx = sumx + graf[x][y];
			int dd = (int) (sumx / dx);
			LocalTime new_time = time.plusMinutes(dd);
			if (new_time.equals(girilen) || new_time.isAfter(girilen)) {
				System.out.println(isimx + " " + girilen + " itibariyle " + ww.get(isimx).get(i) + " "
						+ ww.get(isimx).get(i + 1) + " arasındadır");
				break;
			}
		}
		ww.get(isimx).add(key);
		System.out.println(
				"İki şehir arasında direkt yol olup olmadığını sorgulamak için şehir isimlerini aralarında bir boşluk bırakarak giriniz:");
		String giriş = sc.nextLine();
		String girdi[] = giriş.split(" ");
		String x = girdi[0];
		String y = girdi[1];
		int xx = countrys.getOrDefault(x, 0);
		int yy = countrys.getOrDefault(y, 0);
		for (int i = 0; i < graf.length; i++) {
			if (graf[xx][yy] == 0) {
				System.out.println(x + " " + y + " arasında direkt yol yok");
				break;
			} else {
				System.out.println(x + " " + y + " arasındaki mesafe " + graf[xx][yy] + "km");
				break;
			}
		}
	}

	public static void calculateArrivalTime(Map<String, Integer> countrys, int[][] graf,
			Map<String, LinkedList<String>> ww) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Varış süresi hesaplanacak olan kişinin ismini giriniz:");
		String isim = sc.next();
		int sum = 0;
		int a = ww.get(isim).size();
		String divide = ww.get(isim).getLast();
		double d = Double.parseDouble(divide.replace(",", "."));
		String key = ww.get(isim).get(a - 1);
		ww.get(isim).remove(a - 1);
		for (int i = 0; i < a - 2; i++) {
			int x = countrys.get(ww.get(isim).get(i));
			int y = countrys.get(ww.get(isim).get(i + 1));
			sum = sum + graf[x][y];
		}
		d = sum / d;
		System.out.println(isim + " " + ww.get(isim).get(a - 2) + " varış süresi " + d + "dk");
		ww.get(isim).add(key);
	}

	public static void calculateTotalDistance(Map<String, Integer> countrys, int[][] graf,
			Map<String, LinkedList<String>> ww) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Toplam yol uzunluğu hesaplanacak olan kişinin ismini giriniz:");
		String isim = sc.next();
		int sum = 0;
		int a = ww.get(isim).size();
		String key = ww.get(isim).get(a - 1);
		if (a > 1) {
			ww.get(isim).remove(a - 1);
			for (int i = 0; i < a - 2; i++) {
				int x = countrys.get(ww.get(isim).get(i));
				int y = countrys.get(ww.get(isim).get(i + 1));
				sum = sum + graf[x][y];
			}
			System.out.println(isim + " toplam yol uzunluğu " + sum + " km");
		} else {
			System.out.println(isim + " sadece bir noktada.");
		}
		ww.get(isim).add(key);
	}

	public static Map<String, LinkedList<String>> ways(String file) {
		Map<String, LinkedList<String>> ww = new LinkedHashMap<String, LinkedList<String>>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String sıra;
			boolean readway = false;
			while ((sıra = reader.readLine()) != null) {
				if (sıra.equals("Kişilerin Seyahat Noktaları ve Hızları:")) {
					readway = true;
					continue;
				} else if (sıra.endsWith(" ")) {
					readway = false;
					continue;
				}
				if (readway && !sıra.isEmpty()) {
					String[] parts = sıra.split(" ");
					String per = parts[0];
					String[] carts = parts[1].split("-");
					String speed = parts[2].trim();
					LinkedList<String> yol = new LinkedList<>();
					for (String cart : carts) {
						yol.add(cart);
					}
					yol.addLast(speed);
					ww.put(per, yol);
				}
			}
		} catch (Exception e) {
		}
		return ww;
	}

	public static int[][] createGraph(String file, Map<String, Integer> countrys) {
		Assignments(countrys, file);
		int a = countrys.size() + 1;
		int[][] graf = new int[a][a];
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String sıra;
			boolean readgraf = false;
			int row = 0;
			while ((sıra = reader.readLine()) != null) {
				if (sıra.equals("Graf:")) {
					readgraf = true;
					continue;
				} else if (sıra.equals("Kişiler ve Başlangıç Noktaları:")) {
					readgraf = false;
					continue;
				}
				if (readgraf && !sıra.trim().isEmpty()) {
					String[] parts = sıra.split("\t");

					for (int col = 1; col < parts.length; col++) {
						graf[0][0] = 0;
						graf[row][0] = row;
						graf[row][col] = Integer.parseInt(parts[col]);
					}
					row++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graf;
	}

	public static Map<String, String> StartsPoints(Map<String, String> Starts, Map<String, Integer> countrys,
			String file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String sıra;
			boolean readingPersons = false;
			while ((sıra = reader.readLine()) != null) {
				if (sıra.equals("Kişiler ve Başlangıç Noktaları:")) {
					readingPersons = true;
					continue;
				} else if (sıra.equals("Kişilerin Seyahat Noktaları ve Hızları:")) {
					readingPersons = false;
					continue;
				}
				if (readingPersons && !sıra.trim().isEmpty()) {
					String[] parts = sıra.split(" ");
					if (parts.length == 2) {
						String person = parts[0];
						String startPoint = parts[1];
						Starts.put(person, startPoint);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Starts;
	}

	public static Map<String, Integer> Assignments(Map<String, Integer> countrys, String file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String sıra;
			boolean readingCities = true;
			while ((sıra = reader.readLine()) != null) {
				if (sıra.trim().isEmpty()) {
					continue;
				}
				if (sıra.equals("Graf:")) {
					readingCities = false;
					continue;
				}
				if (readingCities) {
					String[] parts = sıra.split(" ");
					if (parts.length == 2) {
						String city = (parts[0].trim());
						int code = Integer.parseInt(parts[1].trim());
						countrys.put(city, code);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return countrys;
	}
}