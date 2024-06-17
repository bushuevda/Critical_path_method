package network_plann

import (
	"fmt"
	"os"
	"strconv"
)

// Вычисление предшественников
func calculate_previous(matrix *[][]int) [][]int {
	var previous = make([][]int, len(*matrix))
	for i, n_i := range *matrix {
		for j, n_j := range n_i {
			if n_j > 0 {
				previous[j] = append(previous[j], i)
			}
		}
	}
	for i := 0; i < len(previous); i++ {
		for j := 0; j < len(previous[i]); j++ {
		}
	}
	return previous
}

// Вычисление ранних сроков свершения событий
func calculate_Tp(matrix *[][]int, prev *[][]int) []int {
	var Tp = make([]int, len(*matrix))
	for i := 0; i < len(*matrix); i++ {
		var previous []int = (*prev)[i]
		var temp []int
		for _, j := range previous {
			if len(previous) == 1 && j == 0 {
				Tp[i] = (*matrix)[j][i]
			} else {
				temp = append(temp, (*matrix)[j][i]+Tp[j])
			}
		}
		if len(temp) != 0 {
			max_element := 0
			for k := 0; k < len(temp); k++ {
				if max_element < temp[k] {
					max_element = temp[k]
				}
			}
			Tp[i] = max_element
		}
	}
	return Tp
}

// Вычисление поздних сроков свершения событий
func calculate_Tr(matrix *[][]int, Tp *[]int) []int {
	var Tr = make([]int, len(*matrix))
	Tr[len(*Tp)-1] = (*Tp)[len(*Tp)-1]
	for i := len(Tr) - 2; i >= 0; i-- {
		var temp []int
		for j := 0; j < len(Tr); j++ {
			if (*matrix)[i][j] > 0 {
				temp = append(temp, Tr[j]-(*matrix)[i][j])
			}
		}
		if len(temp) != 0 {
			min_element := temp[0]
			for k := 0; k < len(temp); k++ {
				if min_element > temp[k] {
					min_element = temp[k]
				}
			}
			Tr[i] = min_element
		}
	}
	return Tr
}

// Вычисление критического пути
func calculate_critical_path(Tr *[]int, Tp *[]int) []int {
	var critical_path []int
	for i := 0; i < len(*Tr); i++ {
		if (*Tr)[i] == (*Tp)[i] {
			critical_path = append(critical_path, i)
		}
	}
	return critical_path
}

// Проверка размера матрицы (размер должен быть n x n)
func check_matrix(matrix *[][]int) bool {
	for i := 0; i < len(*matrix); i++ {
		if len(*matrix) == len((*matrix)[i]) {
			continue
		} else {
			return false
		}
	}
	return true
}

// Вычисление всех резервов времени работы
func calculate_all_reserve(Tr *[]int, Tp *[]int, matrix *[][]int) (map[string]int, map[string]int, map[string]int, map[string]int) {
	Rfull := make(map[string]int)        // Полный резерв времени работы
	Rguaranteed := make(map[string]int)  // Гарантированный резерв времени работы
	Rfree := make(map[string]int)        // Свободный резерв времени работы
	Rindependent := make(map[string]int) //# Независимый резерв времени работы

	for i := 0; i < len(*matrix); i++ {
		for j := 0; j < len(*matrix); j++ {
			if (*matrix)[i][j] > 0 {
				Rfull[strconv.Itoa(i)+"-"+strconv.Itoa(j)] = (*Tr)[j] - (*Tp)[i] - (*matrix)[i][j]
				Rguaranteed[strconv.Itoa(i)+"-"+strconv.Itoa(j)] = (*Tr)[j] - (*Tr)[i] - (*matrix)[i][j]
				Rfree[strconv.Itoa(i)+"-"+strconv.Itoa(j)] = (*Tp)[j] - (*Tp)[i] - (*matrix)[i][j]
				Rindependent[strconv.Itoa(i)+"-"+strconv.Itoa(j)] = (*Tp)[j] - (*Tr)[i] - (*matrix)[i][j]
			}
		}
	}
	return Rfull, Rguaranteed, Rfree, Rindependent
}

// Вывод ранних и поздних сроков свершения событий
func print_Tp_Tr(Tp *[]int, Tr *[]int) {
	fmt.Println("________________Сроки свершения событий_________________")
	fmt.Fprintf(os.Stdout, "%-15s %-15s %-15s %-15s", "Событие", "Ранние", "Поздние", "Резерв")
	fmt.Println()
	for i := 0; i < len(*Tr); i++ {
		fmt.Fprintf(os.Stdout, "%-15d %-15d %-15d %-15d", i, ((*Tp)[i]), ((*Tr)[i]), ((*Tr)[i] - (*Tp)[i]))
		fmt.Println()
	}
	fmt.Print("\n\n")
}

// Вывод маршрута критического пути
func print_critical_path(critical_path *[]int) {
	fmt.Println("Критический путь:")
	for i := 0; i < len(*critical_path); i++ {
		if i != len(*critical_path)-1 {
			fmt.Print((*critical_path)[i], " -> ")
		} else {
			fmt.Print((*critical_path)[i], "\n\n")
		}
	}
}

// Вывод резервов работы
func print_reserve(Rfull *map[string]int, Rguaranteed *map[string]int, Rfree *map[string]int, Rindependent *map[string]int) {
	fmt.Println("_____________________________Резервы работ_____________________________")
	fmt.Fprintf(os.Stdout, "%-10s %-10s %-20s %-15s %-15s", "Связь", "Полный", "Гарантированный", "Свободный", "Независимый")
	fmt.Println()
	for key, _ := range *Rfull {
		fmt.Fprintf(os.Stdout, "%-10s %-10d %-20d %-15d %-10d", key, (*Rfull)[key], (*Rguaranteed)[key], (*Rfree)[key], (*Rindependent)[key])
		fmt.Println()
	}
}

// Вычисление параметров сетевой модели
func Calculate_parameters(matrix *[][]int) {
	if check_matrix(matrix) {
		ptr_matrix := matrix
		previous := calculate_previous(ptr_matrix)
		ptr_previous := &previous

		Tp := calculate_Tp(ptr_matrix, ptr_previous)
		ptr_Tp := &Tp
		Tr := calculate_Tr(ptr_matrix, ptr_Tp)
		ptr_Tr := &Tr
		print_Tp_Tr(ptr_Tp, ptr_Tr)

		Rfull, Rguaranteed, Rfree, Rindependent := calculate_all_reserve(ptr_Tr, ptr_Tp, ptr_matrix)
		ptr_rfull := &Rfull
		ptr_rguaranteed := &Rguaranteed
		ptr_rfree := &Rfree
		ptr_rindependent := &Rindependent
		print_reserve(ptr_rfull, ptr_rguaranteed, ptr_rfree, ptr_rindependent)

		critical_path := calculate_critical_path(ptr_Tr, ptr_Tp)
		prt_critical_path := &critical_path
		print_critical_path(prt_critical_path)
	} else {
		fmt.Println("Неверный размер матрицы!")
	}
}
