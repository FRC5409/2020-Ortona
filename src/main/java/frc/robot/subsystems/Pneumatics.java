package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Pneumatics extends SubsystemBase {
  /**
   * Creates a new Pneumatics.
   */
  Compressor compressor1;

  private boolean m_autoFill = false;
  private boolean m_manualAutoFillOverride = false;

  /**
   * Constructor for the Pneumatics class
   */
  public Pneumatics() {
    compressor1 = new Compressor(Constants.Pneumatics.MODULE, PneumaticsModuleType.CTREPCM);
    startLoop();
  }

  /**
   * This method will be called once per scheduler run
   */
  @Override
  public void periodic() {

    if (!m_manualAutoFillOverride) {
      // Check if pressure is too low or too high
      if (compressor1.getPressure() <= Constants.Pneumatics.MIN_PSI && !m_autoFill) {
        startLoop();
      } else if (compressor1.getPressure() >= Constants.Pneumatics.MAX_PSI) {
        endLoop();
      }
    }
  }

  /**
   * This method will set the closed loop to true.
   */
  private void startLoop() {
    compressor1.enableDigital();
  }

  /**
   * This method will set the closed loop to be false.
   */
  private void endLoop() {
    compressor1.disable();
  }

  /**
   * This method will close the compressor loop control if it is open and open it
   * if it is closed.
   */
  public void toggle() {
    if (compressor1.enabled())
      endLoop();
    else
      startLoop();
  }

  /**
   * This method will close the compressor.
   */
  public void turnOff() {
    compressor1.close();
  }

  /**
   * This method will set the value of m_manualAutoFillOverride
   * 
   * @param state New value of m_manualAutoFillOverride
   */
  public void setManualOverride(boolean state) {
    m_manualAutoFillOverride = state;
  }
}