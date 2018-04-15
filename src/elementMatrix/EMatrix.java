package elementMatrix;

import Jama.*;
import geometry.*;

public class EMatrix implements SetProperties {

	/************** Private ***************/
	static int dimension = 2;
	int eMatrixNum = -1;
	int nodeNu = 3;
	int[] nuRecord = new int[3];
	double nu, E, t;
	Point[] eNodes = new Point[nodeNu];
	double[] a = new double[nodeNu];
	double[] b = new double[nodeNu];
	double[] c = new double[nodeNu];
	Matrix shapeMatrix = new Matrix(nodeNu, nodeNu, 1.0);
	Matrix subKEMatrix = new Matrix(2, 2);
	Matrix kEMatrix = new Matrix(nodeNu * 2, nodeNu * 2);
	double A = 0;

	/************** Constructor ***********/
	//set element matrix dimension and node number
	public EMatrix(int dime, int node) {
		dimension = dime;
		nodeNu = node;
	}
//construct  element matrix from a element
	public EMatrix(Element oneElement) {
		this.eMatrixNum = oneElement.elementNum;
		for (int i = 0; i < 3; i++) {
			nuRecord[i] = oneElement.elementNudes[i].pNum;
			eNodes[i] = oneElement.elementNudes[i];
		}

	}

	// ************** InterfaceFunction **************//
	//set default mechanical parameter
	public void SetMechanicalPara() {

		this.E = 1e6;
		this.nu = 0.01;
		this.t = 1;
		 System.out.print("WARNING Use Default Mechanical Parameter");
	}
	//set mechanical parameter
	public void SetMechanicalPara(double E, double nu, double t ) {
		this.E = E;
		this.nu = nu;
		this.t=t;
		//this.E = E;
		//this.nu = nu;
		//this.t=t;
	}

	/************** Function *************/
	// get ShapeFunction in shapeMatrix
	public void ShapeFunction() {
		for (int i = 0, j = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				/* if (j!=0 && i < 3 && j < 2) */
				if (j > 0 && j < 2) {

					shapeMatrix.set(i, j, eNodes[i].x);
					shapeMatrix.set(i, j + 1, eNodes[i].y);

				}
				// System.out.print(shapeMatrix.get(i, j)+" ");

			}

			// System.out.println("/");
		}
		// shapeMatrix.print(2, 1);
		//subscript rotate
		A = shapeMatrix.det()*0.5;
		for (int i = 0; i < 3; i++) {
			int j = i + 1;
			int k = i + 2;
			if (j == 3)
				j = 0;
			if (j == 4)
				j = 1;
			if (k == 3)
				k = 0;
			if (k == 4)
				k = 1;
			a[i] = eNodes[j].x * eNodes[k].y - eNodes[k].x * eNodes[j].y;
			b[i] = eNodes[j].y - eNodes[k].y;
			c[i] = -eNodes[j].x + eNodes[k].x;

		}

	}
    // get SubEStiffnessMatrix[i,j]
	public Matrix SubEStiffnessMatrix(int r, int s) {
		subKEMatrix.set(0, 0, b[r] * b[s] + ((1 - nu) / 2) * c[r] * c[s]);
		subKEMatrix.set(0, 1, nu * b[r] * c[s] + ((1 - nu) / 2) * c[r] * b[s]);
		subKEMatrix.set(1, 0, nu * c[r] * b[s] + ((1 - nu) / 2) * b[r] * c[s]);
		subKEMatrix.set(1, 1, c[r] * c[s] + ((1 - nu) / 2) * b[r] * b[s]);
		return subKEMatrix;

	}
	  //put SubEStiffnessMatrix into EStiffnessMatrix
	public void EStiffnessMatrix() {
		for (int i = 0, j = 0; i < 6; i += 2) {
			for (j = i; j < 6; j += 2) {
				if (i == j)
					kEMatrix.setMatrix(i, i + 1, j, j + 1, SubEStiffnessMatrix(i / 2, j / 2));
				if (i != j) {
					kEMatrix.setMatrix(i, i + 1, j, j + 1, SubEStiffnessMatrix(i / 2, j / 2));
					kEMatrix.setMatrix(j, j + 1, i, i + 1, SubEStiffnessMatrix(j / 2, i / 2));
				}
			}
		}
		kEMatrix.timesEquals(E*t/(4*A*(1-nu*nu)));
	}
    // Print
	public void Print() {
		kEMatrix.print(3, 2);

	}
    // get InternalNumber from GlobalNumber
	public int GetInternalNu(int n) {
		int i = 0;
		for (; i < 3; i++)
			if (nuRecord[i] == n)
				break;
		return i;
	}
	// get GlobalNumber from InternalNumber 
	public int GetGlobalNu(int i) {
		return nuRecord[i];
	}
	//get subEMatrix
	public Matrix GetSubMatrix(int i, int j) {
		return kEMatrix.getMatrix(i, i + 1, j, j + 1);
	}

}
