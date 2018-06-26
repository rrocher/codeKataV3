package com.codekataV3.utils;

public interface BloomFilter {

	public void makeEmpty();

	public boolean isEmpty();

	public int getSize();

	public void add(Object obj);
	
	public boolean contains(Object obj);
	
	public void setup();
}
