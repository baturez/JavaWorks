public class decoder4x16 {
	private boolean[] çıktılar;

	public decoder4x16() {
		çıktılar = new boolean[16];
	}

	public void decode(int girdi) {
		String giriş = String.valueOf(girdi);
		if (isValidInput(giriş.length())) {
			çıktı_sıfırla();
			çıktılar[giriş.length()] = true;
			System.out.println(girdi + " to binary: " + toBinaryString(girdi));
		} else {
			System.out.println(girdi);
		}
	}

	private boolean isValidInput(int input) {
		return input >= 0 && input < 16;
	}

	private void çıktı_sıfırla() {
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

	private String toBinaryString(int value) {
		return String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0');
	}
}