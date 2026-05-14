package com.example.janaushadhifinder

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janaushadhifinder.ui.theme.JanAushadhiFinderTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContent {
            JanAushadhiFinderTheme {
                LoginScreen(
                    onLogin = { email, password, onLoading ->
                        auth.signInWithEmailAndPassword(email.trim(), password.trim())
                            .addOnCompleteListener { task ->
                                onLoading(false)
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    onRegister = { email, password, name, onLoading ->
                        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(name.trim())
                                        .build()
                                    auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
                                        onLoading(false)
                                        Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } ?: onLoading(false)
                                } else {
                                    onLoading(false)
                                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    onGoogleSignInResult = { result ->
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result)
                        try {
                            val account = task.getResult(ApiException::class.java)
                            val idToken = account?.idToken
                            if (idToken != null) {
                                val credential = GoogleAuthProvider.getCredential(idToken, null)
                                auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                                    if (authResult.isSuccessful) {
                                        Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Firebase Auth Failed: ${authResult.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Google ID Token is null. Check Firebase Configuration.", Toast.LENGTH_LONG).show()
                                Log.e("LoginActivity", "ID Token is null. Ensure default_web_client_id is correct in strings.xml")
                            }
                        } catch (e: ApiException) {
                            Log.e("LoginActivity", "Google sign in failed code: ${e.statusCode}")
                            Toast.makeText(this, "Google Sign-In Error: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    triggerGooglePicker = { launcher ->
                        val clientId = getString(R.string.default_web_client_id)
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestIdToken(clientId)
                            .build()
                        val client = GoogleSignIn.getClient(this, gso)
                        launcher.launch(client.signInIntent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String, (Boolean) -> Unit) -> Unit,
    onRegister: (String, String, String, (Boolean) -> Unit) -> Unit,
    onGoogleSignInResult: (android.content.Intent?) -> Unit,
    triggerGooglePicker: (androidx.activity.result.ActivityResultLauncher<android.content.Intent>) -> Unit
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        onGoogleSignInResult(result.data)
    }

    fun validateInputs(): Boolean {
        var isValid = true
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() || !trimmedEmail.contains(".")) {
            emailError = "Please enter a valid email address"
            isValid = false
        } else { emailError = null }

        if (trimmedPassword.length < 8 || !trimmedPassword.any { it.isUpperCase() } || !trimmedPassword.any { it.isDigit() } || !trimmedPassword.any { !it.isLetterOrDigit() }) {
            passwordError = "8+ chars, Uppercase, Digit, and Symbol required"
            isValid = false
        } else { passwordError = null }

        if (!isLogin) {
            if (name.trim().isBlank()) {
                nameError = "Name is required"
                isValid = false
            } else { nameError = null }

            if (trimmedPassword != confirmPassword.trim()) {
                confirmPasswordError = "Passwords do not match"
                isValid = false
            } else { confirmPasswordError = null }
        }

        return isValid
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC)).statusBarsPadding().navigationBarsPadding()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Surface(modifier = Modifier.size(70.dp), shape = CircleShape, color = Color(0xFF008952)) {
                Icon(painter = painterResource(id = R.drawable.ic_pill), contentDescription = null, modifier = Modifier.padding(18.dp), tint = Color.White)
            }
            Text(text = if (isLogin) "Welcome Back" else "Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 20.dp))
            
            Card(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!isLogin) {
                        OutlinedTextField(
                            value = name, onValueChange = { name = it; nameError = null },
                            modifier = Modifier.fillMaxWidth(), label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            isError = nameError != null, supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                            shape = RoundedCornerShape(12.dp), enabled = !isLoading
                        )
                    }

                    OutlinedTextField(
                        value = email, onValueChange = { email = it; emailError = null },
                        modifier = Modifier.fillMaxWidth(), label = { Text("Email address") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        isError = emailError != null, supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), enabled = !isLoading
                    )

                    OutlinedTextField(
                        value = password, onValueChange = { password = it; passwordError = null },
                        modifier = Modifier.fillMaxWidth(), label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError != null, supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), enabled = !isLoading
                    )

                    if (!isLogin) {
                        OutlinedTextField(
                            value = confirmPassword, onValueChange = { confirmPassword = it; confirmPasswordError = null },
                            modifier = Modifier.fillMaxWidth(), label = { Text("Confirm Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = confirmPasswordError != null, supportingText = { confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                            shape = RoundedCornerShape(12.dp), enabled = !isLoading
                        )
                    }

                    Button(
                        onClick = {
                            if (validateInputs()) {
                                isLoading = true
                                if (isLogin) onLogin(email, password) { isLoading = it }
                                else onRegister(email, password, name) { isLoading = it }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008952)),
                        enabled = !isLoading
                    ) {
                        if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        else Text(if (isLogin) "Sign In" else "Create Account", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            TextButton(onClick = { isLogin = !isLogin; emailError = null; passwordError = null; confirmPasswordError = null; nameError = null }, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = if (isLogin) "Don't have an account? Register Now" else "Already have an account? Login", color = Color(0xFF008952), fontWeight = FontWeight.Bold)
            }

            Text(text = "OR CONTINUE WITH", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 32.dp))

            SocialButton(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                text = "Sign in with Google",
                iconRes = R.drawable.ic_google,
                onClick = { triggerGooglePicker(googleSignInLauncher) }
            )
        }
    }
}

@Composable
fun SocialButton(modifier: Modifier, text: String, iconRes: Int, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(56.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
        Icon(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Unspecified)
        Spacer(Modifier.width(12.dp))
        Text(text, color = Color.DarkGray)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    JanAushadhiFinderTheme { 
        LoginScreen(onLogin = { _, _, _ -> }, onRegister = { _, _, _, _ -> }, onGoogleSignInResult = { _ -> }, triggerGooglePicker = { _ -> })
    }
}