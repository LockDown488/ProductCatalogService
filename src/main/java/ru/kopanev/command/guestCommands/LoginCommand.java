package ru.kopanev.command.guestCommands;

import lombok.RequiredArgsConstructor;
import ru.kopanev.command.Command;
import ru.kopanev.ui.UserUi;

@RequiredArgsConstructor
public class LoginCommand implements Command {
    private final UserUi userUi;

    @Override
    public void execute() {
        userUi.login();
    }
}
