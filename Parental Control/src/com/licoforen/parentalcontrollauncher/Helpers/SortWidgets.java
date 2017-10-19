package com.licoforen.parentalcontrollauncher.Helpers;

import java.util.List;

import android.appwidget.AppWidgetProviderInfo;

public class SortWidgets {

	List<AppWidgetProviderInfo> packs;
	int packNum;

	public void sort(List<AppWidgetProviderInfo> values) {
		if (values == null || values.size() == 0) {
			return;
		}
		this.packs = values;
		packNum = values.size();
		quicksort(0, packNum - 1);
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;
		AppWidgetProviderInfo pivot = packs.get(low + (high - low) / 2);

		while (i <= j) {
			while (packs.get(i).label.compareToIgnoreCase(pivot.label) < 0) {
				i++;
			}
			while (packs.get(j).label.compareToIgnoreCase(pivot.label) > 0) {
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
		AppWidgetProviderInfo temp = packs.get(i);
		packs.set(i, packs.get(j));
		packs.set(j, temp);
	}

}
