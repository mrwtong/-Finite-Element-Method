# Finite-Element-Method
A Simple Case of Finite Element Method using Java
## 一、简介：
虽然Java语言在计算性能上远不及C++，但是作为一次使用面向对象方法实现一个小型计算程序的编程实践来说是比较合适的。一方面Java支持彻底的面向对象方法，另一方面Java虚拟机能够自动管理内存，减少了使用C++手动管理内存的复杂性，对于初学者来说比较友好。
## 二、程序设计
程序设计任务：分别从字符文件读入节点、网格单元、位移约束和节点应力；计算生成刚度矩阵，并且结合边界条件计算节点位移。  
### 1、程序输入文件：  
（1）nodes（节点文件）   
4//第一行 节点数 末尾回车分隔   
1 0 0//第二行开始为节点数据，其中三个使用空格数据行分别为节点号，x坐标，y坐标   
2 1 0   
3 1 1   
4 0 1   
（2）elements（网格单元文件）   
2//第一行为三角形网格单元数   
1 1 2 4//第二行开始为网格单元，其中使用空格分割的数据分别为单元序号，单元的内部1号节点对应的全局节点号，单元的内部2号节点对应的全局节点号，单元的内部3号节点对应的全局节点号。   
2 2 3 4   
（3）constraint（位移约束）    
2 //第一行为存在位移约束的节点    
3 0 0 //分别为：存在位移约束节点号，该节点X方向位移约束，该节点Y方向位移约束。  
4 0 0    
（4）stress（应力边界条件）   
2//第一行为存在外应力的节点    
1 0 -1//分别为：存在外应力的节点，该节点X方向应力大小，该节点Y方向应力大小。   
2 1 0    
### 2、程序模块     
程序按照功能主要分为5个包（方法见注释）：  
 （1）geometry（几何包）:包含3个几何类，分别为节点数据类（Point），单元类（Element），几何结构类（Geometry）。   
该包构成了计算程序的数据类，存储了节点和单元的构成关系，几何类的构造使用从主程序读取的节点文件流。   
 （2）elementMatrix（单元矩阵包）：包含一个设置力学参数的接口类（SetProperties）和一个单元矩阵类（EMatrix）。   
该包功能为根据单元节点坐标的创建单元刚度矩阵，并且记录单元局部坐标序号和全局坐标序号的映射关系。   
 （3）globalMatrix（整体刚度矩阵包）：包含一个设置力学参数的接口类（SetProperties）和一个整体刚度矩阵类（GMatrix）。   
该包功能为根据单元刚度矩阵和几何结构创建整理刚度矩阵。  
 （4）boundaryConditions(边界条件包)：由边界条件设置类（BoundaryConditions）构成  
BoundaryConditions类实现了由入读的文件流创建边界条件，构造相应的矩阵并且求解。  
 （5）solver（求解类）：由有限单元法求解主函数构成（FEMSolverMain）。  
FEMSolverMain类为主程序，运行时需带节点和边界条件文件的路径参数。该类调用前四个包的功能实现创建几何，构造矩阵、应用边界条件和求解。    

 相关类库：为了实现高效的矩阵运算，使用Java第三方类非稀疏矩阵类库（Jama）来实现矩阵的存储、操作和运算。 
 ## 三、程序文档
 ### 1、Point  
构造方法：  
Point(double, double, int)；由点的坐标和序号构造。    
Point(Point)；由引用构造。    
### 2、Element  
构造方法：  
Element(int, Point[])；由单元序号和单元拥有的节点数组引用构造。     
Element(int, Point, Point, Point)；由单元序号和三个节点的引用构造。    
### 3、Geometry  
构造方法：  
Geometry(BufferedReader, BufferedReader)；由节点文件和单元文件的流构造几何。    
类方法：  
int GetTotalNodeNum()；返回几何的节点数。   
int GetTotalElementNum()；返回几何的单元数。    
Element GetElement(int)；返回几何中的指定单元。    
### 4、EMatrix  
构造方法：  
EMatrix(Element)；由单元构造单元矩阵参数。    
类方法：  
void ShapeFunction()；计算形函数相关参数。    
private Matrix SubEStiffnessMatrix(int, int)；计算制定角标的单元刚度矩阵子矩阵。  
void EStiffnessMatrix()；计算单元刚度矩阵。  
void Print()；在屏幕上打印刚度矩阵。  
int GetInternalNu(int)；根据全局节点号得到单元刚度矩阵内部节点号。  
int GetGlobalNu(int)；根据刚度矩阵内部节点号得到全局节点号。  
Matrix GetSubMatrix(int, int)；得到指定下标的刚度矩阵子矩阵。  
### 5、GMatirx   
构造方法：  
GMatrix(Geometry)；根据几何构造全局矩阵相关内部参数。  
类方法：  
void BuildEMatrix(double , double , double )；根据E，nu，t创建所有的单元刚度矩阵。
private Matrix PlusSubTwoMatrix(int elementnum, int i, int j)；将指定的单元刚度矩阵子矩阵加到整体刚度矩阵指定的位置。  
void BuildGMatrix()；单元遍历法创建整体刚度矩阵。  
void Print()；在屏幕上打印全局刚度矩阵。  
int GetTotalNodes()；得到整体刚度矩阵的节点数目。  
Matrix GetGlobalMatrix()；得到整体刚度矩阵。  
### 6、BoundaryConditions  
构造方法：  
BoundaryConditions(int,BufferedReader, BufferedReader)；分别由节点总数，应力边界条件文件流，约束边界条件文件流构造边界条件相应参数。  
类方法：  
Matrix ReplaceMethod(Matrix)；使用置换法在整体刚度矩阵上构造边界条件。  
void ReplaceMethodSolveOnBC(Matrix)；结合应力边界求解置换法构造的刚度矩阵。  
Matrix GetBGNBNMstress(Matrix)；使用置大数法在构造应力边界条件。  
Matrix GetBGNGlobalMatrix(Matrix globalMatrix)；使用置大数法在整体刚度矩阵上构造边界条件。  
void BNMSolveOnBC(Matrix) 结合应力边界求解制大数法构造的刚度矩阵。  
