package de.buw.fm4se;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprList;
import edu.mit.csail.sdg.ast.Module;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.translator.A4Options.SatSolver;

public final class AlloyAnalyzer {

	private static A4Reporter rep;
	private static A4Options opt;
	private Module world;
	private String address;
	private List<String> redundantFactsStringList;
	private List<Expr> redundantFactsExprList;
	
	public AlloyAnalyzer (String fileName) {
		
		rep = A4Reporter.NOP;
		opt = new A4Options();
		opt.solver = SatSolver.SAT4J;
		
		this.redundantFactsStringList= new ArrayList<>(); //list of redundant facts in the alloy string format 
		this.redundantFactsExprList= new ArrayList<>();	//list of redundant facts in the expression format 	
		this.address=fileName;
		
		this.world=CompUtil.parseEverything_fromFile(rep, null, fileName);
	}


    /* method : findRedundantfacts 
     * description: list of redundant facts in two format (redundantFactsStringList, redundantFactsExprList)are created here. 
     */	
	public void findRedundantfacts () throws IOException {
		Command cmd= this.world.getAllCommands().get(0);
		ExprList formula = (ExprList)cmd.formula;
		ConstList originalArgs = formula.args;
		ConstList.TempList finalList = new ConstList.TempList<Expr>(originalArgs);

		int i = 0;
		while (i < finalList.size()) {
			ConstList.TempList tempList = new ConstList.TempList<Expr>(finalList.makeConst());
			finalList = new ConstList.TempList<Expr>(finalList.makeConst());
			Expr tmpRemoveFact = (Expr) tempList.get(i);
			tempList.remove(i);
			ExprList newFomular = ExprList.make(formula.pos, formula.closingBracket, formula.op, tempList.makeConst());
			Command tempCmd = cmd.change(newFomular);

			Expr nF = tempCmd.formula.implies(tmpRemoveFact).not();
			Command newCmd = tempCmd.change(nF);
			A4Solution tmpAns = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), newCmd, opt);

			if (!tmpAns.satisfiable()) {
				redundantFactsExprList.add(tmpRemoveFact);
				String RedundantFactString = getFactString(tmpRemoveFact,this.address);
				redundantFactsStringList.add(RedundantFactString);
				finalList.remove(i);
				continue;
			}

			i++;
		}

	}
	
    /* method : getRedundantFactsString 
     * output : redundantFactsStringList (List<String>)
     * description: return list of redundant facts in the alloy string format 
     */
	public List<String> getRedundantFactsString () {
		return this.redundantFactsStringList;
	}

    /* method : getRedundantFactsString
     * output : redundantFactsExprList (List<Expr>) 
     * description: return list of redundant facts in the expression format 
     */
	public List<Expr> getRedundantFactsExpr () {
		return this.redundantFactsExprList;
	}
	
    /* method : writeNewAlloy 
     * description: write new alloy file without redundant facts
     * new alloy model will be saved in "outputAlloy.als"
     */
	public void writeNewAlloy () throws IOException {
		List <Integer> resundantFactsLines=  getFactsLines (this.redundantFactsExprList);// getting redundant facts lines
		createNewAlloyfile(resundantFactsLines,this.address);
	}

	//helper methods
	
    /* method : getFactString 
     * inputs: 
     * 	factExpression (Expr): the facts Expression ; fileName (String) : input file address
     * output: 
     * 	fact (String): containing the lines of input  which the fact is written in
     * description: the method save the String lines of given expression based on
     *  its position in the input file 
     */	
	private static String getFactString(Expr factExpression, String fileName) throws IOException {
		  BufferedReader reader = new BufferedReader(new FileReader(fileName));
		  int pos_start=factExpression.pos.y;
		  int pos_end=factExpression.pos.y2;
		  String fact =""; //output variable
		  int i=1;
		  while (i<=pos_end) {
			  String line = reader.readLine().trim();
			  if (i>=pos_start) {
				  if (i==pos_start) {
					  fact=line;
				  } else {
					  fact=fact + '\n' + line;
				  }
			  }
			  i++;
		  }
	    return fact;
	  }

    /* method : getFactsLines 
     * input: 
     * 	ExprList (List<Expr>): the list of all facts Expression
     * output: 
     * 	redundantFactsLines (List <Integer>): list of line numbers in the input file 
     * 	which the given Expressions are located in
     * description: the method create redundantFactsLines list
     */
	private static List <Integer> getFactsLines(List<Expr> ExprList) {
		  List <Integer> redundantFactsLines= new ArrayList<>();
		  for (Expr f:ExprList) {
			  int pos_start=f.pos.y;
			  int pos_end=f.pos.y2;
			  for (int i=pos_start; i<=pos_end; i++) {
				  redundantFactsLines.add(i);
			  }
		  }

	    return redundantFactsLines;
	  }

    /* method : createNewAlloyfile 
     * input: 
     * redundantFactsLines (List<Integer>): the list of line numbers which we want to remove from input file 
     *  ; fileName (String) : input file address
     * output: void
     * description: write new alloy file without desired line numbers 
     * 	new alloy model will be saved in "outputAlloy.als"
     */
	private static void createNewAlloyfile(List<Integer> redundantFactsLines, String inputAlloyFile) throws IOException {
	        File outputAlloyFile = new File("src/main/resources/outputAlloy.als");

	        BufferedReader reader = new BufferedReader(new FileReader(inputAlloyFile));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(outputAlloyFile));
	        String currentLine;
	        int i=1;
	        while ((currentLine = reader.readLine()) != null) {
	            if (!redundantFactsLines.contains(i)) {
	                writer.write(currentLine);
	                writer.newLine();
	            }
	            i++;
	        }

	        reader.close();
	        writer.close();
	  }
}
