package org.firstinspires.ftc.teamcode.Utils;

public class Pose {
    private double x;
    private double y;
    private double theta;

    public Pose(double startX, double startY, double startTheta){
        x = startX;
        y = startY;
        theta = startTheta;
    }

    public Pose(){
        x = 0;
        y = 0;
        theta = 0;
    }

    public void updatePose(double dX, double dY, double dTheta){
        x += dX;
        y += dY;
        theta += dTheta;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }
}
