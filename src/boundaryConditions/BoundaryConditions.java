package boundaryConditions;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;

import Jama.*;
public class BoundaryConditions{
	/************** Private ***************/
        int stressNum;
        int constraintNum;
		Matrix stress;
		Matrix constraint;
		//Matrix solution;
		Matrix BNMstress;
	/************** Constructor ***********/
		public BoundaryConditions(int totalNodes,BufferedReader stressFile, BufferedReader constraintFile) {
			
			stress=new Matrix(totalNodes*2,1);
			constraint=new Matrix(totalNodes*2,1,1.0);
			try {
				// Constructor Nodes
				stressNum = Integer.parseInt(stressFile.readLine());
				// read nodes
				for (int i = 0; i <stressNum; i++) {
					String tempstr = stressFile.readLine();
					String[] str = tempstr.split(" ");
					stress.set((Integer.parseInt(str[0])-1)*2, 0, Double.parseDouble(str[1]));
					stress.set((Integer.parseInt(str[0])-1)*2+1, 0, Double.parseDouble(str[2]));
					
				}
				stressFile.close();
			   //stress.print(4, 2);
				constraintNum = Integer.parseInt(constraintFile.readLine());
				// read elements
				for (int i = 0; i < constraintNum; i++) {
					String tempstr = constraintFile.readLine();
					String[] str = tempstr.split(" ");
					//System.out.print(str[0]);
					constraint.set((Integer.parseInt(str[0])-1)*2, 0, Double.parseDouble(str[1]));
					constraint.set((Integer.parseInt(str[0])-1)*2+1, 0, Double.parseDouble(str[2]));

				}
				//constraintFile.close();
				//constraint.print(4, 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
     /************** Function *************/
		/************1Replacemethod to set boundaryCondition
		 *************** 1.1get Matrix by ReplaceMethond ********/
		public Matrix ReplaceMethod(Matrix globalMatrix) {
			for(int i=0;i<constraint.getRowDimension();i++) {
				if(constraint.get(i,0)==0.0) {
					globalMatrix.setMatrix(i,i,0,globalMatrix.getColumnDimension()-1,new Matrix(1,globalMatrix.getColumnDimension(),0));
					globalMatrix.setMatrix(0,globalMatrix.getColumnDimension()-1,i,i,new Matrix(globalMatrix.getColumnDimension(),1,0));
					globalMatrix.set(i, i,1 );
				}
					
			}
			//globalMatrix.print(4, 2);
			return globalMatrix;
		}
		/************1Replacemethond to set boundaryCondition
		 *************** 1.2solve Matrix by Gaussian elimination and print********/
		public void ReplaceMethodSolveOnBC(Matrix globalMatrix) {
			//LUDecomposition LU= new LUDecomposition(globalMatrix);
			globalMatrix.solve(stress).copy().print(new DecimalFormat("#.#E0#") , 8);
			
		}
		/************2Setting big number method to set boundaryCondition
		 ***************2.1get Matrix by ReplaceMethond ********/
		
		public Matrix GetBGNBNMstress(Matrix globalMatrix) {
			BNMstress= stress.copy();
			for(int i=0;i<constraint.getRowDimension();i++) {
				if(constraint.get(i,0)==0.0)
					BNMstress.set(i, 0,globalMatrix.get(i, i)*constraint.get(i,0)*1e9 );				
			}
			//BNMstress.print(8, 4);
			return BNMstress;
		}
		/************2Setting big number method(BNM) to set boundaryCondition
		 ***************2.2get globalmatrix by BNM ********/
		public Matrix GetBGNGlobalMatrix(Matrix globalMatrix) {
			
			for(int i=0;i<constraint.getRowDimension();i++) {
				if(constraint.get(i,0)==0.0)
					globalMatrix.set(i, i,globalMatrix.get(i, i)*1e9 );				
			}
			//globalMatrix.print(4, 2);
			return globalMatrix;
		}
		/************2Setting big number method to set boundaryCondition
		 ***************2.3solve Matrix by Gaussian elimination and print********/
		public void BNMSolveOnBC(Matrix globalMatrix) {
					globalMatrix.solve(BNMstress).copy().print(new DecimalFormat("#.#E0#") , 8);
			
		}
		
}
