import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class İşleme {
	private SimpleRAM ram = new SimpleRAM(16);
	private decoder3x8 decode = new decoder3x8();
	private decoder4x16 decode2 = new decoder4x16();
	private IR I = new IR();
	private SequenceCounter counter = new SequenceCounter();

	public İşleme(String file) {
		
		loadram(file, ram, decode, decode2);
	}
	
	public void loadram(String file, SimpleRAM ram, decoder3x8 decode, decoder4x16 decode2) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String sıra;
			int adres = 0;
			int time = 0;
			String buyruk = null;
			String denetim = null;
			while ((sıra = reader.readLine()) != null) {

				int data = Integer.parseInt(sıra, 2);
				decode.decode(ram.write(adres, data));
				I.loadbuyruk(decode.get_I());
				int x = ram.write(adres, data);
				String hex = Integer.toHexString(x);
				switch (hex.charAt(0)) {
				case '0':
					buyruk = "AND";
					denetim = "0";
					break;
				case '1':
					buyruk = "ADD";
					denetim = "1";
					break;
				case '2':
					buyruk = "LDA";
					denetim = "2";
					break;
				case '3':
					buyruk = "STA";
					denetim = "3";
					break;
				case '4':
					buyruk = "BUN";
					denetim = "4";
					break;
				case '5':
					buyruk = "BSA";
					denetim = "5";
					break;
				case '6':
					buyruk = "ISZ";
					denetim = "6";
					break;
				case '8':
					buyruk = "AND";
					denetim = "0";
					
					break;
				case '9':
					buyruk = "ADD";
					denetim = "1";
					break;
				case 'a':
					buyruk = "LDA";
					denetim = "2";
					break;
				case 'b':
					buyruk = "STA";
					denetim = "3";
					break;
				case 'c':
					buyruk = "BUN";
					denetim = "4";
					break;
				case 'd':
					buyruk = "BSA";
					denetim = "5";
					break;
				case 'e':
					buyruk = "ISZ";
					denetim = "6";
					break;
				case 'f':
					denetim = "7";
					switch (hex.charAt(1)) {
					case '8':
						buyruk = "INP";
						break;
					case '4':
						buyruk = "OUT";
						break;
					case '2':
						buyruk = "SKI";
						break;
					case '1':
						buyruk = "SKO";
						break;
					case '0':
						switch (hex.charAt(2)) {
						case '8':
							buyruk = "ION";
							break;
						case '4':
							buyruk = "IOF";
							break;

						default:
							break;
						}
						break;
					default:
						break;
					}
					break;
				case '7':
					denetim = "7";
					switch (hex.charAt(1)) {
					case '8':
						buyruk = "CLA";
						break;
					case '4':
						buyruk = "CLE";
						break;
					case '2':
						buyruk = "CMA";
						break;
					case '1':
						buyruk = "CME";
						break;
					case '0':
						switch (hex.charAt(2)) {
						case '8':
							buyruk = "CIR";

							break;
						case '4':
							buyruk = "CIL";

							break;
						case '2':
							buyruk = "INC";

							break;
						case '1':
							buyruk = "SPA";

							break;
						case '0':
							switch (hex.charAt(3)) {
							case '8':
								buyruk = "SNA";
								break;
							case '4':
								buyruk = "SZA";

								break;
							case '2':
								buyruk = "SZE";

								break;
							case '1':
								buyruk = "HLT";

								break;
							default:
								break;
							}
							break;

						default:
							break;
						}
						break;

					default:
						break;
					}

					break;

				default:
					break;
				}
				System.out.println("T" + time + " zamanda" + " I = " + I.getanlıkbuyruk() + " D " + denetim + " aktif "
						+ " IR(0-11) = " + decode.getadress() + " buyruk= " + buyruk);
				counter.increment();
				time++;
				adres++;
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

	}
}
