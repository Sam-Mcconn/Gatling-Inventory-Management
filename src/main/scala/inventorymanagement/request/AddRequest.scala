package inventorymanagement.request

case class AddRequest(locationId: String, items: Seq[Item])
