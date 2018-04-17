package geometry;
//data classÊý¾ÝÀà
public class Point {
	/************** Public Data ***************/
	public double x;
	public double y;
	public int pNum;
	/************** Constructor ***********/
	//default constructor
	public Point() {
		x = 0.0;
		y = 0.0;
		pNum = 0;
	}

	//construct from x£¬y and point number
	public Point(double x, double y, int pNum) {
		this.x = x;
		this.y = y;
		this.pNum = pNum;
	}
	//copy constructor
	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
		this.pNum = point.pNum;
	}
	

}