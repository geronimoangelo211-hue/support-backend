package OsBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsBackendApplication.class, args);
        System.out.println("Backend is running! Ready for Ngrok.");
    }
}