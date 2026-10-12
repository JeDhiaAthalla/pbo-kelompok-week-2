package perpustakaan

import perpustakaan.exception.ItemAlreadyBorrowedException
import perpustakaan.exception.ItemNotFoundException
import perpustakaan.exception.ItemNotBorrowedException
import perpustakaan.manager.LibraryManager
import perpustakaan.model.Book
import perpustakaan.model.DVD
import perpustakaan.model.Magazine

fun main() {

    println("=".repeat(50))
    println("SISTEM MANAJEMEN PERPUSTAKAAN")
    println("=".repeat(50))

    //1. Menambahkan koleksi perpustakaan
    LibraryManager.addItem(
        Book(
            id = "P001",
            title = "Pemrograman Kotlin",
            year = 2023,
            author = "Budi Santoso",
            pages = 350
        )
    )

    LibraryManager.addItem(
        Book(
            id = "P002",
            title = "Dasar-Dasar OOP",
            year = 2022,
            author = "Siti Rahayu",
            pages = 280
        )
    )

    LibraryManager.addItem(
        Magazine(
            id = "M001",
            title = "Tech Today",
            year = 2024,
            issueNumber = 42,
            publisher = "Tech Media"
        )
    )

    LibraryManager.addItem(
        DVD(
            id = "D001",
            title = "Film Dokumenter",
            year = 2021,
            duration = 120,
            genre = "Dokumenter"
        )
    )

    //2. Menampilkan semua koleksi
    println("\n--- Semua Koleksi ---")
    LibraryManager.displayAllItems()

    //3. Meminjam buku
    println("\n--- Meminjam Buku P001 oleh M001 ---")

    runCatching {
        LibraryManager.borrowItem(
            itemId = "P001",
            memberId = "M001",
            date = "2025-01-15"
        )
    }.onSuccess {
        println(
            "Buku 'Pemrograman Kotlin' berhasil dipinjam " +
                    "oleh M001 pada 2025-01-15."
        )
    }.onFailure { error ->
        println("Gagal meminjam: ${error.message}")
    }

    //4. Mencoba meminjam buku yang sama lagi
    println("\n--- Uji Error: Pinjam Buku yang Sudah Dipinjam ---")

    runCatching {
        LibraryManager.borrowItem(
            itemId = "P001",
            memberId = "M002",
            date = "2025-01-16"
        )
    }.onFailure { error ->
        when (error) {
            is ItemAlreadyBorrowedException ->
                println("Error: ${error.message}")

            is ItemNotFoundException ->
                println("Error: ${error.message}")

            else ->
                println("Error: ${error.message}")
        }
    }

    //5. Mencari item berdasarkan kata kunci
    println("\n--- Mencari 'Kotlin' ---")

    val searchResults = LibraryManager.findByTitle("Kotlin")

    if (searchResults.isEmpty()) {
        println("Item tidak ditemukan.")
    } else {
        searchResults.forEach { it.display() }
    }

    //6. Mengembalikan buku
    println("\n--- Mengembalikan Buku P001 ---")

    runCatching {
        LibraryManager.returnItem("P001")
    }.onSuccess {
        println("Buku 'Pemrograman Kotlin' berhasil dikembalikan.")
    }.onFailure { error ->
        println("Gagal mengembalikan: ${error.message}")
    }

    //7. Menguji pengembalian item yang tidak dipinjam
    println("\n--- Uji Error: Item Tidak Sedang Dipinjam ---")

    runCatching {
        LibraryManager.returnItem("P001")
    }.onFailure { error ->
        when (error) {
            is ItemNotBorrowedException ->
                println("Error: ${error.message}")

            else ->
                println("Error: ${error.message}")
        }
    }

    //8. Menguji pencarian item yang tidak ada
    println("\n--- Uji Error: Item Tidak Ditemukan ---")

    runCatching {
        LibraryManager.borrowItem(
            itemId = "X999",
            memberId = "M003",
            date = "2025-01-17"
        )
    }.onFailure { error ->
        println("Error: ${error.message}")
    }

    //9. Meminjam DVD
    println("\n--- Meminjam DVD D001 ---")

    runCatching {
        LibraryManager.borrowItem(
            itemId = "D001",
            memberId = "M002",
            date = "2025-01-18"
        )
    }.onSuccess {
        println("DVD berhasil dipinjam oleh M002.")
    }.onFailure { error ->
        println("Gagal meminjam DVD: ${error.message}")
    }

    //10. Menampilkan item yang tersedia
    println("\n--- Item Tersedia ---")
    LibraryManager.displayAvailableItems()

    //11. Menampilkan item yang sedang dipinjam
    println("\n--- Item Dipinjam ---")
    LibraryManager.displayBorrowedItems()

    //12. Menandai item sebagai hilang
    println("\n--- Menandai P002 sebagai Hilang ---")

    runCatching {
        LibraryManager.markAsLost("P002")
    }.onSuccess {
        println("Item P002 berhasil ditandai hilang.")
    }.onFailure { error ->
        println("Gagal: ${error.message}")
    }

    //13. Status akhir semua koleksi
    println("\n--- Status Akhir Semua Koleksi ---")
    LibraryManager.displayAllItems()

    println("\nProgram selesai.")
}
