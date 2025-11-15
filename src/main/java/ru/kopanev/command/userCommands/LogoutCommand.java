package ru.kopanev.command.userCommands;

import lombok.RequiredArgsConstructor;
import ru.kopanev.command.Command;
import ru.kopanev.ui.UserUi;

@RequiredArgsConstructor
public class LogoutCommand implements Command {
    private final UserUi userUi;

    @Override
    public void execute() {
        userUi.logout();
    }
}
