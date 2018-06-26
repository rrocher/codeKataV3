package com.codekataV3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.codekataV3.service.WordProcessor;

/**
 *The third version of code kata will use spring and multi threading 
 * to make the code more extendable
 */
@SpringBootApplication
public class CodekataV3Application implements ApplicationRunner{

	private static final Logger logger = LoggerFactory.getLogger(CodekataV3Application.class);

    @Value("${file.to.process}")
    private String fileName;
	
	public static void main(String[] args) {
		SpringApplication.run(CodekataV3Application.class, args);
	}


	@Autowired
	private WordProcessor wordProcessor;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

        List<String> list = new ArrayList<>();
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			 list = stream
	                    .filter(line -> line.length() == 6)
	                    .collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, List<String>> myMap = wordProcessor.process(list);
		
		myMap.forEach((key, value)
				-> {logger.info("{} + {} = {}", value.get(0), value.get(1), key);});
		
	}
}
