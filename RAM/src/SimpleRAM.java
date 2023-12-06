public class SimpleRAM {
	private int[] hafıza;

	public SimpleRAM(int byte_boyutu) {
		hafıza = new int[byte_boyutu];
	}

	public int write(int adres, int data) {
		if (adres >= 0 && adres < hafıza.length) {
			hafıza[adres] = data;
			return data;
		} else {
			return data;
		}

	}

	private String toBinary(int value) {
		return String.format(Integer.toBinaryString(value));
	}

	public int read(int adres) {
		if (adres >= 0 && adres < hafıza.length) {
			System.out.println(hafıza[adres] + " " + adres);
			return hafıza[adres];
		} else {
			System.out.println(adres);
			return 0;
		}
	}

}