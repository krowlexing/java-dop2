package org.example.rmi;

import org.example.Repository;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;

public class RepositoryRemote extends UnicastRemoteObject implements IRepository {

    private Repository repository;

    // Конструктор, который инициализирует Repository
    public RepositoryRemote(String username, String password) throws RemoteException {
        super();
        try {
            this.repository = new Repository(username, password);
        } catch (Exception e) {
            throw new RemoteException("Error creating Repository", e);
        }
    }

    @Override
    public void createTables() throws RemoteException {
        try {
            repository.createTables();
        } catch (Exception e) {
            throw new RemoteException("Error creating tables", e);
        }
    }

    @Override
    public void displayAllUsersWithGameStats() throws RemoteException {
        try {
            repository.displayAllUsersWithGameStats();
        } catch (Exception e) {
            throw new RemoteException("Error displaying users with game stats", e);
        }
    }

    @Override
    public void addGameStat(int userId, int totalTime, int moveCount, Date gameDate) throws RemoteException {
        try {
            repository.addGameStat(userId, totalTime, moveCount, gameDate);
        } catch (Exception e) {
            throw new RemoteException("Error adding game stat", e);
        }
    }

    @Override
    public void addUserWithGameStat(String username, int totalTime, int moveCount, Date gameDate) throws RemoteException {
        try {
            repository.addUserWithGameStat(username, totalTime, moveCount, gameDate);
        } catch (Exception e) {
            throw new RemoteException("Error adding user with game stat", e);
        }
    }

    @Override
    public void deleteGameStat(int gameId) throws RemoteException {
        try {
            repository.deleteGameStat(gameId);
        } catch (Exception e) {
            throw new RemoteException("Error deleting game stat", e);
        }
    }

    @Override
    public void deleteUser(int userId) throws RemoteException {
        try {
            repository.deleteUser(userId);
        } catch (Exception e) {
            throw new RemoteException("Error deleting user", e);
        }
    }
}
