package org.example;

import org.example.rmi.IRepository;

import java.rmi.Naming;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello");
        try {
            // Получаем ссылку на удаленный объект
//            var repository = new Repository("use", "pass");
//            repo.
            IRepository repository = (IRepository) Naming.lookup("rmi://localhost/RepositoryService");
//
//            // Вызываем методы удаленного объекта
            repository.createTables();
            repository.displayAllUsersWithGameStats();
//
//            // Пример добавления записи
            repository.addUserWithGameStat("Bob", 150, 20, java.sql.Date.valueOf("2025-03-15"));
            repository.displayAllUsersWithGameStats();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
