package com.licoforen.parentalcontrollauncher.Helpers;

import com.licoforen.parentalcontrollauncher.MainActivity;

public class SortApps {
	
	MainActivity.Pack[] packs;
	int packNum;

	public void sort(MainActivity.Pack[] values) {
		if (values == null || values.length == 0) {
			return;
		}
		this.packs = values;
		packNum = values.length;
		quicksort(0, packNum - 1);
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;
		MainActivity.Pack pivot = packs[low + (high - low) / 2];

		while (i <= j) {
			while (packs[i].label.compareToIgnoreCase(pivot.label) < 0) {
				i++;
			}
			while (packs[j].label.compareToIgnoreCase(pivot.label) > 0) {
				j--;
			}
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private void exchange(int i, int j) {
		MainActivity.Pack temp = packs[i];
		packs[i] = packs[j];
		packs[j] = temp;
	}

}
