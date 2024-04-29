package com.spiritcoder.musalalogistics.drone.util;

import com.spiritcoder.musalalogistics.medication.entity.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is built using the Knapsack algorithm.
 */
public class DroneLoaderUtil {

    public int[][] generateMedicationLoadMatrix(List<Medication> medicationList, int droneWeights) {
        int numberOfItems = medicationList.size();
        int[][] matrix = new int[numberOfItems + 1][droneWeights + 1];

        //initialize matrix
        for (int i = 0; i <= droneWeights; i++) {
            matrix[0][i] = 0;
        }

        for (int i = 1; i <= numberOfItems; i++) {

            for (int j = 0; j <= droneWeights; j++) {
                if (medicationList.get(i-1).getWeight() > j) {
                    matrix[i][j] = matrix[i - 1][j];
                }
                else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - medicationList.get(i-1).getWeight()]
                            + medicationList.get(i-1).getWeight());
                }
            }
        }

        return matrix;
    }

    public List<Medication> getLoadItemsFromMatrix(int[][] matrix, int numberOfItems, short capacity, List<Medication> medicationList){

        List<Medication> medicationItems = new ArrayList<>();
        int result = matrix[numberOfItems][capacity];

        for (int i = numberOfItems; i > 0  &&  result > 0; i--) {

            if (result != matrix[i-1][capacity]) {
                medicationItems.add(medicationList.get(i-1));
                result -= medicationList.get(i-1).getWeight();
                capacity -= medicationList.get(i-1).getWeight();
            }
        }

        //TODO:: remove after test
        System.out.println("unutilized drone capacity " + capacity);
        return medicationItems;
    }
}
