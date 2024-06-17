import network_plann.calcualte_parameters as calcualte_parameters

fun main(){
    var matrix1: ArrayList<ArrayList<Int>> = ArrayList<ArrayList<Int>>()
    matrix1 = arrayListOf(
    //   0  1  2  3  4  5  6  7  8
    arrayListOf(0, 6, 6, 5, 0, 0, 0, 0, 0), //0
    arrayListOf(0, 0, 0, 0, 8, 0, 0, 0, 0), //1
    arrayListOf(0, 0, 0, 0, 5, 5, 0, 0, 0), //2
    arrayListOf(0, 0, 0, 0, 0, 0, 2, 0, 0), //3
    arrayListOf(0, 0, 0, 0, 0, 8, 3, 0, 0), //4
    arrayListOf(0, 0, 0, 0, 0, 0, 0, 4, 4), //5
    arrayListOf(0, 0, 0, 0, 0, 0, 0, 3, 4), //6
    arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 3), //7
    arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)  //8
    ) 
    
    calcualte_parameters(matrix1)
}
