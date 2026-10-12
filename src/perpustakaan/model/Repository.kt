package perpustakaan.model

class Repository<T : Any> {

    private val items = mutableListOf<T>()

    fun add(item: T) {
        items.add(item)
    }

    fun getAll(): List<T> {
        return items.toList()
    }

    fun find(predicate: (T) -> Boolean): T? {
        return items.find(predicate)
    }

    fun findAll(predicate: (T) -> Boolean): List<T> {
        return items.filter(predicate)
    }
}
