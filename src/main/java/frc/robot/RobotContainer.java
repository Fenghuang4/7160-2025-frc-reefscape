package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot
 * (including subsystems, commands, and button mappings) should be declared
 * here.
 */
@Logged
public class RobotContainer {

  private static RobotContainer m_robotContainer = new RobotContainer();

  // The robot's subsystems
  public final ClimberSubsystem m_climberSubsystem         = new ClimberSubsystem();
  public final ElevatorSubsystem m_elevatorSubsystem       = new ElevatorSubsystem();
  public final ManipulatorSubsystem m_manipulatorSubsystem = new ManipulatorSubsystem();
  public final DriveBaseSubsystem m_driveBaseSubsystem     = new DriveBaseSubsystem();
  public final AllianceLandmarks m_landmarks               = new AllianceLandmarks();

  // Joysticks
  public final CommandJoystick m_driverController = new CommandJoystick( 0 );

  private Alliance      currentAlliance;
  // A chooser for autonomous commands
  private final SendableChooser<Command> m_chooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  private RobotContainer() {
    // Smartdashboard Subsystems
    SmartDashboard.putData(m_driveBaseSubsystem);

    // SmartDashboard Buttons
    SmartDashboard.putData("AutonomousCommand", new AutonomousCommand(m_driveBaseSubsystem));

    // Register named commands to PathPlanner
    NamedCommands.registerCommand("ElevatorGoToCommand", m_elevatorSubsystem.goTo(50));

    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    m_driveBaseSubsystem.setDefaultCommand( m_driveBaseSubsystem.moveManual(() -> m_driverController.getRawAxis(1) * m_landmarks.joystickInversion, () -> m_driverController.getRawAxis(0) * m_landmarks.joystickInversion, () -> m_driverController.getRawAxis(4)));


    // Build an auto chooser. This will use Commands.none() as the default option.
    m_chooser = AutoBuilder.buildAutoChooser();

    // Another option that allows you to specify the default auto by its name
    // autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");

    SmartDashboard.putData("Auto Chooser", m_chooser);
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Create some buttons

    System.out.println("configureButtonBindings");
    new Trigger( m_driverController.button( 1 ) ).whileTrue( m_driveBaseSubsystem.getAngleMotorTestCommand() );
    //new Trigger( m_driverController.button( 2 ) ).whileTrue( new DriveAngleSetCommand(new Rotation2d( 0.0 )));
  }

  public void configureTestButtonBindings()
  {
    //new Trigger( m_driverController.button( 1 ) ).whileTrue( new TestLoggerCommand() );
    //new Trigger( m_driverController.button( 1 ) ).whileTrue( m_driveBaseSubsystem.getAngleMotorTestCommand() );
    //new Trigger( m_driverController.button( 2 ) ).whileTrue( m_driveBaseSubsystem.getDriveMotorTestCommand() );
  }

  
   public void opmodeInit( Alliance new_alliance )
   {
      currentAlliance = new_alliance;
      m_landmarks.newAlliance(currentAlliance);
   }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return m_chooser.getSelected();
  }

}
