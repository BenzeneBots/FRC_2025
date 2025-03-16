package frc.robot;

public class RobotConstants {
    public static class IntakePivotConstants {
        // ALVIN ACTUALLY FIND THESE VALUES
        public static final double stowPos = 0;
        public static final double humanPlayer = -30;
        public static final double level1 = -47;
        public static final double level2 = -41;

        public static final double speed = 0.3;
        public static final double slowSpeed = 0.1;
    }

    public static class IntakeSpinnerConstants {
        public static final double intakeSpeed = 0.25;
        public static final double outtakeSpeed = -1.0;
    }

    public static class AlgaePivotConstants {
        public static final double stowPos = .25;
        public static final double deployed = 0.83;
        public static final double score = 2.44;
    }

    public static class AlgaeSpinnerConstants {
        public static final double speed = 0.5;
    }

    public static class DriveConstants {
        public static final double translationDeadband = 0.15;
        public static final double rotationDeadband = 0.2;
    }

    public static class SecondPivotConstants {
        public static final double feedPos = 5.43115234375;
        public static final double level2Pos = -4.62;
    }

    public static class FirstPivotConstants {
        public static final double feedPos = 0.16943359375;
        public static final double level2Pos = 0.48486328125;
    }

    public static class ElevatorConstants {
        public static final double feedPos = 28.84;
        public static final double level2Pos = 140.7490234375;
        public static final double level3 = 295.22265625;
    }
}
