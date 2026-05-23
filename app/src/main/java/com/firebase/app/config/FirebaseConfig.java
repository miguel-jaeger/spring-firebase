package com.firebase.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore getFirestore() {
        try {
            // 1. Verificamos si Firebase ya fue inicializado (evita errores con herramientas como DevTools)
            if (FirebaseApp.getApps().isEmpty()) {
                
                // 2. Leemos el archivo JSON desde la carpeta resources
                InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();

                // 3. Configuramos las credenciales
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                // 4. Inicializamos la aplicación de Firebase de forma prioritaria
                FirebaseApp.initializeApp(options);
                
                System.out.println("=========================================");
                System.out.println("¡CONEXIÓN EN ORDEN: FIREBASE CONFIGURADO!");
                System.out.println("=========================================");
            }
            
            // 5. Ahora que sabemos que la App existe, devolvemos el cliente de Firestore a salvo
            return FirestoreClient.getFirestore();

        } catch (IOException e) {
            System.err.println("Error catastrófico al leer el archivo de credenciales de Firebase: " + e.getMessage());
            throw new RuntimeException("No se pudo iniciar Firestore debido a un error de credenciales.", e);
        }
    }
}