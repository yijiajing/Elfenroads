package commands;

// sent by the host after he receives a boot validation command
public class BootValidationResponseCommand implements GameCommand
{
    private boolean validated;

    public BootValidationResponseCommand(boolean pValidated)
    {
        validated = pValidated;
    }

    @Override
    public void execute() {
        // TODO: implement
        // will be executed on the receiving machine
    }

    public boolean isValidated() {
        return validated;
    }
}
