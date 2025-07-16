package com.example.firebaseauthapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.firebaseauthapp.ui.theme.FirebaseAuthAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAuthAppTheme {
                FirebaseAuthApp()
            }
        }
    }

    @Composable
    fun FirebaseAuthApp() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val user = auth.currentUser

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (user == null) {
                LoginScreen(
                    email = email,
                    password = password,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onLoginClick = {
                        auth.signInWithEmailAndPassword(email.trim(), password.trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MainActivity, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    onRegisterClick = {
                        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@MainActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MainActivity, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                )
            } else {
                HomeScreen(
                    userEmail = user.email ?: "User",
                    onLogout = {
                        auth.signOut()
                        Toast.makeText(this@MainActivity, "Logged out", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    @Composable
    fun LoginScreen(
        email: String,
        password: String,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onLoginClick: () -> Unit,
        onRegisterClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login or Register", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
                Text("Register")
            }
        }
    }

    @Composable
    fun HomeScreen(userEmail: String, onLogout: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome, $userEmail", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onLogout) {
                Text("Logout")
            }
        }
    }
}
