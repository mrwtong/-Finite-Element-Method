package geometry;

import java.io.*;

public class Geometry {
	//geometry class 几何网格类
	/************** Private ***************/
	int totalNodeNum;
	int totalElementNum;
	Point[] nodes;
	Element[] elements;
	/************** Constructor ***********/
	//construct from 2 file string streams（points file  and elements file） 
	public Geometry(BufferedReader nodesFile, BufferedReader elementsFile) {

		try {
			// Constructor Nodes
			totalNodeNum = Integer.parseInt(nodesFile.readLine());
			nodes = new Point[totalNodeNum];
			// read nodes
			for (int i = 0; i < totalNodeNum; i++) {
				String tempstr = nodesFile.readLine();
				String[] str = tempstr.split(" ");
				nodes[i] = new Point(Double.parseDouble(str[1]), Double.parseDouble(str[2]), (Integer.parseInt(str[0]))-1);//get cN
			}
			nodesFile.close();
			//System.out.print(nodes[3].pNum);
			// Constructor Elements
			totalElementNum = Integer.parseInt(elementsFile.readLine());
			elements = new Element[totalElementNum];
			// read elements
			for (int i = 0; i < totalElementNum; i++) {
				String tempstr = elementsFile.readLine();
				String[] str = tempstr.split(" ");
				//System.out.print(str[0]);
				elements[i] = new Element(Integer.parseInt(str[0])-1, nodes[Integer.parseInt(str[1])-1], nodes[Integer.parseInt(str[2])-1],
						nodes[Integer.parseInt(str[3])-1]);//get cN

			}
			elementsFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/************** InterfaceFunction **************/
	/************** Function *************/
	//return totalNodeNum
	public int GetTotalNodeNum() {
		return totalNodeNum;
	}
    //return totalElementNum
	public int GetTotalElementNum() {
		return totalElementNum;
	}
    //return element[i]
	public Element GetElement(int i) {
		return elements[i];

	}

	
	
}
