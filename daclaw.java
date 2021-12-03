package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "The claw! Ooooooooh!")
public class daclaw extends LinearOpMode {
    DcMotor motor1;
    //Buttons for wheel
    boolean a, b;
    //Joystick values
    double x;
    double y;
    double p;
    //bumper and buttons (left) for cascading lift and claw
    boolean a1, b1, a2, b2;
    //Wheel speed
    boolean up, down, left, right;
    double speed;
    DcMotor motor;
    boolean wheelon = false;
    double x2;
    // x is value of wheels, back is -x,  y+x y-x
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor wheel;
    @Override
    public void runOpMode() throws InterruptedException{
        motor1 = hardwareMap.dcMotor.get("claw");
        motor = hardwareMap.dcMotor.get("motor");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        wheel = hardwareMap.dcMotor.get("wheel");
        //Setting lift to encoders
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()){
            b1 = gamepad2.right_bumper;
            a1 = gamepad2.left_bumper;
            a2 = gamepad2.a;
            b2 = gamepad2.b;
            
            //Open/Close claw
            if(a1){
                motor1.setPower(0.25);
                sleep(250);
                motor1.setPower(0);
            }
            if (b1){
                motor1.setPower(-0.25);
                sleep(250);
                motor1.setPower(0);
            }
            //Move lift
            if(a2){
                movingvoid(800);
            }
            if(b2){
                movingvoid(-800);
            }
            //Extra driving code (working)
            a1 = gamepad2.a;
            b1 = gamepad2.b;
            y = gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            x2 = gamepad1.right_stick_x;
            p = gamepad2.left_stick_y;
            a = gamepad1.a;
            b = gamepad1.b;
            up = gamepad1.dpad_up;
            down = gamepad1.dpad_down;
            left = gamepad1.dpad_left;
            right = gamepad1.dpad_right;

            //Setting wheel speeds
            if(up){
                speed = 0.8;
            }
            if(down){
                speed = 0.4;
            }
            if(left){
                speed = 0.6;
            }
            if(right){
                speed = 0.6;
            }
            if(a && wheelon){
                wheel.setPower(-speed);
                wheelon = false;
            }
            if(a && !wheelon){
                wheel.setPower(0);
                wheelon = true;
            }
            if(b && wheelon){
                wheel.setPower(speed);
                wheelon = false;
            }
            if(b && !wheelon){
                wheel.setPower(0);
                wheelon = true;
            }
            //Run to position ticks
            //frontLeft.setPower(Range.clip(-y - (0.5 * x), -1.0, 1.0));
            //frontRight.setPower(Range.clip(y - (0.5 * x), -1.0, 1.0));
            //backLeft.setPower(Range.clip(-y + (0.5 * x), -1.0, 1.0));
            //backRight.setPower(Range.clip(y + (0.5 * x), -1.0, 1.0));
            //Mecanum with only one happening per instance
            if(abs(x) > abs(y) && abs(x) > abs(x2)){
                frontLeft.setPower(Range.clip(-x, -1.0, 1.0));
                frontRight.setPower(Range.clip(x, -1.0, 1.0));
                backLeft.setPower(Range.clip(-x, -1.0, 1.0));
                backRight.setPower(Range.clip(-x, -1.0, 1.0));
            } else if(abs(y) > abs(x) && abs(y) > abs(x2)){
                frontLeft.setPower(Range.clip(y, -1.0, 1.0));
                frontRight.setPower(Range.clip(y, -1.0, 1.0));
                backLeft.setPower(Range.clip(y, -1.0, 1.0));
                backRight.setPower(Range.clip(-y, -1.0, 1.0));
            } else if(abs(x2) > abs(x) && abs(x2) > abs(y)){
                frontLeft.setPower(Range.clip(-x2, -1.0, 1.0));
                frontRight.setPower(Range.clip(x2, -1.0, 1.0));
                backLeft.setPower(Range.clip(x2, -1.0, 1.0));
                backRight.setPower(Range.clip(x2, -1.0, 1.0));
            }
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
            wheel.setPower(0);
        }
    }
    public void movingvoid(double ticks){
        int newTarget;
        double speed2 = 1;
        if(opModeIsActive() && motor.getCurrentPosition() >= 0 && motor.getCurrentPosition() <= 3190){
            newTarget = motor.getCurrentPosition() + (int)ticks;
            motor.setTargetPosition(newTarget);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(Math.abs(speed2));


            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            telemetry.addData("Height", motor.getCurrentPosition());
            telemetry.update();
        }
        /*
        make sure opmode is active
        find current position and add distance to move
        set target position of motor
        run to that position

        make sure that power is not negative (abs)

        stop all motors

        set to run using encoders

        wait a quarter second (for accuracy, so that the program doesn't make the robot move in an unwanted direction)
        */
    }
}
