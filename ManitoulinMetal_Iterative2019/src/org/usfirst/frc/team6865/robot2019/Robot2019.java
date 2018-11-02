/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6865.robot2019;

//imports from WPI lib
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CameraServer;
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


// Method below contains all of robot code, before rest of methods, define the vars
public class Robot2019 extends IterativeRobot {
	
	//define vars of drive type, joy sticks, motor controllers and where the motors are
	private DifferentialDrive Move = new DifferentialDrive(new Spark(0), new Spark(1));
	private Joystick xBox = new Joystick(0);
	private Joystick bigJ = new Joystick(1);
	private Spark belt = new Spark(6);
	private Spark gate = new Spark(5);
	
	
	//define the auto controls on the dashboard
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	//define the game data
	private String GameData = "EEE";
	
	//Set a timer for auto programming
	public Timer time = new Timer();
	
	
	/* This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
//Method below runs when robot is started, initialized
	@Override
	public void robotInit() {
		//adds the options for auto on the dashboard. 
		//addDefualt is do define which is selected without any change
		//addObject is to add other selections
		m_chooser.addDefault("Left", "1");
		m_chooser.addObject("Center", "2");
		m_chooser.addObject("Right", "3");
		SmartDashboard.putData("Auto choices", m_chooser);
		
		//Code for Cameras on USB
		//Arguments are name of dev and device number
		CameraServer.getInstance().startAutomaticCapture("cam1", 2);
		
		//PDP code for CAN, the CAN ID has to be 0 for pdp
		PowerDistributionPanel pdp = new PowerDistributionPanel();
		//Get the temp(C), Volts, Current(A), Energy(J), Power(W) of the pdp
		pdp.getTemperature();
		pdp.getVoltage();
		pdp.getTotalCurrent();
		pdp.getTotalEnergy();
		pdp.getTotalPower();
		//Get the Current(A) of components channel 0-15
		pdp.getCurrent(0);
		
		//Code for Talon SRX
		
		
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
	//Method below is called when auto is started, initialized 
	@Override
	public void autonomousInit() {
		// Get the what was chosen pre-match, start up of code
		m_autoSelected = m_chooser.getSelected();
		System.out.println("Auto selected: " + m_autoSelected);
		
		//Get the required info from the field 
		GameData = DriverStation.getInstance().getGameSpecificMessage();
		
		//start the timer for auto now(make last thing in the init phase)
		time.start();
		auto = true;
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	
	// Method below will be called continuously throughout auto.
	// two vars out of function is to stop the periodic after one run
	public boolean step = true;
	
	private boolean auto = true;
	public void autonomousPeriodic() {

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

//Method below is called when the the teleop phase starts, initializes
	public void teleopInit() {
		//set the drive to not move at all in case still moving from auto
		Move.arcadeDrive(0, 0);
		belt.set(0);
	}
	
	/**
	 * This function is called periodically during operator control.
	 */
	//function is repeadtly called through teleop phase
	@Override
	public void teleopPeriodic() {
		//Set vars, names of the inputs
		double beltMove = xBox.getY();
		double xAxis = bigJ.getX();
		double yAxis = bigJ.getY();
		
		//Belt movement, if no input don't move it
		if(Math.abs(beltMove) > 0.06) {
			belt.set(beltMove);
		} else {
			belt.set(0);
		}
		
		//Gate movement, if no input don't move the motor
		if(xBox.getRawButton(3) == true) {
			gate.set(0.8);
		}
		else if(xBox.getRawButton(2) == true) {
			gate.set(-0.8);
		}
		else {
			gate.set(0.0);
		}
		
		/*Robot movement
		 * the if statement will check both axis to see if they are 
		 * over a threshold value. This value is important as it 
		 * won't let the robot move if the controller receives tiny vibrations
		 * if no input, no movement.
		*/
		if(Math.abs(xAxis) > 0.05 || Math.abs(yAxis) > 0.05) {
			Move.arcadeDrive(-yAxis, xAxis);
		}
		else {
			Move.arcadeDrive(0, 0);
		}
		
		//Output to the dash the location of the axis to check they are functioning properly
		SmartDashboard.putNumber("yAxis", yAxis);
		SmartDashboard.putNumber("XAxis", xAxis);
	}
	
	/**
	 * This function is called periodically during test mode.
	 */
	//This can be used for simple testing of components as it is a separate tab in the driver station
	@Override
	public void testPeriodic() {
	}
}
