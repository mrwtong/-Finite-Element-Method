package globalMatrix;

import java.text.DecimalFormat;
import Jama.*;
import elementMatrix.*;
import geometry.*;

public class GMatrix  implements SetProperties {
	/************** Private ***************/
	Geometry geometry;
	Matrix globalMatrix;
	EMatrix[] eMatrixs;
	/************** Constructor ***********/
	// default constructor
	public GMatrix() {

	}
	// construct from geometry
	public GMatrix(Geometry geometry) {

		this.geometry = geometry;
		eMatrixs = new EMatrix[geometry.GetTotalElementNum()];
		globalMatrix = new Matrix(geometry.GetTotalNodeNum() * 2, geometry.GetTotalNodeNum() * 2);

	}
	/************** InterfaceFunction **************/
	// explicit set mechanical parameter
	public void SetMechanicalPara(double E, double nu, double t) {

		for (int i = 0; i < geometry.GetTotalElementNum(); i++) {
			eMatrixs[i].SetMechanicalPara(E, nu, t);
			
		}
	}

	/************** Function *************/
	// build all element matrixes from elements
	public void BuildEMatrix(double E, double nu, double t) {
		
		for (int i = 0; i < geometry.GetTotalElementNum(); i++) {
			eMatrixs[i] = new EMatrix(geometry.GetElement(i));
			eMatrixs[i].SetMechanicalPara(E, nu, t);
			eMatrixs[i].ShapeFunction();
			eMatrixs[i].EStiffnessMatrix();
			//eMatrixs[i].Print();

		}
	};

	// Subfunction of BuildGMatrix for recursive call
	public Matrix PlusSubTwoMatrix(int elementnum, int i, int j) {

		return globalMatrix
				.getMatrix(eMatrixs[elementnum].GetGlobalNu(i / 2) * 2, eMatrixs[elementnum].GetGlobalNu(i / 2) * 2 + 1,
						eMatrixs[elementnum].GetGlobalNu(j / 2) * 2, eMatrixs[elementnum].GetGlobalNu(j / 2) * 2 + 1)
				.plus(eMatrixs[elementnum].GetSubMatrix(i, j));
	}

	public void BuildGMatrix() {
		for (int elementnum = 0; elementnum < geometry.GetTotalElementNum(); elementnum++) {
			for (int i = 0; i < 6; i += 2) {
				for (int j = i; j < 6; j += 2) {
					if (i == j)
						globalMatrix.setMatrix(eMatrixs[elementnum].GetGlobalNu(i / 2) * 2,
								eMatrixs[elementnum].GetGlobalNu(i / 2) * 2 + 1,
								eMatrixs[elementnum].GetGlobalNu(j / 2) * 2,
								eMatrixs[elementnum].GetGlobalNu(j / 2) * 2 + 1, PlusSubTwoMatrix(elementnum, i, j));
					if (i != j) {
						globalMatrix.setMatrix(eMatrixs[elementnum].GetGlobalNu(i / 2) * 2,
								eMatrixs[elementnum].GetGlobalNu(i / 2) * 2 + 1,
								eMatrixs[elementnum].GetGlobalNu(j / 2) * 2,
								eMatrixs[elementnum].GetGlobalNu(j / 2) * 2 + 1, PlusSubTwoMatrix(elementnum, i, j));
						globalMatrix.setMatrix(eMatrixs[elementnum].GetGlobalNu(j / 2) * 2,
								eMatrixs[elementnum].GetGlobalNu(j / 2) * 2 + 1,
								eMatrixs[elementnum].GetGlobalNu(i / 2) * 2,
								eMatrixs[elementnum].GetGlobalNu(i / 2) * 2 + 1, PlusSubTwoMatrix(elementnum, j, i));

					}

				}
			}
		}

			

		}
	public void Print() {
		
		globalMatrix.print(new DecimalFormat("#.#E0#") , 8);
		//globalMatrix.print(4 , 2);
	}
	public int GetTotalNodes() {
		return geometry.GetTotalNodeNum();
	}
	public Matrix GetGlobalMatrix() {
		 return globalMatrix;
	}

}
