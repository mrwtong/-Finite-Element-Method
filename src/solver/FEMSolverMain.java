package solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import geometry.Geometry;
import globalMatrix.GMatrix;
import boundaryConditions.BoundaryConditions;

public  class  FEMSolverMain {

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub
		
		BufferedReader elementsFileRead = new BufferedReader(
				new FileReader(args[0]+"/elements.txt"));
		BufferedReader nodesFileRead = new BufferedReader(
				new FileReader(args[0]+"/nodes.txt"));//read nodes and element files
		Geometry testGeo=new Geometry(nodesFileRead,elementsFileRead);//build geometry by char stream
		GMatrix globalMatrix=new GMatrix(testGeo);//build global matrix from geometry
		BufferedReader stressFile = new BufferedReader(
				new FileReader(args[0]+"/stress.txt"));
		BufferedReader constraintFile = new BufferedReader(
				new FileReader(args[0]+"/constraint.txt"));//read stress and constraint files
		BoundaryConditions bounCond=new BoundaryConditions(globalMatrix.GetTotalNodes(),stressFile,constraintFile);//build GetGeometry by char stream
		elementsFileRead.close();
		elementsFileRead.close();
		stressFile.close();
		constraintFile.close();
		globalMatrix.BuildEMatrix(1,1/6,1);	//build element matrixes in global matrix from E, nu and tickness
		globalMatrix.BuildGMatrix();//build global matrix from element matrixes
		globalMatrix.Print();
		bounCond.ReplaceMethod(globalMatrix.GetGlobalMatrix());//set boundary condition by ReplaceMethod
		bounCond.ReplaceMethodSolveOnBC(bounCond.GetBGNGlobalMatrix(globalMatrix.GetGlobalMatrix()));// solve matrix
		bounCond.GetBGNBNMstress(globalMatrix.GetGlobalMatrix());//set boundary condition by setting big number
		bounCond.BNMSolveOnBC(bounCond.GetBGNGlobalMatrix(globalMatrix.GetGlobalMatrix()));//solve matrix and print
	    
		
		
	

	}
}
