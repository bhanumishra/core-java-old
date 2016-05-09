package me.utils;

public class StringUtils {
	public static byte[] xor(final byte[] input, final byte[] secret) {
		final byte[] output = new byte[input.length];
		if (secret.length == 0) {
			throw new IllegalArgumentException("Empty security key");
		}
		int spos = 0;
		for (int pos = 0; pos < input.length; ++pos) {
			output[pos] = (byte) (input[pos] ^ secret[spos]);
			++spos;
			if (spos >= secret.length) {
				spos = 0;
			}
		}
		return output;
	}
}
