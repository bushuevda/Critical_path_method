//Вычисление предшественников
function calculate_previous(matrix){
    let previous = Array();
    for (let i = 0; i < matrix.length; i++)
        previous.push([]);

    for(let i = 0; i < matrix.length; i++)
            for(let j = 0; j < matrix.length; j++)
                if(matrix[i][j] > 0)
                    previous[j].push(i);
    return previous;
}

//Вычисление ранних сроков свершения событий
function calculate_Tp(matrix, prev){
    let Tp = Array(matrix.length);
    for (let i = 0; i < matrix.length; i++)
        Tp[i] = 0

    for(let i = 0; i < matrix.length; i++){
        let previous = prev[i];
        let temp = Array();
        previous.forEach((j) => {
            if(previous.length == 1 && j == 0)
                Tp[i] = matrix[j][i];
            else
                temp.push(matrix[j][i] + Tp[j]);
        });
        if (temp.length != 0)
        {
            let max_element = function(temp){
                let m = 0;
                temp.forEach((n => {
                    if (n > m)
                        m = n
                }));
                return m;
            };
            Tp[i] = max_element(temp);
        }
    }
    return Tp;
}

//Вычисление поздних сроков свершения событий
function calculate_Tr(matrix, Tp){
    let Tr = Array(matrix.length);
    for (let i = 0; i < matrix.length; i++)
        Tr[i] = 0

    Tr[Tp.length -1] = Tp[Tp.length - 1];
    for(let i = Tp.length - 2; i >= 0; i--)
    {
        let temp = Array();
        for(let j = 0; j < Tr.length; j++)
            if (matrix[i][j] > 0)
                temp.push(Tr[j] - matrix[i][j]);
        if (temp.length != 0) {
            let min_element = function(temp){
                let m = temp[0];
                temp.forEach((n => {
                    if (n < m)
                        m = n
                }));
                return m;
            };
            Tr[i] = min_element(temp);
        }
    }   
    return Tr;
}

//Вычисление критического пути
function calculate_critical_path(Tp, Tr){
    let critical_path = Array();
    for(let i = 0; i < Tr.length; i++)
        if (Tr[i] == Tp[i])
            critical_path.push(i);
return critical_path;
}

//Проверка размера матрицы (размер должен быть n x n)
function check_matrix(matrix){
    for(let i = 0; i < matrix.length; i++)
        if(matrix.length == matrix[i].length)
            continue;
        else 
            return false;
return true;
}

//Вычисление всех резервов времени работы
function calculate_all_reserve(matrix, Tp, Tr){
    let reserve = Array();
    let Rfull = new Map();
    let Rguaranteed = new Map();
    let Rfree = new Map();
    let Rindependent = new Map();
    for(let i = 0; i < matrix.length; i++)
        for(let j = 0; j < matrix.length; j++)
            if(matrix[i][j] > 0)
            {
                Rfull.set(i.toString() + "-" + j.toString(), Tr[j] - Tp[i] - matrix[i][j]);
                Rguaranteed.set(i.toString() + "-" + j.toString(), Tr[j] - Tr[i] - matrix[i][j]);
                Rfree.set(i.toString() + "-" + j.toString(), Tp[j] - Tp[i] - matrix[i][j]);
                Rindependent.set(i.toString() + "-" + j.toString(), Tp[j] - Tr[i] - matrix[i][j]);    
            }
    reserve.push(Rfull);
    reserve.push(Rguaranteed);
    reserve.push(Rfree);
    reserve.push(Rindependent);
    return reserve;
}

// Вывод ранних и поздних сроков свершения событий
function print_Tp_Tr(Tp, Tr){
    let text = "__________Сроки свершения событий___________\n"
    let size_s = 5
    let s = new String()
    for (let i = 0; i < size_s; i++) s += " "
    text += "Событие" + s +  "Ранние" + s + "Поздние" + s + "Резерв" + s + "\n"
    size_s = 11
    let s2 = new String()
    for (let i = 0; i < size_s; i++) s2 += " "
    for (let i = 0; i < Tp.length; i++) 
        text += i.toString() + s2 + Tp[i].toString() + s2 + Tr[i].toString() + s2 + (Tr[i] - Tp[i]).toString() + "\n"
    console.log(text)
}

// Вывод маршрута критического пути
function print_critical_path(critical_path){
    let text = "Критический путь: "

    for (let i = 0; i < critical_path.length; i++)
        if(i != critical_path.length - 1)
            text += critical_path[i] + " -> "
        else
            text += critical_path[i]
    console.log(text + "\n\n")
}

//Вывод резервов работы
function print_reserve(reserve)
{
    let text = "___________________________Резервы работ___________________________\n"
    let size_s = 3
    let s = new String()
    for (let i = 0; i < size_s; i++) s += " "
    text += "Связь" + s +  "Полный" + s + "Гарантированный" + s + "Свободный" + s + "Независимый" + "\n"
    
    size_s = 10
    let s2 = new String()
    for (let i = 0; i < size_s; i++) s2 += " "

    let Rfull = reserve[0]
    let Rguaranteed = reserve[1]
    let Rfree = reserve[2]
    let Rindependent = reserve[3]
    for(let key of Rfull.keys())
        text += key + s2 + Rfull.get(key) + s2 + Rguaranteed.get(key) + s2 + Rfree.get(key) + s2 + Rindependent.get(key) + "\n"
    console.log(text)
}

//Вычисление параметров сетевой модели
function calculate_parameters(matrix){
    var prev = calculate_previous(matrix);

    var Tp = calculate_Tp(matrix, prev);
    var Tr = calculate_Tr(matrix, Tp)
    print_Tp_Tr(Tp, Tr)

    var reserve = calculate_all_reserve(matrix, Tp, Tr)
    print_reserve(reserve)
    
    var critical_path = calculate_critical_path(Tp, Tr)
    print_critical_path(critical_path)
}


export {calculate_parameters};