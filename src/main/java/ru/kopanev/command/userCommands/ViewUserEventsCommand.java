package ru.kopanev.command.userCommands;

import lombok.RequiredArgsConstructor;
import ru.kopanev.command.Command;
import ru.kopanev.ui.AuditUi;

@RequiredArgsConstructor
public class ViewUserEventsCommand implements Command {
    private final AuditUi auditUi;
    private final String username;

    @Override
    public void execute() {
        auditUi.printUserEvents(username);
    }
}
