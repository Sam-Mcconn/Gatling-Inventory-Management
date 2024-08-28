package inventorymanagement.request

case class Item(itemId: String, expiry: Expiry, quantity: Int)
