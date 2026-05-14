package com.example.janaushadhifinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janaushadhifinder.ui.theme.JanAushadhiFinderTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialQuery = intent.getStringExtra("QUERY") ?: ""
        setContent {
            var medicines by remember { mutableStateOf<List<Medicine>>(emptyList()) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                Firebase.firestore.collection("medicines").addSnapshotListener { value, _ ->
                    if (value != null) {
                        medicines = value.toObjects(Medicine::class.java)
                    }
                    isLoading = false
                }
            }

            JanAushadhiFinderTheme {
                SearchScreen(
                    medicines = medicines,
                    initialQuery = initialQuery,
                    isLoading = isLoading
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    medicines: List<Medicine>,
    initialQuery: String,
    isLoading: Boolean
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }
    var selectedCategory by remember { mutableStateOf("All") }
    var showOnlyAvailable by remember { mutableStateOf(false) }
    var maxPrice by remember { mutableFloatStateOf(2000f) }
    var showFilters by remember { mutableStateOf(false) }
    
    val categories = listOf("All", "Tablets", "Syrup", "Capsules", "Injection")

    val filteredList = remember(medicines, searchQuery, selectedCategory, showOnlyAvailable, maxPrice) {
        medicines.filter { medicine ->
            val matchesQuery = if (searchQuery.isEmpty()) true else {
                medicine.brandName.contains(searchQuery, ignoreCase = true) ||
                medicine.genericName.contains(searchQuery, ignoreCase = true) ||
                getLevenshteinDistance(searchQuery, medicine.brandName) <= 2 ||
                getLevenshteinDistance(searchQuery, medicine.genericName) <= 2
            }
            val matchesCategory = if (selectedCategory == "All") true else medicine.category == selectedCategory
            val matchesAvailability = !showOnlyAvailable || medicine.isAvailable
            val matchesPrice = medicine.brandedPrice <= maxPrice
            matchesQuery && matchesCategory && matchesAvailability && matchesPrice
        }.sortedBy { medicine ->
            if (searchQuery.isEmpty()) 0 else minOf(
                getLevenshteinDistance(searchQuery, medicine.brandName),
                getLevenshteinDistance(searchQuery, medicine.genericName)
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Medicine Search", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF008952)),
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Filters", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF8FAFC))) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search medicine, generic or brand...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = Color(0xFF008952)
                )
            )

            AnimatedVisibility(visible = showFilters) {
                Card(modifier = Modifier.padding(horizontal = 16.dp), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Category", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { category ->
                                FilterChip(selected = selectedCategory == category, onClick = { selectedCategory = category }, label = { Text(category) })
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = showOnlyAvailable, onCheckedChange = { showOnlyAvailable = it })
                            Text("Only Available", fontSize = 14.sp)
                        }
                        Text("Max Branded Price: ₹${maxPrice.toInt()}", fontSize = 14.sp)
                        Slider(value = maxPrice, onValueChange = { maxPrice = it }, valueRange = 0f..2000f)
                    }
                }
            }

            if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF008952))

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredList) { medicine -> MedicineCard(medicine) }
            }
        }
    }
}

@Composable
fun MedicineCard(medicine: Medicine) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = if (medicine.isAvailable) Color(0xFFE6F3EE) else Color(0xFFFFEBEE)) {
                    Icon(painter = painterResource(R.drawable.ic_pill), contentDescription = null, modifier = Modifier.padding(8.dp), tint = if (medicine.isAvailable) Color(0xFF008952) else Color.Red)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(medicine.brandName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(medicine.genericName, color = Color.Gray, fontSize = 13.sp)
                }
                Text(text = if (medicine.isAvailable) "IN STOCK" else "OUT OF STOCK", color = if (medicine.isAvailable) Color(0xFF008952) else Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8F0))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Branded", fontSize = 11.sp, color = Color.Gray)
                    Text("₹${String.format(Locale.US, "%.2f", medicine.brandedPrice)}", textDecoration = TextDecoration.LineThrough)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Jan Aushadhi", fontSize = 11.sp, color = Color(0xFF008952))
                    Text("₹${String.format(Locale.US, "%.2f", medicine.genericPrice)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

private fun getLevenshteinDistance(s1: String, s2: String): Int {
    val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
    for (i in 0..s1.length) dp[i][0] = i
    for (j in 0..s2.length) dp[0][j] = j
    for (i in 1..s1.length) {
        for (j in 1..s2.length) {
            val cost = if (s1[i - 1].lowercaseChar() == s2[j - 1].lowercaseChar()) 0 else 1
            dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
        }
    }
    return dp[s1.length][s2.length]
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val sampleMedicines = listOf(
        Medicine("Augmentin 625 Duo", "Amoxicillin", 201.27, 48.50, "Tablets", true),
        Medicine("Calpol 500mg", "Paracetamol", 15.42, 2.45, "Tablets", false)
    )
    JanAushadhiFinderTheme {
        SearchScreen(medicines = sampleMedicines, initialQuery = "Aug", isLoading = false)
    }
}

@Preview(showBackground = true)
@Composable
fun MedicineCardPreview() {
    JanAushadhiFinderTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MedicineCard(Medicine("Augmentin 625 Duo", "Amoxicillin", 201.27, 48.50, "Tablets", true))
        }
    }
}
