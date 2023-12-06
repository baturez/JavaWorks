import java.util.Iterator;

public class decoder3x8 {

	public boolean[] çıktılar;
	public String I;
	String bina;
	StringBuilder opcode;
	public decoder3x8() {
		çıktılar = new boolean[8];
	}

	public void decode(int girdi) {
		String giriş = String.valueOf(girdi);

		if (giriş.length() >= 0 && giriş.length() < 8) {
			çıktı_sıfırla();
			çıktılar[giriş.length()] = true;
			bina = tobinary(girdi);
			int a = bina.length();
			

			StringBuilder strbr = new StringBuilder(bina);
			while(a != 16) {
				strbr.insert(0, "0");
				a++;
			}
			opcode = strbr;
			
			I = String.valueOf(strbr.charAt(0));
			for (int i = 0; i < bina.length(); i++) {
				if (bina.length() > 12) {

					strbr.deleteCharAt(0);
					bina = strbr.toString();
				} else {
					break;
				}
			}
			
			
		} else
			System.out.println(girdi);
	}
	public String get_I() {
		return I;
	}
	public String getadress() {
		return bina;
	}
	public StringBuilder opcode() {
		return opcode;

	}

	public void çıktı_sıfırla() {
		for (int i = 0; i < çıktılar.length; i++) {
			çıktılar[i] = false;
		}
	}

	public void printOutputs() {
		System.out.print("Decoder çıktısı: ");
		for (boolean output : çıktılar) {
			System.out.print(output ? "1 " : "0 ");
		}
		System.out.println();
	}

	private String tobinary(int value) {
		return String.format(Integer.toBinaryString(value));

	}
}