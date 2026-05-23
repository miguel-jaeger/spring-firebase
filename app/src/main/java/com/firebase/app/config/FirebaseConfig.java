package com.firebase.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore getFirestore() {
        try {
            // 1. Intentar leer la variable de entorno de Render
            String firebaseJson = System.getenv("FIREBASE_CREDENTIALS");
            GoogleCredentials credentials;

            if (firebaseJson != null && !firebaseJson.trim().isEmpty()) {
                // Si existe la variable (Producción en Render), la procesamos como un Stream de texto
                credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8))
                );
            } else {
                // Fallback para cuando estés desarrollando localmente con el archivo físico
                var resource = new org.springframework.core.io.ClassPathResource("firebase-service-account.json");
                credentials = GoogleCredentials.fromStream(resource.getInputStream());
            }

            // 2. Inicializar FirebaseApp si no ha sido inicializada antes
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();
                FirebaseApp.initializeApp(options);
            }

            return FirestoreClient.getFirestore();

        } catch (Exception e) {
            throw new RuntimeException("No se pudo iniciar Firestore debido a un error de credenciales: " + e.getMessage(), e);
        }
    }
}