package ru.kopanev.command.userCommands;

import lombok.RequiredArgsConstructor;
import ru.kopanev.command.Command;
import ru.kopanev.ui.ProductUi;

@RequiredArgsConstructor
public class FilterByBrandCommand implements Command {
    private final ProductUi productUi;

    @Override
    public void execute() {
        productUi.filterByBrand();
    }
}
