package perpustakaan.exception

class ItemNotFoundException(message: String) : Exception(message)

class ItemAlreadyBorrowedException(message: String) : Exception(message)

class ItemNotBorrowedException(message: String) : Exception(message)
