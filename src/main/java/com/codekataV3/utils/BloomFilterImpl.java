package com.codekataV3.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BloomFilterImpl implements BloomFilter{

	private byte[] set;

    @Value("${bf.keySize}")
	private int keySize;

    @Value("${bf.capacity}")
	private Integer setSize;
	
	private Integer size;

	private MessageDigest md;
	
    @Value("${file.dictionnary}")
    private String fileName;
	
	public BloomFilterImpl() {

		
	}
	
	public void setup() {
		set = new byte[setSize];

		size = 0;
		try
		{
			md = MessageDigest.getInstance("MD5");
		}

		catch (NoSuchAlgorithmException e)
		{
			throw new IllegalArgumentException("Error : MD5 Hash not found");
		}
        List<String> list = new ArrayList<>();
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.filter(s->s.length()<6).forEach(s->{this.add(s);});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void makeEmpty() {
		set = new byte[setSize];

		size = 0;

		try
		{

			md = MessageDigest.getInstance("MD5");

		}
		catch (NoSuchAlgorithmException e)
		{

			throw new IllegalArgumentException("Error : MD5 Hash not found");

		}
		
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int getSize() {
		return size;
	}

	private int getHash(int i)
	{
		md.reset();
		byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
		md.update(bytes, 0, bytes.length);
		return Math.abs(new BigInteger(1, md.digest()).intValue()) % (set.length - 1);

	}
	
	@Override
	public void add(Object obj) {
		int[] tmpset = getSetArray(obj);
		for (int i : tmpset)

			set[i] = 1;

		size++;
		
	}

	@Override
	public boolean contains(Object obj) {
		int[] tmpset = getSetArray(obj);

		for (int i : tmpset)

			if (set[i] != 1)

				return false;

		return true;

	}
	
	/* Function to get set array for an object */
	private int[] getSetArray(Object obj)
	{

		int[] tmpset = new int[keySize];

		tmpset[0] = getHash(obj.hashCode());

		for (int i = 1; i < keySize; i++)

			tmpset[i] = (getHash(tmpset[i - 1]));

		return tmpset;

	}

}
