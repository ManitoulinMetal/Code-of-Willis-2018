/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6865.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	
	private DifferentialDrive Move = new DifferentialDrive(new Spark(0), new Spark(1));
	private Joystick xBox = new Joystick(0);
	private Joystick bigJ = new Joystick(1);
	
	private Spark belt = new Spark(6);
	private Spark gate = new Spark(5);
	
	
//	Spark leftDrive = new Spark(0);
	//Spark rightDrive = new Spark(1);
	/*
	Spark rightDrive1 = new Spark(2);
	Spark rightDrive2 = new Spark(3);
	*/
	/*
	private JoystickButton beltForward = new JoystickButton(xBox, 1);
	private JoystickButton beltReverse = new JoystickButton(xBox, 2);
	private JoystickButton beltForwardHalf = new JoystickButton(xBox, 3);
	private JoystickButton beltReverseHalf = new JoystickButton(xBox, 4);
	*/
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	private String GameData = "EEE";
	
	private String autoSelect = "0";
	
	public Timer time = new Timer();
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Left", "1");
		m_chooser.addObject("Center", "2");
		m_chooser.addObject("Right", "3");
		SmartDashboard.putData("Auto choices", m_chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
		m_autoSelected = m_chooser.getSelected();
		 //autoSelected = SmartDashboard.getString("Auto Selector",
		 //defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		GameData = DriverStation.getInstance().getGameSpecificMessage();
		
		time.start();
		auto = true;
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	
	
	public boolean step = true;
	
	private boolean auto = true;
	public void autonomousPeriodic() {
			
		//Bassic
		/*
		if(time.get() < 3)
		{
		Move.arcadeDrive(0.6, 0);
		}
		else
		{
			Move.arcadeDrive(0, 0);
		}
		
		*/
	
		

		
		if(auto == true) {
			
			
			//ROBOT LEFT
			
			if(m_autoSelected.equals("1")) {
				//Switch Left
				if(GameData.startsWith("L")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
					
					if(time.get() > 3.5) {
						Move.arcadeDrive(0, 0);
						belt.set(0.4);
					}
				}
				//Switch Right
				if(GameData.startsWith("R")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
							
			}
			
			
			
			//ROBOT MIDDLE AKA CENTER
			
			if(m_autoSelected.equals("2")) {
				//Switch Left
				if(GameData.startsWith("L")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
				//Switch Right
				if(GameData.startsWith("R")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
							
			}
			
			
			//ROBOT RIGHT
			
			if(m_autoSelected.equals("3")) {
				//Switch Right
				if(GameData.startsWith("R")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
					
					if(time.get() > 3.5) {
						Move.arcadeDrive(0, 0);
						belt.set(0.4);
					}
				}
				//Switch Left
				if(GameData.startsWith("L")) {
					if(time.get() < 3.5) {
						Move.arcadeDrive(-0.6, 0.2);
					}
				}
			}					
		}
	} 			




				
			
	public void teleopInit() {

		Move.arcadeDrive(0, 0);
	}
	
	/*To move the belt
	 * pre: The velocity for the motor
	 * post the equal output to the motor
	 */
	public Command BeltMove(double movement) {
	 belt.set(movement);
	return null;
	}
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		double beltMove = xBox.getY();
		
		if(xBox.getRawButton(3) == true) {
			gate.set(0.8);
		}
		else if(xBox.getRawButton(2) == true) {
			gate.set(-0.8);
		}
		else {
			gate.set(0.0);
		}
		/*
		double deadZone = 0.05;
		double deadZoneBelt = 0.02; 
		
		if(bigJ.getY() > deadZone || bigJ.getX() > deadZone) 
		{
		Move.arcadeDrive(bigJ.getY(), bigJ.getX());
		}
		*/
		
		double xAxis = bigJ.getX();
		double yAxis = bigJ.getY();
		
		if(Math.abs(xAxis) > 0.05 || Math.abs(yAxis) > 0.05) {
			Move.arcadeDrive(-yAxis, xAxis);
			
			/*leftDrive1.set(yAxis + xAxis);
			leftDrive2.set(yAxis + xAxis);
			rightDrive1.set(yAxis - xAxis);
			rightDrive2.set(yAxis - xAxis);
			*/
		}
		
		else {
			Move.arcadeDrive(0, 0);
		}
		
		SmartDashboard.putNumber("yAxis", yAxis);
		SmartDashboard.putNumber("XAxis", xAxis);
		/*
		beltForward.whileHeld(BeltMove(0.54));
		beltReverse.whileHeld(BeltMove(-0.54));
		beltForwardHalf.whileHeld(BeltMove(-0.27));
		beltReverseHalf.whileHeld(BeltMove(-0.27));
		*/
		
		if(Math.abs(beltMove) > 0.06) {
			BeltMove(beltMove);
		} else {
			BeltMove(0);
		}
		
		

	}
	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
