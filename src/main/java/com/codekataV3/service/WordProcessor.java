package com.codekataV3.service;

import java.util.List;
import java.util.Map;

import com.codekataV3.utils.BloomFilter;

public interface WordProcessor {

	public void setBloomFilterSpeller(BloomFilter bloomFilter);
	
	public boolean areValid(String s1, String s2);
	
	public List<String> checkComposedString(String s);
	
	public Map<String, List<String>> process(List<String> list);
}
