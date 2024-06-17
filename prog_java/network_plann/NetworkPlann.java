package network_plann;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;


public class NetworkPlann {
    //Вычисление предшественников
    private ArrayList<ArrayList<Integer> > calculate_previous(int [][] matrix){
        ArrayList<ArrayList<Integer> > previous = new ArrayList<ArrayList<Integer>>(matrix.length);
        for(int i = 0; i< matrix.length; i++)
            previous.add(new ArrayList<Integer>());

        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix.length; j++)
                if (matrix[i][j] > 0)
                    previous.get(j).add(i);
        return previous;
    };

    //Вычисление ранних сроков свершения событий
    private ArrayList<Integer> calculate_Tp(int [][] matrix, ArrayList<ArrayList<Integer> > prev){
        ArrayList<Integer> Tp = new ArrayList<Integer>();
        for(int i = 0; i < matrix.length; i++)
            Tp.add(0);

        for(int i = 0; i < Tp.size(); i++)
        {
            ArrayList<Integer> previous = prev.get(i);
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (Integer j : previous) 
                if(previous.size() == 1 && j == 0)
                    Tp.set(i, matrix[j][i]);
                else
                    temp.add(matrix[j][i] + Tp.get(j));
            if(temp.size() != 0)
                Tp.set(i, Collections.max(temp));
        }
        return Tp;
    };

    //Вычисление поздних сроков свершения событий
    private ArrayList<Integer> calculate_Tr(int [][] matrix, ArrayList<Integer> Tp){
        ArrayList<Integer> Tr = new ArrayList<Integer>();
        for(int i = 0; i < matrix.length; i++)
            Tr.add(0);

        Tr.set(Tp.size() - 1, Tp.get(Tp.size() - 1));
        for(int i = Tp.size() - 2; i >= 0; i--){
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for(int j = 0; j < Tr.size(); j++)
                if (matrix[i][j] > 0)
                    temp.add(Tr.get(j) - matrix[i][j]);
            Tr.set(i, Collections.min(temp));
        }
        return Tr;
    };

    //Вычисление критического пути
    private ArrayList<Integer> calculate_critical_path(ArrayList<Integer> Tp, ArrayList<Integer> Tr){
        ArrayList<Integer> critical_path = new ArrayList<Integer>();
        for(int i = 0; i < Tr.size(); i++)
            if (Tr.get(i) == Tp.get(i))
                critical_path.add(i);
        return critical_path;
    };

    //Проверка размера матрицы (размер должен быть n x n)
    private Boolean check_matrix(int [][] matrix){
        for(int i = 0; i < matrix.length; i++)
            if(matrix.length == matrix[i].length)
                continue;
            else 
                return false;
        return true;
    };

    //Вычисление всех резервов времени работы
    private ArrayList<HashMap<String, Integer>> calculate_all_reserve(int [][] matrix, ArrayList<Integer> Tp, ArrayList<Integer> Tr){
        ArrayList<HashMap<String, Integer>> reserve = new ArrayList<HashMap<String, Integer>>();
        HashMap<String, Integer> Rfull = new HashMap<String, Integer>();
        HashMap<String, Integer> Rguaranteed = new HashMap<String, Integer>();
        HashMap<String, Integer> Rfree = new HashMap<String, Integer>();
        HashMap<String, Integer> Rindependent = new HashMap<String, Integer>();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++)
                if(matrix[i][j] > 0)
                {
                    Rfull.put(Integer.toString(i) + "-" + Integer.toString(j),  Tr.get(j) - Tp.get(i) - matrix[i][j]);
                    Rguaranteed.put(Integer.toString(i) + "-" + Integer.toString(j), Tr.get(j) - Tr.get(i) - matrix[i][j]);
                    Rfree.put(Integer.toString(i) + "-" + Integer.toString(j), Tp.get(j) - Tp.get(i) - matrix[i][j]);
                    Rindependent.put(Integer.toString(i) + "-" + Integer.toString(j), Tp.get(j) - Tr.get(i) - matrix[i][j]);  
                }
        }
        reserve.add(Rfull);
        reserve.add(Rguaranteed);
        reserve.add(Rfree);
        reserve.add(Rindependent);
        return reserve;
    }

    //Вывод ранних и поздних сроков свершения событий
    private void print_Tp_Tr(ArrayList<Integer> Tp, ArrayList<Integer> Tr){
        System.out.println("_____________Сроки свершения событий______________");
        System.out.printf("%-13s  ", "Событие");
        System.out.printf("%-13s  ", "Ранние");
        System.out.printf("%-13s  ", "Поздние");
        System.out.printf("%-13s  ", "Резерв");
        System.out.println(" ");
        for(int i = 0; i < Tp.size(); i++){
            System.out.printf("%-15s  ", i);
            System.out.printf("%-14s  ", Tp.get(i));
            System.out.printf("%-13s  ", Tr.get(i));
            System.out.printf("%-5s  ", Tr.get(i) - Tp.get(i));
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    //Вывод резервов работы
    private void print_reserve(ArrayList<HashMap<String, Integer>> reserve){
        System.out.println("___________________Резервы работ_____________________");
        System.out.printf("%-5s  ", "Связь");
        System.out.printf("%-5s  ", "Полный");
        System.out.printf("%-5s  ", "Гарантированный");
        System.out.printf("%-5s  ", "Свободный");
        System.out.printf("%-5s  ", "Независимый");
        System.out.println(" ");
        Set<String> keys = reserve.get(0).keySet();

        for (String key : keys) {
            System.out.printf("%-8s  ", key);
            System.out.printf("%-10s  ", reserve.get(0).get(key));
            System.out.printf("%-12s  ", reserve.get(1).get(key));
            System.out.printf("%-10s  ", reserve.get(2).get(key));
            System.out.printf("%-5s  ", reserve.get(3).get(key));
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    //Вывод маршрута критического пути
    private void print_critical_path(ArrayList<Integer> critical_path){
        System.out.print("Критический путь: ");
        for(int i = 0; i < critical_path.size(); i++){
            if(i < critical_path.size() - 1)
                System.out.print(" " + critical_path.get(i) + " -> ");
            else
            System.out.print(" " + critical_path.get(i));
        }
        System.out.println(" ");
    }
    
    //Вычисление параметров сетевой модели
    public void calculate_parameters(int [][] matrix){
        if (check_matrix(matrix)){
            ArrayList<ArrayList<Integer> > previous = calculate_previous(matrix);

            ArrayList<Integer> Tp = calculate_Tp(matrix, previous);
            ArrayList<Integer> Tr = calculate_Tr(matrix, Tp);
            print_Tp_Tr(Tp, Tr);
    
            ArrayList<HashMap<String, Integer>> reserve = calculate_all_reserve(matrix, Tp, Tr);
            print_reserve(reserve);

            ArrayList<Integer> critical_path = calculate_critical_path(Tp, Tr);
            print_critical_path(critical_path);
        }
        else
            System.out.println("Неверный размер матрицы!");
    }

}