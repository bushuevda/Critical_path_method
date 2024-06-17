#include "network_plann.hpp"
#include <iostream>
#include <algorithm>

//Вычисление предшественников
std::vector<std::vector<int>> nplann::calculate_previous(const std::vector<std::vector<int>> &matrix){
    std::vector<std::vector<int>> previous(size(matrix));
    for(int i = 0; i < size(matrix); i++)
        for(int j = 0; j < size(matrix); j++)
            if(matrix[i][j] > 0)
                previous[j].push_back(i);
    return previous;
};

//Вычисление ранних сроков свершения событий
std::vector<int> nplann::calculate_Tp(const std::vector<std::vector<int>> &matrix, const std::vector<std::vector<int>> &prev){
    std::vector<int> Tp(size(matrix));
    std::generate(Tp.begin(), Tp.end(), [](){return 0;});

    for(int i = 0; i < matrix.size(); i++)
    {
        std::vector<int> previous = prev[i];
        std::vector<int> temp;
        for(auto j : previous)
            if(previous.size() == 1 && j == 0)
                Tp[i] = matrix[j][i];
            else
                temp.push_back(matrix[j][i] + Tp[j]);
        if (temp.size() != 0)
                Tp[i] = *std::max_element(temp.begin(), temp.end());
    }
    return Tp;
};

//Вычисление поздних сроков свершения событий
std::vector<int> nplann::calculate_Tr(const std::vector<std::vector<int>> &matrix, const std::vector<int> &Tp){
    std::vector<int> Tr(size(matrix));
    std::generate(Tr.begin(), Tr.end(), [](){return 0;});
    Tr[Tp.size() -1] = Tp[Tp.size() - 1];
    for(int i = Tp.size() - 2; i >= 0; i--)
    {
        std::vector<int> temp;
        for(int j = 0; j < Tr.size(); j++)
            if (matrix[i][j] > 0)
                temp.push_back(Tr[j] - matrix[i][j]);
        Tr[i] = *std::min_element(temp.begin(), temp.end());
    }   
    return Tr;
};

//Вычисление критического пути
std::vector<int> nplann::calculate_critical_path(const std::vector<int> &Tp, const std::vector<int> &Tr){
    std::vector<int> critical_path;
    for(int i = 0; i < Tr.size(); i++)
        if (Tr[i] == Tp[i])
            critical_path.push_back(i);
    return critical_path;
};

//Проверка размера матрицы (размер должен быть n x n)
bool nplann::check_matrix(const std::vector<std::vector<int>> &matrix){
    int size = matrix.size();
    for(int i = 0; i < matrix.size(); i++)
        if(matrix.size() == matrix[i].size())
            continue;
        else 
            return false;
    return true;
};

//Вычисление всех резервов времени работы
std::vector<std::map<std::string, int>> nplann::calculate_all_reserve(const std::vector<std::vector<int>> &matrix, const std::vector<int> &Tp, const std::vector<int> &Tr){
    std::vector<std::map<std::string, int>> reserve;
    std::map<std::string, int> Rfull;
    std::map<std::string, int> Rguaranteed;
    std::map<std::string, int> Rfree;
    std::map<std::string, int> Rindependent;
    for(int i = 0; i < matrix.size(); i++)
        for(int j = 0; j < matrix.size(); j++)
            if(matrix[i][j] > 0)
            {
                Rfull[std::to_string(i) + "-" + std::to_string(j)] = Tr[j] - Tp[i] - matrix[i][j];
                Rguaranteed[std::to_string(i) + "-" + std::to_string(j)] = Tr[j] - Tr[i] - matrix[i][j];
                Rfree[std::to_string(i) + "-" + std::to_string(j)] = Tp[j] - Tp[i] - matrix[i][j];
                Rindependent[std::to_string(i) + "-" + std::to_string(j)] = Tp[j] - Tr[i] - matrix[i][j];    
            }
    reserve.push_back(Rfull);
    reserve.push_back(Rguaranteed);
    reserve.push_back(Rfree);
    reserve.push_back(Rindependent);
    return reserve;
};

