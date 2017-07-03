package com.mars.examples.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ObjectCrypter {
	private Cipher deCipher;
	private Cipher enCipher;
	private SecretKeySpec key;
	private IvParameterSpec ivSpec;

	public ObjectCrypter(byte[] keyBytes, byte[] ivBytes) {
		// wrap key data in Key/IV specs to pass to cipher

		ivSpec = new IvParameterSpec(ivBytes);
		// create the cipher with the algorithm you choose
		// see javadoc for Cipher class for more info, e.g.
		try {
			DESKeySpec dkey = new DESKeySpec(keyBytes);
			key = new SecretKeySpec(dkey.getKey(), "DES");
			deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	private Object convertFromByteArray(byte[] byteObject) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(byteObject);
		ObjectInputStream in = new ObjectInputStream(bais);
		Object o = in.readObject();
		in.close();
		return o;
	}

	private byte[] convertToByteArray(Object complexObject) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);

		out.writeObject(complexObject);

		out.close();
		return baos.toByteArray();
	}

	public Object decrypt(byte[] encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException {
		deCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

		return convertFromByteArray(deCipher.doFinal(encrypted));
	}

	public byte[] encrypt(Object obj) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException,
			IllegalBlockSizeException, ShortBufferException, BadPaddingException {
		byte[] input = convertToByteArray(obj);
		enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

		return enCipher.doFinal(input);
		// cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		// byte[] encypted = new byte[cipher.getOutputSize(input.length)];
		// int enc_len = cipher.update(input, 0, input.length, encypted, 0);
		// enc_len += cipher.doFinal(encypted, enc_len);
		// return encypted;
	}
}