package pgdp.threads;

import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort extends RecursiveAction {
    final Comparable[] array;
    final Comparable[] helper;
    final int low;
    final int high;

    public ParallelMergeSort(Comparable[] array) {
        this(array, new Comparable[array.length], 0, array.length - 1);
    }

    public ParallelMergeSort(final Comparable[] array, final Comparable[] helper, final int low, final int high) {
        this.array = array;
        this.helper = helper;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (low < high) {
            final int middle = (low + high) / 2;
            ParallelMergeSort first = new ParallelMergeSort(array, helper, low, middle);
            ParallelMergeSort second = new ParallelMergeSort(array, helper, middle + 1, high);

            first.fork();
            second.fork();
            second.join();
            first.join();

            for (int i = low; i <= high; i++) {
                helper[i] = array[i];
            }

            int helperLeft = low;
            int helperRight = middle + 1;
            int current = low;

            while (helperLeft <= middle && helperRight <= high) {
                if (helper[helperLeft].compareTo(helper[helperRight]) <= 0) {
                    array[current] = helper[helperLeft++];
                } else {
                    array[current] = helper[helperRight++];
                }
                current++;
            }

            while (helperLeft <= middle) {
                array[current++] = helper[helperLeft++];
            }
        }
    }
}
