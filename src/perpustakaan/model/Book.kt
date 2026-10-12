
package perpustakaan.model

import perpustakaan.interfaces.Borrowable
import perpustakaan.interfaces.Searchable

class Book(
    override val id: String,
    override val title: String,
    override val year: Int,
    val author: String,
    val pages: Int
) : LibraryItem(id, title, year), Borrowable, Searchable {

    override var status: ItemStatus = ItemStatus.Available

    override fun getDetails(): String {
        return "Buku oleh $author, $pages halaman"
    }

    override fun borrow(memberId: String, date: String): Boolean {
        if (!isAvailable()) return false

        status = ItemStatus.Borrowed(memberId, date)
        return true
    }

    override fun returnItem(): Boolean {
        if (status !is ItemStatus.Borrowed) return false

        status = ItemStatus.Available
        return true
    }

    override fun isAvailable(): Boolean {
        return status is ItemStatus.Available
    }

    override fun getBorrower(): String? {
        return when (val currentStatus = status) {
            is ItemStatus.Borrowed -> currentStatus.by
            else -> null
        }
    }

    override fun matches(query: String): Boolean {
        return title.contains(query, ignoreCase = true) ||
                author.contains(query, ignoreCase = true)
    }
}
