package ru.kopanev.command.userCommands;

import lombok.RequiredArgsConstructor;
import ru.kopanev.command.Command;
import ru.kopanev.ui.AuditUi;

@RequiredArgsConstructor
public class ViewAuditCommand implements Command {
    private final AuditUi auditUi;

    @Override
    public void execute() {
        auditUi.viewAudit();
    }
}
