package com.anish.calabashbros;

public class BubbleSorter<T extends Comparable<T>> implements Sorter<T> {

    private T[] a;
    private T[][] b;
    private boolean isMatrix;

    @Override
    public void load(T[] a) {
        this.a = a;
        isMatrix = false;
    }

    @Override
    public void load(T[][] b) {
        this.b = b;
        isMatrix = true;
    }

    private void swap(int i, int j) {
        T temp;
        if (!isMatrix) {
            temp = a[i];
            a[i] = a[j];
            a[j] = temp;
            plan += "" + a[i] + "<->" + a[j] + "\n";
        }
        else {
            int row = b.length;
            temp = b[i/row][i%row];
            b[i/row][i%row] = b[j/row][j%row];
            b[j/row][j%row] = temp;
            plan += "" + b[i/row][i%row] + "<->" + b[j/row][j%row] + "\n";
        }
    }

    private String plan = "";

    @Override
    public void sort() {
        boolean sorted = false;
        if (!isMatrix) {
            while (!sorted) {
                sorted = true;
                for (int i = 0; i < a.length - 1; i++) {
                    if (a[i].compareTo(a[i + 1]) > 0) {
                        swap(i, i + 1);
                        sorted = false;
                    }
                }
            }
        }
        else {
            while (!sorted) {
                sorted = true;
                int row = b.length;
                for (int i = 0; i < row * row - 1; i++) {
                    if (b[i/row][i%row].compareTo(b[(i+1)/row][(i+1)%row]) > 0) {
                        swap(i, i + 1);
                        sorted = false;
                    }
                }
            }
        }
    }

    @Override
    public String getPlan() {
        return this.plan;
    }

}