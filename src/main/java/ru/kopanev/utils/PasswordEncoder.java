package ru.kopanev.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Утилита для хеширования и проверки паролей с использованием BCrypt.
 * BCrypt — это алгоритм хеширования с солью, специально разработанный
 * для безопасного хранения паролей.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public class PasswordEncoder {
    /**
     * Хеширует пароль с использованием BCrypt.
     *
     * @param password пароль в открытом виде
     * @return хешированный пароль
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Проверяет, соответствует ли пароль хешу.
     *
     * @param password пароль в открытом виде
     * @param hashedPassword хешированный пароль из БД
     * @return true, если пароль верный; false в противном случае
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
