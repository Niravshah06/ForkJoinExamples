package com.nirav.forkJoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Sum extends RecursiveTask<Long>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int BaseBlockSize= 10;
	int lowerBound, upperBound;
	int[] data;
	
	Sum(int[] data, int lowerBound, int upperBound){
	this.data = data;
	this.lowerBound = lowerBound;
	this.upperBound= upperBound;
	}

	@Override
	protected Long compute() {
		if(upperBound-lowerBound <= BaseBlockSize) {
			long sum = 0;
			for(int i=lowerBound; i< upperBound; ++i) sum = sum + data[i];
			return sum;
			} 
		else
			{
			int mid = lowerBound + (upperBound-lowerBound) / 2; 
			Sum left = new Sum(data, lowerBound, mid);
			Sum right = new Sum(data, mid, upperBound);
			left.fork();
			right.fork();
			
			long rSum= right.join();
			long lSum= left.join();
			return lSum+ rSum;
			}

	}
	public static void main(String[] args) {
		int inputArray[] = new int[100];
		
		for(int j = 0; j < inputArray.length; j++)
			inputArray[j] = (int)(Math.random()*1000);
		
		ForkJoinPool fjPool= new ForkJoinPool();
		long sum = fjPool.invoke(new Sum(inputArray,0,inputArray.length));
		fjPool.getPoolSize();
		System.out.println(fjPool.getPoolSize()+ "   Sum = "+sum);
		System.out.println(Arrays.toString(inputArray));
		}

	

}
