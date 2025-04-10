package org.example.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface IRepository extends Remote {

    // Метод для создания таблиц, если их нет
    void createTables() throws RemoteException;

    // Метод для вывода всех пользователей с их статистикой
    void displayAllUsersWithGameStats() throws RemoteException;

    // Метод для добавления записи в таблицу game_stats
    void addGameStat(int userId, int totalTime, int moveCount, Date gameDate) throws RemoteException;

    // Метод для добавления записи пользователя и связанной статистики
    void addUserWithGameStat(String username, int totalTime, int moveCount, Date gameDate) throws RemoteException;

    // Метод для удаления записи из game_stats
    void deleteGameStat(int gameId) throws RemoteException;

    // Метод для удаления пользователя
    void deleteUser(int userId) throws RemoteException;
}
