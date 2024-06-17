package network_plann

//Вычисление предшественников
private fun calcualte_previous(matrix: ArrayList<ArrayList<Int>>): ArrayList<ArrayList<Int>> {
    var previous: ArrayList<ArrayList<Int>> = ArrayList(matrix.size)
    for( i in 0..matrix.size - 1)
        previous.add(ArrayList<Int>())

    for(i in 0..matrix.size - 1)
        for(j in 0..matrix.size - 1)
            if (matrix.get(i).get(j) > 0)
                previous.get(j).add(i)
    return previous
}

//Вычисление ранних сроков свершения событий
private fun calcualte_Tp(matrix: ArrayList<ArrayList<Int>>, prev: ArrayList<ArrayList<Int>>): ArrayList<Int>{
    var Tp: ArrayList<Int> = ArrayList()
    for(i in 0..matrix.size - 1)
        Tp.add(0)

    for(i in 0..Tp.size - 1){
        var previous: ArrayList<Int> = prev.get(i)
        var temp: ArrayList<Int> = ArrayList()
        for(j in previous){
            if(previous.size == 1 && j == 0)
                Tp.set(i, matrix.get(j).get(i))
            else
                temp.add(matrix.get(j).get(i) + Tp.get(j))
        }
        if(temp.size != 0)
            Tp.set(i, temp.maxOrNull() ?: 0)
    }
    return Tp
}

//Вычисление поздних сроков свершения событий
private fun calculate_Tr(matrix: ArrayList<ArrayList<Int>>, Tp: ArrayList<Int>): ArrayList<Int> {
    var Tr: ArrayList<Int> = ArrayList()
    for(i in 0..matrix.size - 1)
        Tr.add(0)
    Tr.set(Tp.size - 1, Tp.get(Tp.size - 1))
    for(i in Tp.size - 2 downTo 0){
        var temp: ArrayList<Int> = ArrayList()
        for(j in 0..Tr.size - 1)
            if(matrix.get(i).get(j) > 0)
                temp.add(Tr.get(j) - matrix.get(i).get(j))
        Tr.set(i, temp.minOrNull() ?: 0)
    }
    return Tr
}

//Вычисление критического пути
private fun calculate_critical_path(Tp: ArrayList<Int>, Tr: ArrayList<Int>): ArrayList<Int>{
    var critical_path: ArrayList<Int> = ArrayList()
    for(i in 0..Tr.size - 1)
        if(Tr.get(i) == Tp.get(i))
            critical_path.add(i)
    return critical_path
}

//Проверка размера матрицы (размер должен быть n x n)
private fun check_matrix(matrix: ArrayList<ArrayList<Int>>): Boolean{
    for(i in 0..matrix.size - 1)
        if(matrix.size == matrix.get(i).size)
            continue
        else
            return false
    return true
}

//Вычисление всех резервов времени работы
private fun calculate_all_reserve(matrix: ArrayList<ArrayList<Int>>, Tp: ArrayList<Int>, Tr: ArrayList<Int>): ArrayList<HashMap<String, Int>>{
    var reserve: ArrayList<HashMap<String, Int>> = ArrayList()
    var Rfull: HashMap<String, Int> = HashMap()
    var Rguaranteed: HashMap<String, Int> = HashMap()
    var Rfree: HashMap<String, Int> = HashMap()
    var Rindependent: HashMap<String, Int> = HashMap()

    for(i in 0..matrix.size - 1)
        for(j in 0..matrix.size - 1)
            if(matrix.get(i).get(j) > 0){
                Rfull.put(i.toString() + "-" + j.toString(),  Tr.get(j) - Tp.get(i) - matrix.get(i).get(j))
                Rguaranteed.put(i.toString() + "-" + j.toString(), Tr.get(j) - Tr.get(i) - matrix.get(i).get(j))
                Rfree.put(i.toString() + "-" + j.toString(), Tp.get(j) - Tp.get(i) - matrix.get(i).get(j))
                Rindependent.put(i.toString() + "-" + j.toString(), Tp.get(j) - Tr.get(i) - matrix.get(i).get(j))
            }
    reserve.add(Rfull)
    reserve.add(Rguaranteed)
    reserve.add(Rfree)
    reserve.add(Rindependent)
    return reserve
}

//Вывод ранних и поздних сроков свершения событий
private fun print_Tp_Tr(Tp: ArrayList<Int>, Tr: ArrayList<Int>){
    print("_____________Сроки свершения событий______________")
    println()
    print(String.format("%-13s", "Событие"))
    print(String.format("%-13s", "Ранние"))
    print(String.format("%-13s", "Поздние"))
    print(String.format("%-13s", "Резерв"))
    println()
    for(i in 0..Tp.size - 1){
        print(String.format("%-13s", i))
        print(String.format("%-13s", Tp[i]))
        print(String.format("%-13s", Tr[i]))
        print(String.format("%-13s", Tr[i] - Tp[i]))
        println()
    }
    println()
}

//Вывод маршрута критического пути
private fun print_critical_path(critical_path: ArrayList<Int>){
    print("Критический путь: ")
    for(i in 0..critical_path.size - 1){
        if(i < critical_path.size - 1)
            print(critical_path.get(i).toString() + " -> ")
        else
            print(critical_path.get(i))
    }
    println()
}

//Вывод резервов работы
private fun print_reserve(reserve: ArrayList<HashMap<String, Int>>){
    
    println("_______________________________Резервы работ_________________________________")
    print(String.format("%-13s", "Связь"))
    print(String.format("%-13s", "Полный"))
    print(String.format("%-18s", "Гарантированный"))
    print(String.format("%-18s", "Свободный"))
    print(String.format("%-16s", "Независимый"))
    println()

    for(i in reserve[0].keys){
        print(String.format("%-13s", i))
        print(String.format("%-13s", reserve[0][i]))
        print(String.format("%-18s", reserve[1][i]))
        print(String.format("%-18s", reserve[2][i]))
        print(String.format("%-13s", reserve[3][i]))
        println()
    }
}

//Вычисление параметров сетевой модели
public fun calcualte_parameters(matrix: ArrayList<ArrayList<Int>>){
    if(check_matrix(matrix)){
        var previous: ArrayList<ArrayList<Int>> = calcualte_previous(matrix)

        var Tp: ArrayList<Int> = calcualte_Tp(matrix, previous)
        var Tr: ArrayList<Int> = calculate_Tr(matrix, Tp)
        print_Tp_Tr(Tp, Tr)
        
        var reserve: ArrayList<HashMap<String, Int>> = calculate_all_reserve(matrix, Tp, Tr)
        print_reserve(reserve)

        var critical_path: ArrayList<Int> = calculate_critical_path(Tp, Tr)
        print_critical_path(critical_path)
    }
    else
        println("Неверный размер матрицы!")
}