package org.usfirst.frc.team175.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

import org.usfirst.frc.team175.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Arvind
 */
public class Climber extends Subsystem {

	/* Declarations */
	// Talon SRs
	private Talon mClimbExtend;
	private Talon mWinch;
	
	// Solenoid
	private Solenoid mClimbAlign;

	// Limit Switches
	private DigitalInput mClimbUpLimit;
	private DigitalInput mClimbDownLimit;

	// Enums
	public enum ClimberState {
		EXTEND, RETRACT, IDLE
	}

	public enum WinchState {
		WIND_UP, WIND_OUT, IDLE
	}

	public Climber() {
		/* Instantiations */
		// Talon(pwmIO : int)
		mClimbExtend = new Talon(RobotMap.CLIMB_EXTEND_PORT);
		mWinch = new Talon(RobotMap.WINCH_PORT);

		// Solenoid(canID : int, channel : int)
		mClimbAlign = new Solenoid(RobotMap.CLIMB_ALIGN_PORT, RobotMap.CLIMB_ALIGN_CHANNEL);

		// DigitalInput(io : int)
		mClimbUpLimit = new DigitalInput(RobotMap.CLIMB_UP_LIMIT_PORT);
		mClimbDownLimit = new DigitalInput(RobotMap.CLIMB_DOWN_LIMIT_PORT);

		// Set climber inwards
		mClimbAlign.set(false);
	}

	public void setPosition(ClimberState climberState) {
		switch (climberState) {
			case EXTEND:
				mClimbExtend.set(!mClimbUpLimit.get() ? 1 : 0);
				// mClimbExtend.set(mClimbDownLimit.get() ? 1 : 0);
				// mClimbExtend.set(1);
				break;
			case RETRACT:
				mClimbExtend.set(!mClimbDownLimit.get() ? -1 : 0);
				// mClimbExtend.set(mClimbUpLimit.get() ? -1 : 0);
				// mClimbExtend.set(-1);
				break;
			case IDLE:
				mClimbExtend.set(0);
				break;
		}
	}

	public void setManual(double speed) {
		mClimbExtend.set(speed);
	}

	public boolean isExtended() {
		return mClimbUpLimit.get();
	}

	public boolean isRetracted() {
		return mClimbDownLimit.get();
	}

	public void winch(WinchState winchState) {
		switch (winchState) {
			case WIND_UP:
				mWinch.set(1);
				break;
			case WIND_OUT:
				mWinch.set(-1);
				break;
			case IDLE:
				mWinch.set(0);
				break;
		}
	}

	public void winchManual(double speed) {
		mWinch.set(speed);
	}

	// TODO: Determine whether this is necessary or not
	public double getWinchSpeed() {
		return mWinch.get();
	}

	public void align(boolean align) {
		mClimbAlign.set((mWinch.get() == 0) ? align : false);
	}

	public boolean isAligned() {
		return mClimbAlign.get();
	}

	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Climber Percent Power", mClimbExtend.get());
		SmartDashboard.putNumber("Winch Percent Power", mWinch.get());

		SmartDashboard.putBoolean("Climber State", mClimbAlign.get());
		SmartDashboard.putBoolean("Extended all the way?", mClimbUpLimit.get());
		SmartDashboard.putBoolean("Retracted all the way?", mClimbDownLimit.get());
	}

	public void initDefaultCommand() {
		// TODO: Set the default command, if any, for a subsystem here. Example:
		// setDefaultCommand(new MySpecialCommand());
	}

}
