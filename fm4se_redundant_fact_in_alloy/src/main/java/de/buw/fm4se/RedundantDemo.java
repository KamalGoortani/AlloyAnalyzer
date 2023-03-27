package de.buw.fm4se;

import java.io.IOException;
import java.util.List;

import edu.mit.csail.sdg.ast.Expr;

public class RedundantDemo {

	public static void main(String[] args) throws IOException {

		// input alloy model
//		String fileName = "src/main/resources/dreadbury.als"; //no redundant fact
//		String fileName = "src/main/resources/dreadbury2.als"; //one redundant fact
//		String fileName = "src/main/resources/dreadbury3.als"; //one redundant fact
		String fileName = "src/main/resources/productPrice.als"; //one redundant fact
//		String fileName = "src/main/resources/houses.als";    //two redundant fact
//		String fileName = "src/main/resources/car.als"; //all facts are redundant
//		String fileName = "src/main/resources/FileSystemModel.als"; //one redundant fact


		AlloyAnalyzer model = new AlloyAnalyzer(fileName); //creating AlloyAnalyzer object
		model.findRedundantfacts(); //finding redundant facts in model

		// Getting list of redundant facts in the Expression format
		List<Expr> redundantFactsExprList=model.getRedundantFactsExpr();
		System.out.println("Redundant facts in Expression format:");
		System.out.println(redundantFactsExprList);
		System.out.println();
		// Getting list of redundant facts in the alloy string format
		List<String> redundantFactsStringList=model.getRedundantFactsString();
		System.out.println("Redundant facts in alloy string format:");
		System.out.println(redundantFactsStringList);

		// writing new alloy file without redundant facts
		// new alloy model will be saved in "outputAlloy.als"
		model.writeNewAlloy();
	}

}
