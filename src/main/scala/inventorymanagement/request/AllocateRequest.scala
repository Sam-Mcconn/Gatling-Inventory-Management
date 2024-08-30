package inventorymanagement.request

case class AllocateRequest(locationId: String, orderId: String, items: Seq[Item])