//Вывод маршрута критического пути
void nplann::print_critical_path(const std::vector<int> &critical_path, std::ios &init){
    //Критический путь
    std::cout<<"Критический путь:"<<std::endl;
    for(auto i : critical_path)
    {
        std::cout<<i<<" ";
        std::cout.width(5);
        std::cout.fill('-');
    }
    std::cout.copyfmt(init);    
    std::cout<<"\n\n";
};

//Вывод резервов работы+
void nplann::print_reserve(std::vector<std::map<std::string, int>> &reserve){
        //Резервы работ
        std::cout << "Резервы работ:" << std::endl;
        std::cout << "|"; std::cout.width(16);
        std::cout << "Связь"; std::cout.width(9);
        std::cout << "|"; std::cout.width(18);
        std::cout << "Полный"; std::cout.width(8);
        std::cout << "|"; std::cout.width(18);
        std::cout << "Гарантированный"; std::cout.width(5);
        std::cout << "|"; std::cout.width(13);
        std::cout << "Независимый"; std::cout.width(9);
        std::cout << "|"; std::cout.width(10);
        std::cout << "Свободный"; std::cout.width(11);
        std::cout << "|" << std::endl;
        for(auto [key, value] : reserve[0])
        {
            std::cout << "|"; std::cout.width(10);
            std::cout << key; std::cout.width(10);
            std::cout << "|"; std::cout.width(10);
            std::cout << reserve[0][key]; std::cout.width(10);
            std::cout << "|"; std::cout.width(10);
            std::cout << reserve[1][key]; std::cout.width(10);
            std::cout << "|"; std::cout.width(10);
            std::cout << reserve[2][key]; std::cout.width(10);
            std::cout << "|"; std::cout.width(10);
            std::cout << reserve[3][key]; std::cout.width(10);
            std::cout << "|" << std::endl;
        }
};

void  nplann::print_Tp_Tr(const std::vector<int> &Tp, const std::vector<int> &Tr){
        std::cout << "|"; std::cout.width(15);
        std::cout << "Связь"; std::cout.width(8);
        std::cout << "|"; std::cout.width(9);
        std::cout << "Ранние сроки"; std::cout.width(6);
        std::cout << "|"; std::cout.width(9);
        std::cout << "Поздние сроки"; std::cout.width(5);
        std::cout << "|"; std::cout.width(9);
        std::cout << "Резерв"; std::cout.width(12);
        std::cout << "|"; 
        std::cout << std::endl;

        for(int i = 0; i < Tr.size(); i++)
        {
            std::cout << "|"; std::cout.width(9);
            std::cout << i ; std::cout.width(9);
            std::cout << "|"; std::cout.width(9);
            std::cout << Tr[i]; std::cout.width(9);
            std::cout <<"|" ; std::cout.width(9);
            std::cout << Tp[i]; std::cout.width(9);
            std::cout <<"|"; std::cout.width(9);
            std::cout << Tr[i] - Tp[i]; std::cout.width(9);
            std::cout <<"|"; 
            std::cout << std::endl;    
        }
        std::cout<<"\n\n";
};

//Вычисление параметров сетевой модели
void nplann::calculate_parameters(const std::vector<std::vector<int>> &matrix){
    if(check_matrix(matrix)){
        std::vector<std::vector<int>> prev = nplann::calculate_previous(matrix);
        std::vector<int> Tp = nplann::calculate_Tp(matrix, prev);
        std::vector<int> Tr = nplann::calculate_Tr(matrix, Tp);
        std::vector<int> critical_path = nplann::calculate_critical_path(Tp, Tr);
        std::vector<std::map<std::string, int>> reserve = nplann::calculate_all_reserve(matrix, Tp, Tr); 
        std::ios init(NULL);
        init.copyfmt(std::cout);
        print_Tp_Tr(Tp, Tr);
        print_reserve(reserve);
        print_critical_path(critical_path, init);
    }
    else
        std::cout<<"Неверный размер матрицы!" << std::endl;
};