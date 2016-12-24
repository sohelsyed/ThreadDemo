package com.sohel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class AddNos extends RecursiveTask<Long> {

	int arr[];
	int start;
	int finish;

	public AddNos(int[] arr, int start, int finish) {
		super();
		this.arr = arr;
		this.start = start;
		this.finish = finish;
	}

	public AddNos(int[] arr2) {
		this(arr2, 0, arr2.length);
	}

	@Override
	protected Long compute() {

		System.out.println(" Thread adding "+start + " to " + finish+ " " +Thread.currentThread().getName() );
		
		long result = 0;

		if (finish - start < 50) {
			// Do Not Split
			for (int i = start; i < finish; i++) {
				result = result + arr[i];
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}

		int mid = (start+finish)/2;
		
		AddNos p1 = new AddNos(arr, start , mid);
		p1.fork();
		AddNos p2 = new AddNos(arr, mid , finish);
		p2.fork();
		return p1.join()+p2.join();
	}

	public static void main(String[] args) {
		int[] arr = IntStream.range(1, 500).toArray();
		ForkJoinPool pool = new ForkJoinPool(5);
		AddNos addNos = new AddNos(arr);
		pool.invoke(addNos);

		System.out.println("Actual Result " + Arrays.stream(arr).sum());

		System.out.println("A");
		System.out.println(addNos.join());
		System.out.println("B");

		try {
			System.out.println(addNos.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
