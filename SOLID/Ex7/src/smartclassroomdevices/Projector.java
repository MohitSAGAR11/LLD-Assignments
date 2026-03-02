package smartclassroomdevices;


public class Projector implements Powerable, InputConnectable, SmartClassroomDevice {
    private boolean on;

    @Override
    public void powerOn() {
        on = true;
    }

    @Override
    public void powerOff() {
        on = false;
        System.out.println(getClassSimpleName() + " OFF");
    }

    @Override
    public void connectInput(String port) {
        if (on) System.out.println(getClassSimpleName() +  " ON (" + port + ")");
    }

    @Override
    public String getClassSimpleName() {
        return Projector.class.getSimpleName();
    }
}