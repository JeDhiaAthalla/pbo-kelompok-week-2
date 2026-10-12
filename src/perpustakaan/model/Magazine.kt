package perpustakaan.model

import perpustakaan.interfaces.Searchable

class Magazine(
    override val id: String,
    override val title: String,
    override val year: Int,
    val issueNumber: Int,
    val publisher: String
) : LibraryItem(id, title, year), Searchable {

    override var status: ItemStatus = ItemStatus.Available

    override fun getDetails(): String {
        return "Majalah edisi #$issueNumber, " +
                "diterbitkan oleh $publisher"
    }

    override fun matches(query: String): Boolean {
        return title.contains(query, ignoreCase = true) ||
                publisher.contains(query, ignoreCase = true)
    }
}
