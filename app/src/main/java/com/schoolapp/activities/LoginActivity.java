package com.schoolapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schoolapp.R;
import com.schoolapp.models.User;
import com.schoolapp.utils.PrefsManager;
import com.schoolapp.database.DatabaseHelper;
import androidx.appcompat.app.AlertDialog;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private RadioGroup rgRole;
    private ProgressBar progressBar;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilEmail        = findViewById(R.id.til_email);
        tilPassword     = findViewById(R.id.til_password);
        etEmail         = findViewById(R.id.et_email);
        etPassword      = findViewById(R.id.et_password);
        btnLogin        = findViewById(R.id.btn_login);
        rgRole          = findViewById(R.id.rg_role);
        progressBar     = findViewById(R.id.progress_bar);
        tvForgotPassword= findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvForgotPassword.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Mot de passe oublié")
                        .setMessage("Veuillez contacter le secrétariat de votre établissement pour réinitialiser votre mot de passe.")
                        .setPositiveButton("OK", null)
                        .show());
    }

    private void attemptLogin() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email    = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        boolean valid = true;

        // --- Validation de l'email ---
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email obligatoire");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email invalide");
            valid = false;
        }

        // --- Validation du mot de passe ---
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Mot de passe obligatoire");
            valid = false;
        } else if (password.length() < 6 || password.length() > 20) {
            tilPassword.setError("Entre 6 et 20 caractères");
            valid = false;
        } else if (password.contains(" ")) {
            tilPassword.setError("Pas d'espaces autorisés");
            valid = false;
        } else if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            tilPassword.setError("Doit contenir au moins une lettre et un chiffre");
            valid = false;
        }

        if (!valid) return;

        showLoading(true);

        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            User user = db.checkLogin(email, password);

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                if (user == null) {
                    showLoading(false);
                    tilPassword.setError("Email ou mot de passe incorrect");
                    Toast.makeText(this, "Connexion échouée", Toast.LENGTH_SHORT).show();
                    return;
                }

                PrefsManager prefs = PrefsManager.getInstance(this);
                prefs.saveUserSession(user.getId(), user.getName(), user.getEmail(),
                        user.getRole(), "mock_token_12345");
                prefs.setSelectedChild(user.getLinkedStudentId());
                // Après saveUserSession(...)
                PrefsManager.getInstance(this).saveSchoolLocation(
                        33.9989,        // lat Rabat
                        -6.8520,        // lng Rabat
                        "Lycée Averroès",
                        "Rabat, Maroc"
                );

                showLoading(false);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }).start();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        btnLogin.setText(show ? "" : getString(R.string.login_button));
    }
}