/*
 * Sort Algorithm: Quick Sort on List<String>.
 * 	- List element: 19601001001 (original: 001 10 1  1960)
 * 	- Sort on the whole string element (ID can also be sorted).
 *  - (We can also sort on date part using substring(0,7).
 *  - First merge year, month, day and Id together as one string.
 * 	- Make sure each number has same digits by adding "0" at the beginning.
 *  - Time Complexity: O(nlogn)
 *  - Space Complexity: O(n)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MyThread extends Thread {
	private Thread t;
	private int Id;

	MyThread(int Id) {
		this.Id = Id;
	}

	// This method is used to swap the values between the two given index
	public static void swap(List<String> strList, int left, int right) {
		String temp = strList.get(left);
		strList.set(left, strList.get(right));
		strList.set(right, temp);
	}

	// This method is used to partition the given array and returns the integer
	// which points to the sorted pivot index
	private static int partition(List<String> strList, int left, int right) {
		String pivot = strList.get(left);

		while (left <= right) {
			// searching number which is greater than pivot, bottom up
			while (strList.get(left).compareTo(pivot) < 0) {
				left++;
			}
			// searching number which is less than pivot, top down
			while (strList.get(right).compareTo(pivot) > 0) {
				right--;
			}

			// swap the values
			if (left <= right) {
				swap(strList, left, right);

				// increment left index and decrement right index
				left++;
				right--;
			}
		}
		return left;
	}

	public void quickSort(List<String> strList, int left, int right) {
		int index = partition(strList, left, right);

		// Recursively call quickSort with left part of the partitioned array
		if (left < index - 1) {
			quickSort(strList, left, index - 1);
		}

		// Recursively call quick sort with right part of the partitioned array
		if (right > index) {
			quickSort(strList, index, right);
		}
	}

	public void mySort(List<String> strList) {
		if (strList.isEmpty())
			return;

		quickSort(strList, 0, strList.size() - 1);
	}

	public void write(String result) {
		synchronized (System.out) {
			try {
				System.out.println("Thread ID Person ID\tMonth\tDay\tYear");
				System.out.print(result);
			} catch (Exception e) {
				System.out.println("Thread  interrupted.");
			}
		}
	}

	public void run() {
		try {

			File file = new File("input.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			List<String> sortList = new ArrayList<String>();
			while ((line = bufferedReader.readLine()) != null) {
				if (line.trim().length() == 0) {
					continue; // Skip blank lines
				}
				String[] parts = line.split("(\\s+|\\t)");
				if (parts[1].length() == 1)
					parts[1] = "0" + parts[1];
				if (parts[2].length() == 1)
					parts[2] = "0" + parts[2];
				sortList.add(parts[3] + " " + parts[1] + " " + parts[2] + " "
						+ parts[0]);
			}
			fileReader.close();
			mySort(sortList);
			for (int i = 0; i < sortList.size(); i++) {
				stringBuffer.append("Thread " + Id + ":\t");
				String[] parts = sortList.get(i).split(" ");
				if (parts[1].charAt(0) == '0')
					parts[1] = parts[1].substring(1);
				if (parts[2].charAt(0) == '0')
					parts[2] = parts[2].substring(1);
				stringBuffer.append(parts[3] + "\t" + parts[1] + "\t"
						+ parts[2] + "\t" + parts[0]);
				stringBuffer.append("\n");
			}
			write(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("Thread " + this.Id + " exiting.");
	}

	public void start() {
		// System.out.println("Starting Thread " + Id);
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

}

public class MultiThreads {
	public static void main(String args[]) {

		MyThread T1 = new MyThread(1);
		MyThread T2 = new MyThread(2);
		// MyThread T3 = new MyThread(3);

		T1.start();
		T2.start();
		// T3.start();

		// wait for threads to end
		try {
			T1.join();
			T2.join();
			// T3.join();
		} catch (Exception e) {
			System.out.println("Interrupted");
		}
	}
}