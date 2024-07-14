package org.firstinspires.ftc.teamcode.Utils;

public class UtilMethods {
    public static void scaleTo1(double[] nums){
        //finds the maximum number in the array "nums"
        double max = 0;
        for(double num : nums){
            if(Math.abs(num) > 0){
                max = Math.abs(num);
            }
        }

        //if max is greater than 1, divide all items in "nums" by the max to scale everything to be in the range [-1, 1]
        if(max > 1){
            for (int i = 0; i < nums.length; i++) {
                nums[i] = nums[i] / max;
            }
        }
    }
}
