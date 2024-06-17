#ifndef NETWORK_PLANN
#define NETWORK_PLANN 1
#include <vector>
#include <map>
#include <string>

namespace nplann{
    //Вычисление предшественников+
    std::vector<std::vector<int>> calculate_previous(const std::vector<std::vector<int>> &matrix);

    //Вычисление ранних сроков свершения событий+
    std::vector<int> calculate_Tp(const std::vector<std::vector<int>> &matrix, const std::vector<std::vector<int>> &prev);

    //Вычисление поздних сроков свершения событий+
    std::vector<int> calculate_Tr(const std::vector<std::vector<int>> &matrix, const std::vector<int> &Tp);

    //Вычисление критического пути+
    std::vector<int> calculate_critical_path(const std::vector<int> &Tp, const std::vector<int> &Tr);

    //Проверка размера матрицы (размер должен быть n x n)+
    bool check_matrix(const std::vector<std::vector<int>> &matrix);

    //Вычисление всех резервов времени работы+
    std::vector<std::map<std::string, int>> calculate_all_reserve(const std::vector<std::vector<int>> &matrix, const std::vector<int> &Tp, const std::vector<int> &Tr);

    //Вывод ранних и поздних сроков свершения событий
    void print_Tp_Tr(const std::vector<int> &Tp, const std::vector<int> &Tr);

    //Вывод маршрута критического пути+
    void print_critical_path(const std::vector<int> &critical_path, std::ios &init);

    //Вывод резервов работы+
    void print_reserve(const std::vector<std::map<std::string, int>> &reserve);

    //Вычисление параметров сетевой модели
    void calculate_parameters(const std::vector<std::vector<int>> &matrix);
}

#endif