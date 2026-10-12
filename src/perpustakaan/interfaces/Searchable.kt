package perpustakaan.interfaces

interface Searchable {
    fun matches(query: String): Boolean
}
