package com.example.janaushadhifinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.janaushadhifinder.ui.theme.JanAushadhiFinderTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*

class StoreLocatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JanAushadhiFinderTheme {
                StoreLocatorScreen(
                    onCallClick = { phone ->
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:$phone".toUri()
                        }
                        startActivity(intent)
                    },
                    onDirectionsClick = { lat, lon ->
                        val gmmIntentUri = "google.navigation:q=$lat,$lon".toUri()
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)
                    }
                )
            }
        }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreLocatorScreen(
    onCallClick: (String) -> Unit,
    onDirectionsClick: (Double, Double) -> Unit
) {
    var stores by remember { mutableStateOf<List<Store>>(emptyList()) }
    var cityQuery by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("All States") }
    var isMapView by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val states = listOf("All States", "Karnataka", "Maharashtra", "Delhi", "Tamil Nadu")

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("stores").addSnapshotListener { value, _ ->
            if (value != null) {
                stores = value.toObjects(Store::class.java)
            }
            isLoading = false
        }
    }

    val filteredStores = remember(stores, cityQuery, selectedState) {
        stores.filter { store ->
            val matchesCity =
                cityQuery.isEmpty() || store.city.contains(cityQuery, ignoreCase = true)
            val matchesState = selectedState == "All States" || store.state == selectedState
            matchesCity && matchesState
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Store Locator", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF008952)),
                actions = {
                    IconButton(onClick = { isMapView = !isMapView }) {
                        Icon(
                            if (isMapView) Icons.AutoMirrored.Filled.List else Icons.Default.Map,
                            contentDescription = "Toggle View",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedState,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select State") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            states.forEach { state ->
                                DropdownMenuItem(
                                    text = { Text(state) },
                                    onClick = { selectedState = state; expanded = false })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = cityQuery,
                        onValueChange = { cityQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Search City") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            if (isLoading) LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF008952)
            )

            if (isMapView) {
                StoreMapView(filteredStores, onDirectionsClick)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredStores) { store ->
                        StoreCard(
                            store,
                            onCallClick,
                            onDirectionsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoreMapView(stores: List<Store>, onDirectionsClick: (Double, Double) -> Unit) {
    val india = LatLng(20.5937, 78.9629)
    val cameraPositionState =
        rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(india, 5f) }

    GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
        stores.forEach { store ->
            Marker(
                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                title = store.name,
                snippet = store.address,
                onInfoWindowClick = { onDirectionsClick(store.latitude, store.longitude) }
            )
        }
    }
}

@Composable
fun StoreCard(
    store: Store,
    onCallClick: (String) -> Unit,
    onDirectionsClick: (Double, Double) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = store.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "${store.city}, ${store.state}",
                        fontSize = 14.sp,
                        color = Color(0xFF008952),
                        fontWeight = FontWeight.Medium
                    )
                }
                StatusBadge(isOpen = store.isOpen)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = store.address, fontSize = 13.sp, color = Color(0xFF64748B))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = store.openingHours, fontSize = 13.sp, color = Color(0xFF64748B))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onCallClick(store.phone) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008952)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.ic_call),
                        null,
                        modifier = Modifier.size(18.dp)
                    ); Spacer(Modifier.width(8.dp)); Text("Call")
                }
                OutlinedButton(
                    onClick = { onDirectionsClick(store.latitude, store.longitude) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF008952))
                ) {
                    Text("Directions", color = Color(0xFF008952))
                }
            }
        }
    }
}

@Composable
fun StatusBadge(isOpen: Boolean) {
    val color = if (isOpen) Color(0xFF008952) else Color.Red
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
        Text(
            text = if (isOpen) "OPEN" else "CLOSED",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StoreLocatorPreview() {
    val sampleStores = listOf(
        Store(
            "PMBJK Hubballi Main",
            "Station Road",
            "Hubballi",
            "Karnataka",
            1.2,
            true,
            "09:00 - 21:00",
            "9123456780",
            15.0,
            75.0
        )
    )
    JanAushadhiFinderTheme {
        // UI only preview without Firebase
        StoreLocatorScreenContent(
            stores = sampleStores,
            isLoading = false,
            onCallClick = {},
            onDirectionsClick = { _, _ -> })
    }
}

@Composable
fun StoreLocatorScreenContent(
    stores: List<Store>,
    isLoading: Boolean,
    onCallClick: (String) -> Unit,
    onDirectionsClick: (Double, Double) -> Unit
) {
    // This is a helper to allow the preview to work without real state
    Scaffold { p ->
        Column(Modifier.padding(p)) {
            LazyColumn {
                items(stores) { StoreCard(it, onCallClick, onDirectionsClick) }
            }
        }
    }
}
