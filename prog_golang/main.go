package main

import "prog_golang/network_plann"

func main() {
	matrix := [][]int{
		//0  1  2  3  4  5  6  7  8
		{0, 6, 6, 5, 0, 0, 0, 0, 0}, //0
		{0, 0, 0, 0, 8, 0, 0, 0, 0}, //1
		{0, 0, 0, 0, 5, 5, 0, 0, 0}, //2
		{0, 0, 0, 0, 0, 0, 2, 0, 0}, //3
		{0, 0, 0, 0, 0, 8, 3, 0, 0}, //4
		{0, 0, 0, 0, 0, 0, 0, 4, 4}, //5
		{0, 0, 0, 0, 0, 0, 0, 3, 4}, //6
		{0, 0, 0, 0, 0, 0, 0, 0, 3}, //7
		{0, 0, 0, 0, 0, 0, 0, 0, 0}, //8
	}
	ptr_matrix := &matrix
	network_plann.Calculate_parameters(ptr_matrix)
}
