extends Node

var DBitems: Array[itemDB] = []
var getAllItems: HTTPRequest
var data_hash: int = 0
var already_loaded: bool = false

func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

func _on_session_ready() -> void:
	print("🟢 PlayerInfo listo. Obteniendo ítems...")

	getAllItems = HTTPRequest.new()
	add_child(getAllItems)

	getAllItems.request_completed.connect(_on_get_all_items_request_completed)

	var headers = [
		"Accept: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey
	]

	var result = getAllItems.request(
		PlayerInfo.urlSpring + "/api/items",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error al iniciar la petición HTTP:", result)

func _on_get_all_items_request_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Los ítems ya estaban cargados y no han cambiado.")
			return

		DBitems.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for item_data in parsed_json:
				var new_item = parse_item(item_data)
				if new_item:
					DBitems.append(new_item)
			print("✅ Ítems cargados:", DBitems.size())
		else:
			print("⚠️ La respuesta está vacía o mal formada")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ Error HTTP", response_code)

func parse_item(item: Dictionary) -> itemDB:
	if not item.has("name") or not item.has("description"):
		print("⚠️ Datos de ítem incompletos:", item)
		return null

	var i = itemDB.new()
	i.itemName = item["name"]
	i.itemDescription = item["description"]
	i.quantity = item.get("quantity", 0) # Por si llega a faltar

	return i
