package perpustakaan.model

abstract class LibraryItem(
    open val id: String,
    open val title: String,
    open val year: Int
) {
    abstract var status: ItemStatus

    abstract fun getDetails(): String

    open fun display() {
        println("[$id] $title ($year) - ${status.display()}")
        println("   Detail: ${getDetails()}")
    }
}
