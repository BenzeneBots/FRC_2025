package frc.robot;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.controls.MotionMagicVoltage;

public class RobotConstants {
    public static class CoralPivotConstants{
        public static final int CoralPivotDeviceID = 60;

        public static final Slot0Configs CoralPivotPID = new Slot0Configs()
            .withGravityType(GravityTypeValue.Arm_Cosine)
            .withKG(0.5)
            .withKP(0)
            .withKI(0)
            .withKD(0);
        public static final MotionMagicConfigs MMC = new MotionMagicConfigs()
            .withMotionMagicCruiseVelocity(80)
            .withMotionMagicAcceleration(160)
            .withMotionMagicJerk(1600);


        public static final TalonFXConfiguration CoralPivotConfigs = new TalonFXConfiguration()
            .withSlot0(CoralPivotPID)
            .withMotionMagic(MMC);

        public static final double stowPos = 0;
        public static final double humanPlayer = 29;
    }
}