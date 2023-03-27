package de.buw.fm4se;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


public class AlloyAnalyzerRedundantTest {
	long startTime = System.currentTimeMillis();
    @Test //no redundant fact
    public void testDreadbury() throws IOException {    	
    	
		String fileName = "src/main/resources/dreadbury.als";
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list  = model.getRedundantFactsString();
		assertTrue(list.isEmpty()); //list should be empty
    }
    
    @Test //there is one redundant fact
    public void testDreadbury2() throws IOException {  
    	
		String fileName = "src/main/resources/dreadbury2.als";
        List<String> list1 = Arrays.asList("fact someoneKilledAgatha {\n"
        		+ "some killed.Agatha\n"
        		+ "}");
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list2  = model.getRedundantFactsString();
		assertEquals(list1,list2);        
    }
    
    @Test
    public void testDreadbury3() throws IOException {    	
    	
		String fileName = "src/main/resources/dreadbury3.als";
        List<String> list1 = Arrays.asList("Agatha in Person.killed");
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list2  = model.getRedundantFactsString();
		assertEquals(list1,list2);
    }
    
    @Test
    public void productPrice() throws IOException {    	
    	
		String fileName = "src/main/resources/productPrice.als";
        List<String> list1 = Arrays.asList("fact Fact2 {\n"
        		+ "all p: Product | (p.price - p.discount) >= 0\n"
        		+ "}");
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list2  = model.getRedundantFactsString();
		assertEquals(list1,list2);
    }
    
    @Test 
    public void houses() throws IOException {    	
    	
		String fileName = "src/main/resources/houses.als";
        List<String> list1 = Arrays.asList("Major in House.major","H3.major = Phil");
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list2  = model.getRedundantFactsString();
		assertEquals(list1,list2); 
    }
    
    @Test 
    public void car() throws IOException {    	
    	
		String fileName = "src/main/resources/car.als";
        List<String> list1 = Arrays.asList("fact OwnerIsPerson {\n"
        		+ "all c: Car | c.owner in Person\n"
        		+ "}","fact PersonIsPerson {\n"
        				+ "all p: Person | p in Person\n"
        				+ "}");
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list2  = model.getRedundantFactsString();
		assertEquals(list1,list2); 
    }
    
    @Test //there is one redundant fact
    public void FileSystemModel() throws IOException {  
    	
		String fileName = "src/main/resources/FileSystemModel.als";
        List<String> list1 = Arrays.asList("fact { File + Dir = FSObject }");
		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts();
		List<String> list2  = model.getRedundantFactsString();
		assertEquals(list1,list2);        
    }
    

}
