package com.example.gladson.socorramev2.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Classe de configuração do Firebase para o App.
 */
public class FirebaseConfig {

    private static DatabaseReference database;
    private static FirebaseAuth auth;

    /**
     * Retorna a instância do FirebaseDatabase.
     *
     * @return
     */
    public static DatabaseReference getFirebaseDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }

        return database;
    }

    /**
     * Retorna a instância do FirebaseAuth.
     *
     * @return
     */
    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }
}
