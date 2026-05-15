package com.example.janaushadhifinder

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object MedicineData {

    fun uploadLargeMedicineDataset() {

        val db = Firebase.firestore

        val genericMedicines = listOf(
            "Paracetamol",
            "Azithromycin",
            "Amoxicillin",
            "Cetirizine",
            "Pantoprazole",
            "Omeprazole",
            "Metformin",
            "Aspirin",
            "Ibuprofen",
            "Vitamin C",
            "Vitamin B Complex",
            "Diphenhydramine",
            "Ranitidine",
            "Antacid",
            "Fexofenadine",
            "Ceftriaxone",
            "Diclofenac",
            "Doxycycline",
            "Levocetirizine",
            "Ofloxacin"
        )

        val brandPrefixes = listOf(
            "Dolo",
            "Crocin",
            "Azee",
            "Mox",
            "Calpol",
            "Allegra",
            "Pantocid",
            "Augmentin",
            "Benadryl",
            "Monocef",
            "Rantac",
            "Digene",
            "Neurobion",
            "Cetcip",
            "Limcee",
            "Zincovit"
        )

        val categories = listOf(
            "Tablets",
            "Capsules",
            "Syrup",
            "Injection",
            "Ointment"
        )

        val medicines = mutableListOf<Medicine>()

        for (i in 1..550) {

            val generic = genericMedicines.random()
            val prefix = brandPrefixes.random()
            val category = categories.random()

            val brandedPrice = (50..500).random().toDouble()
            val genericPrice = (10..150).random().toDouble()

            medicines.add(
                Medicine(
                    brandName = "$prefix $i",
                    genericName = generic,
                    brandedPrice = brandedPrice,
                    genericPrice = genericPrice,
                    category = category,
                    isAvailable = listOf(true, false).random()
                )
            )
        }

        medicines.forEach { medicine ->

            db.collection("medicines")
                .document(medicine.brandName)
                .set(medicine)
        }
    }
}