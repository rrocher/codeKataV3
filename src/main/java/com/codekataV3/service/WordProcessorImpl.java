package com.codekataV3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codekataV3.CodekataV3Application;
import com.codekataV3.utils.BloomFilter;
import com.codekataV3.utils.CustomRecursiveAction;

@Service
public class WordProcessorImpl implements WordProcessor{

	private static final Logger logger = LoggerFactory.getLogger(WordProcessorImpl.class);
	
	@Autowired
	private BloomFilter bloomFilter;
	
	
	public WordProcessorImpl() {
	}

	@Override
	public void setBloomFilterSpeller(BloomFilter bFilter) {
		bloomFilter = bFilter;
		
	}

	@Override
	public boolean areValid(String s1, String s2) {
		if (bloomFilter.contains(s1) && bloomFilter.contains(s2))
			return true;
		return false;
	}

	@Override
	public List<String> checkComposedString(String s) {

		List<String> set = new ArrayList<>();
		for (int x=1; x< s.length()-2;x++) {
			String s1 = s.substring(0, x+1);
			String s2 = s.substring(x+1);

			if (true == areValid(s1, s2)) {
				set.add(s1);
				set.add(s2);
				return set;
			}
		}
		return null;
	}

	@Override
	public Map<String, List<String>> process(List<String> list) {
		logger.info("Processing list of words");
		bloomFilter.setup();
		ConcurrentHashMap<String, List<String>> mymap = new ConcurrentHashMap<>();
		ForkJoinPool commonPool = ForkJoinPool.commonPool();

		ForkJoinTask<?> customRecursiveTask = new CustomRecursiveAction(this, list, mymap);
		logger.info("Forking the process of list of words");
		commonPool.execute(customRecursiveTask );
		logger.info("Processing list of words done");
		return mymap;
	}

}
