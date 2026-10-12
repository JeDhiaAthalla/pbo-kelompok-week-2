package perpustakaan.model

sealed class ItemStatus {

    data object Available : ItemStatus()

    data class Borrowed(
        val by: String,
        val date: String
    ) : ItemStatus()

    data object Lost : ItemStatus()

    fun display(): String = when (this) {
        Available -> "Tersedia"
        is Borrowed -> "Dipinjam oleh $by pada $date"
        Lost -> "Hilang"
    }
}
