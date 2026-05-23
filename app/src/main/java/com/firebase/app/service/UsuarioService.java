package com.firebase.app.service;

import com.firebase.app.model.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UsuarioService {

    @Autowired
    private Firestore firestore;

    // 1. OBTENER POR ID (Ya lo tenías)
    public Usuario obtenerUsuarioPorId(String idDocumento) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("usuario").document(idDocumento);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Usuario usuario = document.toObject(Usuario.class);
            if (usuario != null && usuario.getIdPersona() == null) {
                usuario.setIdPersona(document.getId());
            }
            return usuario;
        }
        return null; 
    }

    // 2. LISTAR TODOS
    public List<Usuario> listarTodos() throws ExecutionException, InterruptedException {
        List<Usuario> listaUsuarios = new ArrayList<>();
        // Trae todos los documentos de la colección 'usuario'
        ApiFuture<QuerySnapshot> future = firestore.collection("usuario").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            Usuario usuario = document.toObject(Usuario.class);
            if (usuario.getIdPersona() == null) {
                usuario.setIdPersona(document.getId());
            }
            listaUsuarios.add(usuario);
        }
        return listaUsuarios;
    }

    // 3. INSERTAR NUEVO
    public String insertar(Usuario usuario) throws ExecutionException, InterruptedException {
        // Creamos un documento con un ID automático generado por Firebase
        DocumentReference docRef = firestore.collection("usuario").document();
        
        // Asignamos ese ID generado al campo idPersona del objeto antes de guardarlo
        usuario.setIdPersona(docRef.getId());
        
        // Guardamos en Firestore
        ApiFuture<WriteResult> result = docRef.set(usuario);
        
        // Retorna el ID asignado
        return docRef.getId();
    }

    // 4. ACTUALIZAR
    public String actualizar(String idDocumento, Usuario usuario) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("usuario").document(idDocumento);
        
        // Aseguramos que conserve su ID original en el cuerpo del documento
        usuario.setIdPersona(idDocumento);
        
        // .set() con SetOptions.merge() actualiza los campos modificados sin borrar los que no envíes
        ApiFuture<WriteResult> result = docRef.set(usuario, SetOptions.merge());
        
        return result.get().getUpdateTime().toString();
    }

    // 5. ELIMINAR
    public void eliminar(String idDocumento) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("usuario").document(idDocumento);
        ApiFuture<WriteResult> result = docRef.delete();
        result.get(); // Bloquea hasta que la eliminación se confirme en Firebase
    }
}