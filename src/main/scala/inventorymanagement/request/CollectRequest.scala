package inventorymanagement.request

case class CollectRequest(locationId: String, orderId: String, items: Seq[Item])
