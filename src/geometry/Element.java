package geometry;

public class Element  {
	//data classÊý¾ÝÀà
	/************** Private ***************/
	public int elementNum;
	public Point[] elementNudes = new Point[3];

	/************** Constructor ***********/
	//construct from element number and point[3]
		public Element(int elementNum, Point[] elementnodes) {
	this.elementNum = elementNum;
	elementNudes=elementnodes;
}
		public Element(int elementNum, Point node0, Point node1, Point node2) {
		this.elementNum = elementNum;
		elementNudes[0] = node0;
		elementNudes[1] = node1;
		elementNudes[2] = node2;
	}

	/************** InterfaceFunction **************/
		/************** Function *************/
}
