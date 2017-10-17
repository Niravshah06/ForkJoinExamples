package com.nirav.forkJoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * class which implements merge and insertion Sort to increase efficiency
 *
 */
public class MergeSort extends RecursiveTask<int[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int BaseBlockSize = 100;
	int lb, ub;
	int[] f;
	int[] result = null;

	/**
	 * public constructor
	 * 
	 * @param f
	 * @param lowerBound
	 * @param upperBound
	 */
	public MergeSort(int[] f, int lowerBound, int upperBound)

	{
		this.f = f;
		lb = lowerBound;
		ub = upperBound;

	}

	/**
	 * method do insertion sort
	 * 
	 * @param array
	 */
	public static int[] insertionSort(int[] array) {

		int temp;
		for (int i = 1; i < array.length; i++) {
			for (int j = i; j > 0; j--) {
				if (array[j] < array[j - 1]) {
					temp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = temp;
				}
			}
		}
		return array;

	}

	/**
	 * compute method of RecursiveTask to be overridden
	 */
	@Override
	protected int[] compute() {
		if (ub - lb <= BaseBlockSize) {
			// do insertion sort if we have array size of 100
			int b1[] = Arrays.copyOfRange(f, lb, ub);
			insertionSort(b1);
			return b1;
		}

		else {
			int mid = (lb + ub) / 2;
			MergeSort left = new MergeSort(f, lb, mid);
			MergeSort right = new MergeSort(f, mid, ub);
			left.fork();
			right.fork();
			int[] lArray = (int[]) left.join();
			int[] rArray = (int[]) right.join();
			// merge both array
			int[] ou = mergeTwoArray(lArray, rArray);

			merge(ou, 0, ou.length / 2, ou.length);
			return ou;

		}
	}

	public int[] mergeTwoArray(int[] arr1, int[] arr2) {
		int res[] = null;
		if (null == arr2) {
			res = arr1;
			return res;

		}
		res = new int[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, res, 0, arr1.length);
		System.arraycopy(arr2, 0, res, arr1.length, arr2.length);
		return res;
	}

	/**
	 * driver method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int a[] = new int[10000];

		for (int j = 0; j < a.length; j++)
			a[j] = (int) (Math.random() * 1000);

		// System.out.println(Arrays.toString(f));
		System.out.println("input:\n" + Arrays.toString(a));
		ForkJoinPool fjPool = new ForkJoinPool();
		int output[] = fjPool.invoke(new MergeSort(a, 0, a.length));
		// System.out.println(fjPool.getPoolSize());
		System.out.println("output:\n" + Arrays.toString(output));

		// to verify if output of program is proper
		/*
		 * List<Integer> intList = new ArrayList<Integer>(); for (int index = 0; index <
		 * a.length; index++) { intList.add(a[index]); } Collections.sort(intList);
		 * System.out.println("outp"+"\n"+intList);
		 */

	}

	/**
	 * merge method of mergeSort
	 * 
	 * @param f
	 * @param p
	 * @param q
	 * @param r
	 */
	static void merge(int f[], int p, int q, int r) {
		// p<=q<=r
		int i = p;
		int j = q;
		// use temp array to store merged sub-sequence
		int temp[] = new int[r - p];
		int t = 0;
		while (i < q && j < r) {
			if (f[i] <= f[j]) {
				temp[t] = f[i];
				i++;
				t++;
			} else {
				temp[t] = f[j];
				j++;
				t++;
			}
		}
		// tag on remaining sequence
		while (i < q) {
			temp[t] = f[i];
			i++;
			t++;
		}
		while (j < r) {
			temp[t] = f[j];
			j++;
			t++;
		}
		// copy temp back to f
		i = p;
		t = 0;
		while (t < temp.length) {
			f[i] = temp[t];
			i++;
			t++;
		}
	}
}
