package ru.kopanev.command.guestCommands;

import ru.kopanev.command.Command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Выход. До свидания!");
        System.exit(0);
    }
}
