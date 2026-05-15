package com.example.janaushadhifinder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janaushadhifinder.ui.theme.JanAushadhiFinderTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
//        uploadMedicines()
//        uploadStores()
        enableEdgeToEdge()
        setContent {
            // State to track the current user reactively
            var currentUser by remember { mutableStateOf(auth.currentUser) }

            // Listen for Auth changes (Login/Logout/Register)
            DisposableEffect(Unit) {
                val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                    currentUser = firebaseAuth.currentUser
                }
                auth.addAuthStateListener(listener)
                onDispose { auth.removeAuthStateListener(listener) }
            }

            JanAushadhiFinderTheme {
                HomeScreen(
                    userName = currentUser?.displayName ?: currentUser?.email?.split("@")?.get(0),
                    userEmail = currentUser?.email,
                    onSearch = { query ->
                        if (currentUser == null) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        } else {
                            val intent = Intent(this@MainActivity, SearchActivity::class.java).apply {
                                putExtra("QUERY", query)
                            }
                            startActivity(intent)
                        }
                    },
                    onFindStores = {
                        if (currentUser == null) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        } else {
                            startActivity(Intent(this@MainActivity, StoreLocatorActivity::class.java))
                        }
                    },
                    onSignOut = {
                        if (currentUser != null) {
                            auth.signOut()
                            Toast.makeText(this@MainActivity, "Signed out", Toast.LENGTH_SHORT).show()
                        } else {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }
                    }
                )
            }
        }
    }

    private fun uploadMedicines() {
        val db = Firebase.firestore
        val medicines = listOf(
            Medicine("Dolo 650", "Paracetamol", 35.0, 12.0, "Tablets", true),
            Medicine("Crocin", "Paracetamol", 40.0, 15.0, "Tablets", true),
            Medicine("Azithral", "Azithromycin", 120.0, 45.0, "Capsules", true),
            Medicine("Benadryl", "Diphenhydramine", 95.0, 38.0, "Syrup", true),
            Medicine("Augmentin", "Amoxicillin", 180.0, 60.0, "Capsules", true),
            Medicine("Calpol", "Paracetamol", 30.0, 10.0, "Syrup", true),
            Medicine("Cetcip", "Cetirizine", 55.0, 18.0, "Tablets", true),
            Medicine("Pantocid", "Pantoprazole", 150.0, 48.0, "Tablets", true),
            Medicine("Azee", "Azithromycin", 130.0, 50.0, "Capsules", true),
            Medicine("Monocef", "Ceftriaxone", 220.0, 85.0, "Injection", true),
            Medicine("Cofsils", "Amylmetacresol", 35.0, 12.0, "Tablets", true),
            Medicine("Zincovit", "Multivitamin", 110.0, 40.0, "Capsules", true),
            Medicine("Limcee", "Vitamin C", 25.0, 8.0, "Tablets", true),
            Medicine("Digene", "Antacid", 85.0, 28.0, "Syrup", true),
            Medicine("Ecosprin", "Aspirin", 45.0, 15.0, "Tablets", true),
            Medicine("Neurobion", "Vitamin B Complex", 95.0, 30.0, "Tablets", true),
            Medicine("Mox", "Amoxicillin", 160.0, 55.0, "Capsules", true),
            Medicine("Allegra", "Fexofenadine", 170.0, 60.0, "Tablets", true),
            Medicine("Omez", "Omeprazole", 90.0, 32.0, "Capsules", true),
            Medicine("Rantac", "Ranitidine", 60.0, 20.0, "Tablets", true)
        )

        medicines.forEach { medicine ->
            db.collection("medicines")
//                .add(medicine)
                .document(medicine.brandName)
                .set(medicine)
        }

        Toast.makeText(this, "Medicines Uploaded", Toast.LENGTH_SHORT).show()
    }

    private fun uploadStores() {
        val db = Firebase.firestore
        val stores = listOf(
            Store(
                name = "PMBJK Hubballi Main",
                address = "Station Road",
                city = "Hubballi",
                state = "Karnataka",
                distance = 1.2,
                isOpen = true,
                openingHours = "09:00 - 21:00",
                phone = "9123456780",
                latitude = 15.3647,
                longitude = 75.1240
            ),
            Store(
                name = "Jan Aushadhi Mangalore",
                address = "MG Road",
                city = "Mangalore",
                state = "Karnataka",
                distance = 2.5,
                isOpen = true,
                openingHours = "09:00 - 20:00",
                phone = "9876543210",
                latitude = 12.9141,
                longitude = 74.8560
            ),
            Store(
                name = "Jan Aushadhi Bangalore",
                address = "Rajajinagar",
                city = "Bangalore",
                state = "Karnataka",
                distance = 4.0,
                isOpen = true,
                openingHours = "08:00 - 22:00",
                phone = "9876543211",
                latitude = 12.9716,
                longitude = 77.5946
            ),
            Store(
                name = "Jan Aushadhi Mysore",
                address = "VV Mohalla",
                city = "Mysore",
                state = "Karnataka",
                distance = 1.8,
                isOpen = false,
                openingHours = "09:00 - 20:00",
                phone = "9876543212",
                latitude = 12.2958,
                longitude = 76.6394
            ),
            Store(
                name = "Jan Aushadhi Chennai",
                address = "T Nagar",
                city = "Chennai",
                state = "Tamil Nadu",
                distance = 3.2,
                isOpen = true,
                openingHours = "08:00 - 21:00",
                phone = "9876543213",
                latitude = 13.0827,
                longitude = 80.2707
            ),
            Store(
                name = "Jan Aushadhi Mumbai",
                address = "Andheri West",
                city = "Mumbai",
                state = "Maharashtra",
                distance = 5.5,
                isOpen = true,
                openingHours = "09:00 - 22:00",
                phone = "9876543214",
                latitude = 19.0760,
                longitude = 72.8777
            ),
            Store(
                name = "Jan Aushadhi Delhi",
                address = "Connaught Place",
                city = "Delhi",
                state = "Delhi",
                distance = 2.9,
                isOpen = true,
                openingHours = "09:00 - 21:00",
                phone = "9876543215",
                latitude = 28.6139,
                longitude = 77.2090
            ),
            Store(
                name = "Jan Aushadhi Kochi",
                address = "MG Road",
                city = "Kochi",
                state = "Kerala",
                distance = 2.1,
                isOpen = true,
                openingHours = "09:00 - 21:00",
                phone = "9876543216",
                latitude = 9.9312,
                longitude = 76.2673
            ),
            Store(
                name = "Jan Aushadhi Trivandrum",
                address = "Palayam",
                city = "Thiruvananthapuram",
                state = "Kerala",
                distance = 3.4,
                isOpen = true,
                openingHours = "08:00 - 20:00",
                phone = "9876543217",
                latitude = 8.5241,
                longitude = 76.9366
            ),
            Store(
                name = "Jan Aushadhi Kozhikode",
                address = "SM Street",
                city = "Kozhikode",
                state = "Kerala",
                distance = 1.7,
                isOpen = false,
                openingHours = "09:00 - 19:00",
                phone = "9876543218",
                latitude = 11.2588,
                longitude = 75.7804
            ),
            Store(
                name = "Jan Aushadhi Hyderabad",
                address = "Banjara Hills",
                city = "Hyderabad",
                state = "Telangana",
                distance = 4.6,
                isOpen = true,
                openingHours = "09:00 - 22:00",
                phone = "9876543219",
                latitude = 17.3850,
                longitude = 78.4867
            ),
            Store(
                name = "Jan Aushadhi Ahmedabad",
                address = "Navrangpura",
                city = "Ahmedabad",
                state = "Gujarat",
                distance = 3.3,
                isOpen = true,
                openingHours = "08:00 - 21:00",
                phone = "9876543220",
                latitude = 23.0225,
                longitude = 72.5714
            ),
            Store(
                name = "Jan Aushadhi Jaipur",
                address = "MI Road",
                city = "Jaipur",
                state = "Rajasthan",
                distance = 2.8,
                isOpen = true,
                openingHours = "09:00 - 21:00",
                phone = "9876543221",
                latitude = 26.9124,
                longitude = 75.7873
            )
        )

        stores.forEach { store ->
            db.collection("stores")
                .document(store.name)
                .set(store)
        }
        Toast.makeText(this, "Stores Uploaded", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun HomeScreen(
    userName: String?,
    userEmail: String?,
    onSearch: (String) -> Unit,
    onFindStores: () -> Unit,
    onSignOut: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("Tablets", "Syrup", "Capsules", "Injection", "Ointment")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color(0xFF007944))
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pill),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_title_home),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onSignOut() },
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text(
                    text = if (userEmail != null) "LOGOUT" else "LOGIN",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userName != null) {
                Text(
                    text = "Welcome, $userName",
                    color = Color(0xFF1E293B),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = stringResource(R.string.healthcare_for_all),
                color = Color(0xFF007944),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.find_affordable_medicines),
                color = Color(0xFF1E293B),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                modifier = Modifier.fillMaxWidth(),
//                placeholder = { Text("Search by medicine or brand") },
//                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    unfocusedBorderColor = Color(0xFFE2E8F0),
//                    focusedBorderColor = Color(0xFF007944)
//                )
//            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { onSearch(searchQuery) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008952))
                ) {
                    Text("Search Medicine")
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = onFindStores,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF007944))
                ) {
                    Text("Find Stores", color = Color(0xFF007944))
                }
            }
        }

        Text(
            text = "Medicine Categories",
            modifier = Modifier.padding(horizontal = 34.dp, vertical = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier.clickable { onSearch(category) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = Color(0xFF475569),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    JanAushadhiFinderTheme { HomeScreen("Gagan", "user@example.com", {}, {}, {}) }
}
