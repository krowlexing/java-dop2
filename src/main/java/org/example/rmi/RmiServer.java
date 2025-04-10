package org.example.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiServer {

    public static void main(String[] args) {
        try {
            // Создаем и запускаем RMI реестр на порту 1099 (по умолчанию)
            LocateRegistry.createRegistry(1099);

            // Создаем удаленный объект
            IRepository remoteRepository = new RepositoryRemote("your_username", "your_password");

            // Регистрируем удаленный объект в реестре RMI
            Naming.rebind("rmi://localhost/RepositoryService", remoteRepository);

            System.out.println("RMI server is ready. RepositoryService is bound.");

            synchronized (RmiServer.class) {
                RmiServer.class.wait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
