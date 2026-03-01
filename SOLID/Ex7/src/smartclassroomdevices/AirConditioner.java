package smartclassroomdevices;

public class AirConditioner implements Powerable , TemperatureControllable , SmartClassroomDevice {
    @Override public void powerOn() { /* ok */ }
    @Override public void powerOff() { System.out.println("AC OFF"); }

    @Override
    public String getClassSimpleName() {
        return AirConditioner.class.getSimpleName();
    }

    @Override
    public void setTemperature(int temperature) {
        System.out.println("Temperature changed to " + temperature);
    }
}
