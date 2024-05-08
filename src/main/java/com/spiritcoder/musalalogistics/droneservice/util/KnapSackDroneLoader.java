package com.spiritcoder.musalalogistics.droneservice.util;

import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KnapSackDroneLoader implements DroneLoader {

    @Override
    public List<Medication> loadDrone(List<Medication> medicationList, Drone drone) {
        return loadDroneWithMedication(medicationList, drone);
    }

    public List<Medication> loadDroneWithMedication(List<Medication> medicationList, Drone drone){
        int[][] possibleMedicationLoadMatrix = generateMedicationLoadMatrix(medicationList, drone);
        return getLoadItemsFromMatrix(possibleMedicationLoadMatrix, medicationList.size(), (short) drone.getWeight(), medicationList);
    }

    public int[][] generateMedicationLoadMatrix(List<Medication> medicationList, Drone drone) {
        int numberOfItems = medicationList.size();
        int droneWeight = drone.getWeight();

        int[][] matrix = new int[numberOfItems + 1][droneWeight + 1];

        //initialize matrix
        for (int i = 0; i <= droneWeight; i++) {
            matrix[0][i] = 0;
        }

        // iterating through items
        for (int i = 1; i <= numberOfItems; i++) {
            // we iterate on each capacity
            for (int j = 0; j <= droneWeight; j++) {

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

        //select required items via load matrix
        for (int i = numberOfItems; i > 0  &&  result > 0; i--) {

            if (result != matrix[i-1][capacity]) {
                medicationItems.add(medicationList.get(i-1));
                // we remove items value and weight
                result -= medicationList.get(i-1).getWeight();
                capacity -= medicationList.get(i-1).getWeight();
            }
        }

        return medicationItems;
    }


}
