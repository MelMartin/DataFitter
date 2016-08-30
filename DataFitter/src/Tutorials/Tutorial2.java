package Tutorials;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import Tools.ExpectationMaximization1D;
import Tools.KMeans;
import jMEF.*;

public class Tutorial2 {


	/**
	 * Main function.
	 * @param args
	 */
	public Tutorial2 () {

		// Display
		String title = "";
		title += "+----------------------------------------+\n";
		title += "| Bregman soft clustering & classical EM |\n";
		title += "+----------------------------------------+\n";
		System.out.print(title);

		// Variables
		int n = 3;//number of mixtures
		int m = 1000;//number of points to be drawn from the model
		
		// Initial mixture model
		MixtureModel mm = new MixtureModel(n);
		mm.EF = new UnivariateGaussian();//EF Exponential Family (here=gaussian)
		for (int i=0; i<n; i++){
			PVector param  = new PVector(2);//vector with dimension 2
			param.array[0] = 10 * (i+1);
			param.array[1] = 2  * (i+1);
			mm.param[i]    = param;
			mm.weight[i]   = i+1;
		}

		mm.normalizeWeights();
		System.out.println("Initial mixure model \n" + mm + "\n");
		
		// Draw points from initial mixture model and compute the n clusters
		//PVector[]         points   = mm.drawRandomPoints(m);
		PVector[]         points   = readDataFromFile("C:\\Users\\Mel\\Documents\\BIOINFORMATICS\\DELFT_Research\\Data\\SimulatedReads\\PloEst\\PloEst200\\points.txt");

		Vector<PVector>[] clusters = KMeans.run(points, n);
		//ADAPTED FOR MY DATA
		/*
		for (int p=0;p<points.length;p++){
			System.out.print(" "+points[p].toString());

		}
		System.out.println();
		//*/
		// Classical EM
		MixtureModel mmc;
		mmc = ExpectationMaximization1D.initialize(clusters);
		mmc = ExpectationMaximization1D.run(points, mmc);
		System.out.println("Mixure model estimated using classical EM \n" + mmc + "\n");
		
		// Bregman soft clustering
		MixtureModel mmef;
		mmef = BregmanSoftClustering.initialize(clusters, new UnivariateGaussian());
		mmef = BregmanSoftClustering.run(points, mmef);
		System.out.println("Mixure model estimated using Bregman soft clustering \n" + mmef + "\n");

	}
	
	public PVector[] readDataFromFile(String filePath){
		
	    File file = new File(filePath); 
	    PVector[] result=null;
	    try {
	        Scanner scanner = new Scanner(file); 

	        scanner.nextLine(); // to ignore the first line which has the header

	       
	        result=new PVector[7269];
	        int ind=0;
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            if (ind>7250)System.out.println("line:"+line+" ind:"+ind);
	            // Do something with these values
	            PVector curVec=new PVector(1);
	            curVec.array[0]=Double.parseDouble(line);
	            result[ind++]=curVec;
	         
	        }
	        scanner.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	
	
}
