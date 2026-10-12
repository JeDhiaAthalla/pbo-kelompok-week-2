package perpustakaan.manager

import perpustakaan.exception.ItemAlreadyBorrowedException
import perpustakaan.exception.ItemNotBorrowedException
import perpustakaan.exception.ItemNotFoundException
import perpustakaan.interfaces.Borrowable
import perpustakaan.interfaces.Searchable
import perpustakaan.model.ItemStatus
import perpustakaan.model.LibraryItem
import perpustakaan.model.Repository

object LibraryManager {

    private val itemRepo = Repository<LibraryItem>()

    fun addItem(item: LibraryItem) {
        require(itemRepo.find { it.id == item.id } == null) {
            "ID item ${item.id} sudah digunakan."
        }

        itemRepo.add(item)
    }

    fun findByTitle(query: String): List<LibraryItem> {
        return itemRepo.findAll { item ->
            item is Searchable && item.matches(query)
        }
    }

    fun borrowItem(
        itemId: String,
        memberId: String,
        date: String
    ): Boolean {
        return runCatching {
            val item = itemRepo.find { it.id == itemId }
                ?: throw ItemNotFoundException(
                    "Item dengan ID $itemId tidak ditemukan."
                )

            val borrowable = item as? Borrowable
                ?: throw IllegalArgumentException(
                    "Item '$itemId' tidak dapat dipinjam."
                )

            when (item.status) {
                is ItemStatus.Borrowed ->
                    throw ItemAlreadyBorrowedException(
                        "Item '$itemId' sudah dipinjam."
                    )

                ItemStatus.Lost ->
                    throw IllegalArgumentException(
                        "Item '$itemId' berstatus hilang."
                    )

                ItemStatus.Available -> Unit
            }

            check(borrowable.borrow(memberId, date)) {
                "Peminjaman item '$itemId' gagal."
            }

            true
        }.getOrElse { error ->
            throw error
        }
    }

    fun returnItem(itemId: String): Boolean {
        return runCatching {
            val item = itemRepo.find { it.id == itemId }
                ?: throw ItemNotFoundException(
                    "Item dengan ID $itemId tidak ditemukan."
                )

            val borrowable = item as? Borrowable
                ?: throw IllegalArgumentException(
                    "Item '$itemId' tidak dapat dipinjam."
                )

            if (item.status !is ItemStatus.Borrowed) {
                throw ItemNotBorrowedException(
                    "Item '$itemId' tidak sedang dipinjam."
                )
            }

            check(borrowable.returnItem()) {
                "Pengembalian item '$itemId' gagal."
            }

            true
        }.getOrElse { error ->
            throw error
        }
    }

    fun markAsLost(itemId: String) {
        val item = itemRepo.find { it.id == itemId }
            ?: throw ItemNotFoundException(
                "Item dengan ID $itemId tidak ditemukan."
            )

        item.status = ItemStatus.Lost
    }

    fun getAvailableItems(): List<LibraryItem> {
        return itemRepo.findAll {
            it.status is ItemStatus.Available
        }
    }

    fun getBorrowedItems(): List<LibraryItem> {
        return itemRepo.findAll {
            it.status is ItemStatus.Borrowed
        }
    }

    fun displayAllItems() {
        itemRepo.getAll().forEach { item ->
            item.display()
        }
    }

    fun displayAvailableItems() {
        getAvailableItems().forEach { it.display() }
    }

    fun displayBorrowedItems() {
        getBorrowedItems().forEach { it.display() }
    }
}
