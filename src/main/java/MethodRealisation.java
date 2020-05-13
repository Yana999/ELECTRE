import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

public class MethodRealisation {
    private double[][] matrix; // матрица значений кртитериев и альтернатив (data в Python)
    private double[] scales; // массив длин шкал (L в Python)
    private int alternativesCount = 2; // количества альтернатив (a in Python)
    private int criteriesCount = 2;// количество критериев (k in Python)
    private double[][] c; // индекс согласи€ с гипотезой о превосходстве альтернатив (c_arr в Python)
    private double[][] d; //индекс несогласи€ с гипотезой о превосходстве (d_arr в Python)
    private double[] wieghts; //массив значений весов дл€ критериев (W в Python)
    private int[] suggestions; //массив гипотез (H в Python)
    private double alpha = 1;
    private double beta = 0;

    public void countScales(){ //вычисление длины шкалы (разница между макс и мин значени€ми критери€)
        scales = new double[matrix[0].length];
        for(int i = 0; i < matrix[0].length;i++){
            double max = matrix[0][i];
            double min = matrix[0][i];
            for(int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] < min) {
                    min = matrix[j][i];
                } else if (matrix[j][i] > max) {
                    max = matrix[j][i];
                }
            }
            scales[i] = max - min;
        }
    }

    private double countC(int i, int j, double[][] data, double[] W, int[] H){ // формула расчета гипотезы о прин€тии
        double[] i_alt = data[i];
        double[] j_alt = data[j];
        double w_sum = sum(W);
        double w = 0;
        for(int k=0; k<i_alt.length;k++){
            if (i_alt[k]*H[k] >= j_alt[k]*H[k]){
                w+=W[k];
            }
        }
        return (double)Math.round(100*w/w_sum)/100.0;
    }

    private double countD(int i, int j, double[][]data, double[] L, int[]H){ // формула расчета гипотезы о неприн€тии
        double[] i_alt = data[i];
        double[] j_alt = data[j];
        double[] delta = new double[4];
        for(int k=0; k < i_alt.length;k++) {
            if (j_alt[k] * H[k] > i_alt[k] * H[k]) {
                delta[k] = Math.abs((j_alt[k] - i_alt[k]) / L[k]);
            }
        }if (delta.length > 0) {
            return (double)Math.round(100*max(delta))/100.0;
        }
        return 0;
    }

    public void indexCount(){ // заполение таблиц с индексами
        c = new double[alternativesCount][alternativesCount];
        d = new double[alternativesCount][alternativesCount];
        for (int i = 0; i < alternativesCount; i++){
            for (int j = 0; j < alternativesCount; j++){
                c[i][j] = 0;
                d[i][j] = 0;
            }
        }

        for (int i = 0; i < alternativesCount; i++){
            for (int j = i+1; (j < alternativesCount); j++){
                c[i][j] = countC(i, j, matrix, wieghts, suggestions);
                c[j][i] = countC(j, i, matrix, wieghts, suggestions);
                d[i][j] = countD(i, j, matrix, scales, suggestions);
                d[j][i] = countD(j, i, matrix, scales, suggestions);
            }

        }
        System.out.println("c: ");
        for (int i = 0; i < alternativesCount; i++){
            for (int j = 0; j < alternativesCount; j++){
                System.out.print(c[i][j]);
            }
            System.out.println();
        }
        System.out.println("d: ");
        for (int i = 0; i < alternativesCount; i++){
            for (int j = 0; j < alternativesCount; j++){
                System.out.print(d[i][j]);
            }
            System.out.println();
        }
    }

    public String solution(TextField a, TextField b){
        indexCount();
        ArrayList<Integer> superrior_alt = new ArrayList<>();
        ArrayList<Integer> dome_alt = new ArrayList<>();
        ArrayList<Integer> Y = new ArrayList<>();
        alpha = Double.parseDouble(a.getText());
        beta = Double.parseDouble(b.getText());

        for(int i = 0; i < alternativesCount; i++){
            Y.add(i);
        }
        for (int i = 0; i < alternativesCount;i++){
            for(int j = i+1; j < alternativesCount; j++){
                if((c[i][j] >= alpha) && (d[i][j]) <= beta){
                    superrior_alt.add(i);
                    dome_alt.add(j);
                    if(Y.contains(j)){
                        Y.remove(Y.indexOf(j));
                    }
                }
                if((c[j][i] >= alpha) && (d[j][i] <= beta)){
                    superrior_alt.add(j);
                    dome_alt.add(i);
                    if(Y.contains(i)){
                        Y.remove(Y.indexOf(i));
                    }
                }
            }
        }
        String s ="–езультат: \n";
        for(int i = 0; (i < superrior_alt.size()) && (i < dome_alt.size()); i++){
            String r = String.format("јльтернатива %d ---> јльтернатива %s \n", superrior_alt.get(i), dome_alt.get(i));
            s += r;
        }
        s += "ядро недоминируемых альтрнатив: \n";
        for(int i = 0; i < Y.size(); i++){
            String r = String.format("јльтернатива %d \n", Y.get(i));
            s += r;
        }
        return s;
    }

    private double sum (double[] data){//посчитать сумму элементов массива
        double sum = 0;
        for (int i = 0; i < data.length; i++){
            sum+=data[i];
        }
        return sum;
    }
    private double max (double[] data){//найти наибольший элемент массива
        double max = 0;
        for (int i = 0; i < data.length; i++){
            if(max < data[i]){
                max = data[i];
            }
        }
        return max;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double[] getWieghts() {
        return wieghts;
    }

    public void setWieghts(double[] wieghts) {
        this.wieghts = wieghts;
    }

    public double[] getScales() {
        return scales;
    }

    public void setScales(double[] scales) {
        this.scales = scales;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public int getAlternativesCount() {
        return alternativesCount;
    }

    public void setAlternativesCount(int alternativesCount) {
        this.alternativesCount = alternativesCount;
    }

    public int getCriteriesCount() {
        return criteriesCount;
    }

    public void setCriteriesCount(int criteriesCount) {
        this.criteriesCount = criteriesCount;
    }

    public int[] getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(int[] suggestions) {
        this.suggestions = suggestions;
    }
}
