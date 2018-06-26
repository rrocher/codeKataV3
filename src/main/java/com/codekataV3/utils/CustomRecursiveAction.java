package com.codekataV3.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codekataV3.service.WordProcessor;


public class CustomRecursiveAction extends RecursiveAction{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> workload ;
    private static final int THRESHOLD = 100;
    ConcurrentHashMap<String, List<String>> mymap;
    private WordProcessor wordProcessor;

	private static final Logger logger = LoggerFactory.getLogger(CustomRecursiveAction.class);
 
    public CustomRecursiveAction(WordProcessor wordProcessor, List<String> workload, ConcurrentHashMap<String, List<String>> map) {
        this.workload = workload;
        this.mymap = map;
        this.wordProcessor = wordProcessor;
    }
 
	@Override
	protected void compute() {
        if (workload.size() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
           processing(workload);
        }
		
	}
	private List<CustomRecursiveAction> createSubtasks() {
        List<CustomRecursiveAction> subtasks = new ArrayList<>();
 
        List<String> partOne = workload.subList(0, workload.size() / 2);
        List<String> partTwo = workload.subList(workload.size() / 2, workload.size());
 
        subtasks.add(new CustomRecursiveAction(wordProcessor, partOne, mymap));
        subtasks.add(new CustomRecursiveAction(wordProcessor, partTwo, mymap));
 
        return subtasks;
    }
 
    private void processing(List<String> work) {
    	work.parallelStream().forEach(s -> {

        	List<String> myset = this.wordProcessor.checkComposedString(s);
			if (myset != null) {
    			mymap.put(s, myset);
			}

        });
    }

}
