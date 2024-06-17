#Вычисление предшественников
def calculate_previous(matrix: list[list]) -> list:
    previous = [[] for i in range(len(matrix))]
    for i, n_i in enumerate(matrix):
        for j, n_j in enumerate(n_i): 
            if type(n_j) == int:
                if n_j > 0:
                    previous[j].append(i)
            else:
                previous[j].append(i)
    return previous

#Вычисление ранних сроков свершения событий
def calculate_Tp(matrix: list[list], prev: list[list]) -> list:
    Tp = [0 for i in range(len(matrix))] # Установка размерности и заполнение нулями вектора ранних сроков
    for i in range(len(matrix)):
        previous = prev[i]
        temp = list()
        for j in previous:
            if len(previous) == 1 and j == 0:
                Tp[i] = matrix[j][i]
            else:
                temp.append(matrix[j][i] + Tp[j])
        print(temp)
        if temp:
            Tp[i] = max(temp)
    return Tp

#Вычисление поздних сроков свершения событий
def calculate_Tr(matrix: list[list], Tp: list) -> list:
    Tr = [0 for i in range(len(matrix))]  # Установка размерности и заполнение нулями вектора поздних сроков
    Tr[len(Tp) - 1] = Tp[len(Tp) - 1]
    for i in range(len(Tr) - 2, -1, -1):
        temp = list()
        for j in range(len(Tr)):
            if matrix[i][j] > 0:
                temp.append(Tr[j] - matrix[i][j])
        Tr[i] = min(temp)
    return Tr            

#Вычисление критического пути
def calculate_critical_path(Tr: list, Tp: list) -> list:
    critical_path = list()
    for i in range(len(Tr)):
        if Tr[i] == Tp[i]:
            critical_path.append(i)
    return critical_path
    
#Проверка размера матрицы (размер должен быть n x n)
def check_matrix(matrix: list[list]) -> bool:
    for i in range(len(matrix)):
        if len(matrix) == len(matrix[i]):
            continue
        else:
            return False
    return True

#Вычисление всех резервов времени работы
def calculate_all_reserve(Tp: list, Tr: list, matrix: list[list]) -> tuple[dict, dict, dict, dict]:
    Rfull = dict() # Полный резерв времени работы
    Rguaranteed = dict() # Гарантированный резерв времени работы    
    Rfree = dict() # Свободный резерв времени работы
    Rindependent = dict() # Независимый резерв времени работы
    for i in range(len(matrix)):
        for j in range(len(matrix)):
            if matrix[i][j] > 0:
                Rfull[f'{i}' + '-' + f'{j}'] = Tr[j] - Tp[i] - matrix[i][j]
                Rguaranteed[f'{i}' + '-' + f'{j}'] = Tr[j] - Tr[i] - matrix[i][j]
                Rfree[f'{i}' + '-' + f'{j}'] = Tp[j] - Tp[i] - matrix[i][j]
                Rindependent[f'{i}' + '-' + f'{j}'] = Tp[j] - Tr[i] - matrix[i][j]
    return Rfull, Rguaranteed, Rfree, Rindependent

#Вывод ранних и поздних сроков свершения событий
def print_Tp_Tr(Tp: list, Tr: list) -> None:
    print('__________Сроки свершения событий___________')
    column_name = ['Событие', 'Ранние', 'Поздние', 'Резерв']
    print(f'{column_name[0]:^10}' + "|" + f'{column_name[1]:^10}' + "|" + f'{column_name[2]:^10}' + "|" + f'{column_name[3]:^10}' + "|")
    for i in range(len(Tp)):
        print(f'{i:^10}' + "|" + F'{Tp[i]:^10}' + "|" + F'{Tr[i]:^10}' + "|"+ F'{Tr[i] - Tp[i]:^10}' + "|")
    print("\n")

#Вывод маршрута критического пути
def print_critical_path(critical_path: list) -> None:
    print('Критический путь:')
    for i in range(len(critical_path)):
        if(i != len(critical_path) - 1):
            print(critical_path[i], " -> ", end = '')
        else:
            print(critical_path[i], end = '')
    print("\n\n")

#Вывод резервов работы
def print_reserve(Rfull: dict, Rguaranteed: dict, Rfree: dict, Rindependent: dict) -> None:
    print('___________________________Резервы работ___________________________')
    column_name = ['Связь', 'Полный', 'Гарантированный', 'Свободный', 'Независимый']

    print(f'{column_name[0]:^10}' + "|" + f'{column_name[1]:^10}' + "|" + f'{column_name[2]:^17}' + "|" + f'{column_name[3]:^12}' + "|" + f'{column_name[4]:^14}' + "|")
    for key in Rfull:
        print(f'{key:^10}' + "|" + f'{Rfull[key]:^10}' + "|" + f'{Rguaranteed[key]:^17}' + "|"  + f'{Rfree[key]:^12}' + "|"  + f'{ Rindependent[key]:^14}' + "|")
    print("\n")

#Вычисление параметров сетевой модели
def calculate_parameters(matrix: list[list]) -> None:
    if check_matrix(matrix):
        prev = calculate_previous(matrix)
        Tp = calculate_Tp(matrix, prev)
        Tr = calculate_Tr(matrix, Tp)
        print_Tp_Tr(Tp, Tr)

        Rfull, Rguaranteed, Rfree, Rindependent = calculate_all_reserve(Tp, Tr, matrix)
        print_reserve(Rfull, Rguaranteed, Rfree, Rindependent)

        critical_path = calculate_critical_path(Tr, Tp)
        print_critical_path(critical_path)
    else:
        print('Неверный размер матрицы!')