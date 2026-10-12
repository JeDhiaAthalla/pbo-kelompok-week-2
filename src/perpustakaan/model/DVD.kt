package perpustakaan.model

import perpustakaan.interfaces.Borrowable
import perpustakaan.interfaces.Searchable

class DVD(
    override val id: String,
    override val title: String,
    override val year: Int,
    val duration: Int,
    val genre: String
) : LibraryItem(id, title, year), Borrowable, Searchable {

    override var status: ItemStatus = ItemStatus.Available

    override fun getDetails(): String {
        return "DVD, $duration menit, genre: $genre"
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
                genre.contains(query, ignoreCase = true)
    }
}
